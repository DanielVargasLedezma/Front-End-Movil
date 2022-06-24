package com.example.app_matricula_movil.ui.view.fragment.grupos

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.ciclo.Ciclo
import com.example.app_matricula_movil.data.models.curso.CursoComplejo
import com.example.app_matricula_movil.data.models.grupo.Grupo
import com.example.app_matricula_movil.data.models.grupo.GrupoComplejo
import com.example.app_matricula_movil.data.models.profesor.Profesor
import com.example.app_matricula_movil.data.repository.CicloRepository
import com.example.app_matricula_movil.data.repository.GrupoRepository
import com.example.app_matricula_movil.data.repository.ProfesorRepository
import com.example.app_matricula_movil.databinding.FragmentEditarGrupoBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [EditarGrupoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditarGrupoFragment : Fragment() {
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"
    private val ARG_PARAM3 = "param3"

    private var grupoAEditar: GrupoComplejo? = null
    private var cursoElegido: CursoComplejo? = null
    private var viendoVista: String? = null

    private var _binding: FragmentEditarGrupoBinding? = null
    private val binding get() = _binding!!

    /*
     * Se usa un array adapter para setearlo a cada spinner; cada adapter debe diferir porque es uno por spinner. Son
     * lateinit porque se inician después con los propios métodos de iniciar. En general, se quieren de tipo string para
     * hacerle display a los nombres y luego relacionar lo que se selecciona en el spinner en su evento con algo a
     * conveniencia.
     */
    private lateinit var adapterProfesor: ArrayAdapter<String>
    private lateinit var adapterCiclo: ArrayAdapter<String>
    private lateinit var adapterDiaUno: ArrayAdapter<String>
    private lateinit var adapterDiaDos: ArrayAdapter<String>

    private val ciclos: ArrayList<Ciclo> = arrayListOf()
    private val profesores: ArrayList<Profesor> = arrayListOf()

    private val grupoAInsertar = Grupo()

    private val cicloRepository = CicloRepository()
    private val profesorRepository = ProfesorRepository()
    private val grupoRepository = GrupoRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            grupoAEditar = it.getSerializable(ARG_PARAM1) as GrupoComplejo?
            cursoElegido = it.getSerializable(ARG_PARAM2) as CursoComplejo?
            viendoVista = it.getString(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditarGrupoBinding.inflate(inflater, container, false)

        setAdapterDias()

        CoroutineScope(Dispatchers.IO).launch {
            val responseUno = cicloRepository.getCiclos((activity as NavdrawActivity).token!!)
            val responseDos = profesorRepository.getProfesores((activity as NavdrawActivity).token!!)

            if (responseUno != null) {
                activity!!.runOnUiThread {
                    ciclos.addAll(responseUno.ciclos)
                }
            }

            if (responseDos != null) {
                activity!!.runOnUiThread {
                    profesores.addAll(responseDos.profesores)
                }
            }

            activity!!.runOnUiThread {
                setAdapterProfesor()
                setAdapterCiclo()
                fillInputsByDefault()
            }
        }

        binding.apply {
            insertar.setOnClickListener {
                if (numeroGrupo.text.isNotEmpty() && grupoAInsertar.cedula_profesor.isNotEmpty() &&
                    grupoAInsertar.id_ciclo != 0 && grupoAInsertar.dia_uno.isNotEmpty() && horaInicio.text.isNotEmpty()
                    && horaFinal.text.isNotEmpty()
                ) {
                    grupoAInsertar.numero_grupo = numeroGrupo.text.toString()
                    grupoAInsertar.horario = "${horaInicio.text}-${horaFinal.text}"
                    grupoAInsertar.codigo_curso = cursoElegido!!.codigo_curso

                    if (grupoAInsertar.dia_uno == grupoAInsertar.dia_dos) grupoAInsertar.dia_dos = ""

                    AlertDialog.Builder(this@EditarGrupoFragment.context!!).apply {
                        setTitle("¿Está seguro de guardar los cambios de este grupo?")
                        setMessage("Esta acción editará el grupo del sistema.")

                        setPositiveButton("Aceptar") { _: DialogInterface, _: Int ->
                            CoroutineScope(Dispatchers.IO).launch {
                                val response =
                                    grupoRepository.editarGrupo(grupoAInsertar, (activity as NavdrawActivity).token!!)

                                if (response) {
                                    activity!!.runOnUiThread {
                                        iniciarCursos()
                                    }
                                } else {
                                    activity!!.runOnUiThread {
                                        Toast.makeText(
                                            this@EditarGrupoFragment.context,
                                            "Error al insertar",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                            }
                        }

                        setNegativeButton("Cancelar", null)
                    }.show()
                } else {
                    Toast.makeText(
                        this@EditarGrupoFragment.context,
                        "Campos sin rellenar o sin elegir",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            descartar.setOnClickListener {
                fillInputsByDefault()
            }

            goBack.setOnClickListener {
                iniciarCursos()
            }
        }

        return binding.root
    }

    private fun fillInputsByDefault() {
        val horas = grupoAEditar!!.horario.split("-")

        binding.apply {
            numeroGrupo.setText(grupoAEditar!!.numero_grupo)
            horaInicio.setText(horas[0])
            horaFinal.setText(horas[1])

            setSelectedProfesor()
            setSelectedCiclo()
            setSelectedDias()
        }
    }

    /*
     * Método encapsulado para no tener mucho desmadre en el código. Primero se inicializa el array adapter en cuestión
     * con el contexto y un tipo de layout de item; diría que es personalizable.
     */
    private fun setAdapterProfesor() {
        binding.apply {
            adapterProfesor = ArrayAdapter<String>(
                this@EditarGrupoFragment.context!!,
                R.layout.spinner_list_teacher_first
            )

            adapterProfesor.setDropDownViewResource(R.layout.spinner_list_item)

            adapterProfesor.add("Seleccionar profesor")

            /*
             * En este caso, como llenamos con datos que están dentro de una entidad, recorremos el array que sacamos del
             * API con las carreras; esto para conseguir el nombre y el código de la carrera en este caso.
             */
            for (profesor in profesores) {
                adapterProfesor.add("${profesor.cedula_profesor}  ${profesor.nombre}")
            }

            /*
             * Se le asigna el adapter que recién se llenó al spinner respectivo; esto hace que se muestren los datos con
             * los que se llenaron.
             */
            profesorSpinner.adapter = adapterProfesor

            /*
             * Le ponemos un listener custom al spinner para cuando se seleccione un item, esto por medio de un objeto
             * que implementa la interfaz de listener para así implementar los métodos de una forma diferente para cada
             * spinner. En general solo se implementa el primer método, pero el segundo podría servir como validación
             * en caso de que no seleccione nada; en este caso le asignamos al curso a insertar en una posición menos a
             * la seleccionada por si se seleccionó el "seleccionar"
             */
            profesorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    if (position > 0) grupoAInsertar.cedula_profesor = profesores[position - 1].cedula_profesor
                    else grupoAInsertar.cedula_profesor = ""
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    /*
     * Método encapsulado para no tener mucho desmadre en el código. Primero se inicializa el array adapter en cuestión
     * con el contexto y un tipo de layout de item; diría que es personalizable.
     */
    private fun setAdapterCiclo() {
        binding.apply {
            adapterCiclo = ArrayAdapter<String>(
                this@EditarGrupoFragment.context!!,
                R.layout.spinner_list_cycle_first
            )

            adapterCiclo.setDropDownViewResource(R.layout.spinner_list_item)

            /*
             * En este caso, como llenamos con datos que están dentro de una entidad, recorremos el array que sacamos del
             * API con las carreras; esto para conseguir el nombre y el código de la carrera en este caso.
             */
            for (ciclo in ciclos) {
                adapterCiclo.add(getCicloText(ciclo))
            }

            /*
             * Se le asigna el adapter que recién se llenó al spinner respectivo; esto hace que se muestren los datos con
             * los que se llenaron.
             */
            cicloSpinner.adapter = adapterCiclo

            /*
             * Le ponemos un listener custom al spinner para cuando se seleccione un item, esto por medio de un objeto
             * que implementa la interfaz de listener para así implementar los métodos de una forma diferente para cada
             * spinner. En general solo se implementa el primer método, pero el segundo podría servir como validación
             * en caso de que no seleccione nada; en este caso le asignamos al curso a insertar en una posición menos a
             * la seleccionada por si se seleccionó el "seleccionar"
             */
            cicloSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    grupoAInsertar.id_ciclo = ciclos[position].id_ciclo
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    private fun setAdapterDias() {
        binding.apply {
            adapterDiaUno =
                ArrayAdapter<String>(this@EditarGrupoFragment.context!!, R.layout.spinner_list_year_first)

            adapterDiaDos =
                ArrayAdapter<String>(this@EditarGrupoFragment.context!!, R.layout.spinner_list_year_first)

            adapterDiaUno.setDropDownViewResource(R.layout.spinner_list_item)
            adapterDiaDos.setDropDownViewResource(R.layout.spinner_list_item)

            adapterDiaUno.addAll(listOf("Dia - Primero", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes"))
            adapterDiaDos.addAll(listOf("Dia - Segundo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes"))

            diaUnoSpinner.adapter = adapterDiaUno

            diaDosSpinner.adapter = adapterDiaDos

            diaUnoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    if (position == 3) grupoAInsertar.dia_uno = "W"
                    else if (position > 0) grupoAInsertar.dia_uno =
                        adapterDiaUno.getItem(position).toString()[0].toString()
                    else grupoAInsertar.dia_uno = ""
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}

            }

            diaDosSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    if (position == 3) grupoAInsertar.dia_dos = "W"
                    else if (position > 0) grupoAInsertar.dia_dos =
                        adapterDiaDos.getItem(position).toString()[0].toString()
                    else grupoAInsertar.dia_dos = ""
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

            setSelectedDias()
        }
    }

    private fun setSelectedDias() {
        when (grupoAEditar!!.dia_uno) {
            "L" -> binding.diaUnoSpinner.setSelection(1)
            "M" -> binding.diaUnoSpinner.setSelection(2)
            "W" -> binding.diaUnoSpinner.setSelection(3)
            "J" -> binding.diaUnoSpinner.setSelection(4)
            "V" -> binding.diaUnoSpinner.setSelection(5)
        }

        if (grupoAEditar!!.dia_dos != null && grupoAEditar!!.dia_dos.isNotEmpty()
            && grupoAEditar!!.dia_dos != "null"
        ) {
            when (grupoAEditar!!.dia_dos) {
                "L" -> binding.diaDosSpinner.setSelection(1)
                "M" -> binding.diaDosSpinner.setSelection(2)
                "W" -> binding.diaDosSpinner.setSelection(3)
                "J" -> binding.diaDosSpinner.setSelection(4)
                "V" -> binding.diaDosSpinner.setSelection(5)
            }
        }
    }

    private fun setSelectedCiclo() {
        for ((i, ciclo) in ciclos.withIndex()) {
            if (ciclo.id_ciclo == grupoAEditar!!.ciclo.id_ciclo) {
                binding.cicloSpinner.setSelection(i)
                grupoAInsertar.id_ciclo = ciclo.id_ciclo
                break
            }
        }
    }

    private fun setSelectedProfesor() {
        for ((i, profesor) in profesores.withIndex()) {
            if (profesor.cedula_profesor == grupoAEditar!!.profesor.cedula_profesor) {
                binding.profesorSpinner.setSelection(i + 1)
                grupoAInsertar.cedula_profesor = profesor.cedula_profesor
                break
            }
        }
    }

    private fun getCicloText(ciclo: Ciclo): String {
        var returnValue = "Ciclo "

        when (ciclo.numero_ciclo) {
            1 -> returnValue += "I de ${ciclo.year}"
            2 -> returnValue += "II de ${ciclo.year}"
        }

        return returnValue
    }

    private fun iniciarCursos() {
        (activity as NavdrawActivity).supportActionBar?.title = "Grupos del curso ${cursoElegido!!.codigo_curso}"

        swapFragments(
            GruposFragment.newInstance(
                cursoElegido!!, viendoVista!!
            )
        )
    }

    private fun swapFragments(fragment: Fragment) {
        val fragmentTransaction = parentFragmentManager.beginTransaction()

        fragmentTransaction.replace(
            R.id.contentMain, fragment
        )

        fragmentTransaction.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditarGrupoFragment.
         */
        @JvmStatic
        fun newInstance(grupoAEditar: GrupoComplejo, cursoElegido: CursoComplejo? = null, tipoVista: String? = null) =
            EditarGrupoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, grupoAEditar)
                    putSerializable(ARG_PARAM2, cursoElegido)
                    putString(ARG_PARAM3, tipoVista)
                }
            }
    }
}