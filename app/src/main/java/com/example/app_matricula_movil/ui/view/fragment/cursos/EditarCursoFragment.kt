package com.example.app_matricula_movil.ui.view.fragment.cursos

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.Usuario
import com.example.app_matricula_movil.data.models.carrera.CarreraCompleja
import com.example.app_matricula_movil.data.models.curso.Curso
import com.example.app_matricula_movil.data.models.curso.CursoComplejo
import com.example.app_matricula_movil.data.repository.CarreraRepository
import com.example.app_matricula_movil.data.repository.CursoRepository
import com.example.app_matricula_movil.databinding.FragmentEditarCursoBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import com.example.app_matricula_movil.ui.view.fragment.carreras.CarrerasFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [EditarCursoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditarCursoFragment : Fragment() {
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"

    private var cursoAEditar: CursoComplejo? = null
    private var carreraCompleja: CarreraCompleja? = null

    private var _binding: FragmentEditarCursoBinding? = null
    private val binding get() = _binding!!

    /*
     * Se usa un array adapter para setearlo a cada spinner; cada adapter debe diferir porque es uno por spinner. Son
     * lateinit porque se inician después con los propios métodos de iniciar. En general, se quieren de tipo string para
     * hacerle display a los nombres y luego relacionar lo que se selecciona en el spinner en su evento con algo a
     * conveniencia.
     */
    private lateinit var adapterYear: ArrayAdapter<String>
    private lateinit var adapterCiclo: ArrayAdapter<String>
    private lateinit var adapterCarrera: ArrayAdapter<String>

    private val carreras: ArrayList<CarreraCompleja> = arrayListOf()

    private val cursoAEnviar: Curso = Curso()

    private val carreraRepository = CarreraRepository()
    private val cursoRepository = CursoRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cursoAEditar = it.getSerializable(ARG_PARAM1) as CursoComplejo?
            carreraCompleja = it.getSerializable(ARG_PARAM2) as CarreraCompleja?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditarCursoBinding.inflate(inflater, container, false)
        setAdapterYear()
        setAdapterCiclo()

        CoroutineScope(Dispatchers.IO).launch {
            val response = carreraRepository.getCarreras((activity as NavdrawActivity).token!!)

            if (response != null) {
                activity!!.runOnUiThread {
                    carreras.addAll(response.carreras)
                }
            }

            activity!!.runOnUiThread {
                setAdapterCarrera()
                fillInputsByDefault()
            }
        }

        binding.apply {
            descartar.setOnClickListener {
                fillInputsByDefault()
            }

            insertar.setOnClickListener {
                if (codigoCurso.text.isNotEmpty() && nombreCurso.text.isNotEmpty() && horasSemanales.text.isNotEmpty()
                    && creditosCurso.text.isNotEmpty() && cursoAEnviar.codigo_carrera.isNotEmpty()
                    && cursoAEnviar.anyo_a_llevar.isNotEmpty() && cursoAEnviar.num_semestre_a_llevar != 0
                ) {
                    AlertDialog.Builder(this@EditarCursoFragment.context!!).apply {
                        setTitle("¿Está seguro de guardar los cambios de este curso?")
                        setMessage("Esta acción editará el curso del sistema.")

                        setPositiveButton("Aceptar") { _: DialogInterface, _: Int ->
                            cursoAEnviar.codigo_curso = codigoCurso.text.toString()
                            cursoAEnviar.nombre = nombreCurso.text.toString()
                            cursoAEnviar.horas_semanales = horasSemanales.text.toString().toInt()
                            cursoAEnviar.creditos = creditosCurso.text.toString().toInt()

                            CoroutineScope(Dispatchers.IO).launch {
                                val response =
                                    cursoRepository.editarCurso(cursoAEnviar, (activity as NavdrawActivity).token!!)

                                if (response) {
                                    activity!!.runOnUiThread {
                                        if (carreraCompleja != null) {
                                            (activity as NavdrawActivity).supportActionBar?.title =
                                                "Carreras Registradas"

                                            swapFragments(
                                                CarrerasFragment.newInstance()
                                            )
                                        } else iniciarCursos()
                                    }
                                } else {
                                    activity!!.runOnUiThread {
                                        Toast.makeText(
                                            this@EditarCursoFragment.context,
                                            "Error al editar",
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
                        this@EditarCursoFragment.context,
                        "Campos sin rellenar o sin elegir",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            goBack.setOnClickListener {
                iniciarCursos()
            }
        }

        return binding.root
    }

    /*
     * Método encapsulado para no tener mucho desmadre en el código. Primero se inicializa el array adapter en cuestión
     * con el contexto y un tipo de layout de item; diría que es personalizable.
     */
    private fun setAdapterCarrera() {
        binding.apply {
            adapterCarrera = ArrayAdapter<String>(
                this@EditarCursoFragment.context!!,
                R.layout.spinner_list_carreer_first
            )

            adapterCarrera.setDropDownViewResource(R.layout.spinner_list_item)

            adapterCarrera.add("Seleccionar carrera")

            /*
             * En este caso, como llenamos con datos que están dentro de una entidad, recorremos el array que sacamos del
             * API con las carreras; esto para conseguir el nombre y el código de la carrera en este caso.
             */
            for (carrera in carreras) {
                adapterCarrera.add("${carrera.codigo_carrera}  ${carrera.nombre}")
            }

            /*
             * Se le asigna el adapter que recién se llenó al spinner respectivo; esto hace que se muestren los datos con
             * los que se llenaron.
             */
            carreraSpinner.adapter = adapterCarrera

            carreraSpinner.textAlignment = View.TEXT_ALIGNMENT_CENTER

            /*
             * Le ponemos un listener custom al spinner para cuando se seleccione un item, esto por medio de un objeto
             * que implementa la interfaz de listener para así implementar los métodos de una forma diferente para cada
             * spinner. En general solo se implementa el primer método, pero el segundo podría servir como validación
             * en caso de que no seleccione nada; en este caso le asignamos al curso a insertar en una posición menos a
             * la seleccionada por si se seleccionó el "seleccionar"
             */
            carreraSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    if (position > 0) cursoAEnviar.codigo_carrera = carreras[position - 1].codigo_carrera
                    else cursoAEnviar.codigo_carrera = ""
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    private fun setAdapterCiclo() {
        binding.apply {
            adapterCiclo =
                ArrayAdapter<String>(this@EditarCursoFragment.context!!, R.layout.spinner_list_cycle_first)

            adapterCiclo.setDropDownViewResource(R.layout.spinner_list_item)

            adapterCiclo.addAll(listOf("Seleccionar ciclo", "Primer Ciclo", "Segundo Ciclo"))

            cicloACursar.adapter = adapterCiclo

            cicloACursar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    cursoAEnviar.num_semestre_a_llevar = position
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}

            }
        }
    }

    private fun setAdapterYear() {
        binding.apply {
            adapterYear =
                ArrayAdapter<String>(this@EditarCursoFragment.context!!, R.layout.spinner_list_year_first)

            adapterYear.setDropDownViewResource(R.layout.spinner_list_item)

            adapterYear.addAll(
                listOf(
                    "Seleccionar año",
                    "Primer Año",
                    "Segundo Año",
                    "Tercer Año",
                    "Cuarto Año",
                    "Quinto Año"
                )
            )

            anyoACursar.adapter = adapterYear

            anyoACursar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    when (position) {
                        0 -> cursoAEnviar.anyo_a_llevar = ""
                        1 -> cursoAEnviar.anyo_a_llevar = "Primero"
                        2 -> cursoAEnviar.anyo_a_llevar = "Segundo"
                        3 -> cursoAEnviar.anyo_a_llevar = "Tercer"
                        4 -> cursoAEnviar.anyo_a_llevar = "Cuarto"
                        5 -> cursoAEnviar.anyo_a_llevar = "Quinto"
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    private fun iniciarCursos() {
        if (carreraCompleja == null) (activity as NavdrawActivity).supportActionBar?.title = "Cursos Registrados"
        else (activity as NavdrawActivity).supportActionBar?.title = "Cursos de ${carreraCompleja!!.codigo_carrera}"

        swapFragments(
            CursosFragment.newInstance(
                carreraCompleja
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

    private fun fillInputsByDefault() {
        binding.apply {
            codigoCurso.setText(cursoAEditar!!.codigo_curso)
            nombreCurso.setText(cursoAEditar!!.nombre)
            horasSemanales.setText(cursoAEditar!!.horas_semanales.toString())
            creditosCurso.setText(cursoAEditar!!.creditos.toString())

            when (cursoAEditar!!.anyo_a_llevar) {
                "Primero" -> {
                    anyoACursar.setSelection(1)
                }
                "Segundo" -> {
                    anyoACursar.setSelection(2)
                }
                "Tercer" -> {
                    anyoACursar.setSelection(3)
                }
                "Cuarto" -> {
                    anyoACursar.setSelection(4)
                }
                "Quinto" -> {
                    anyoACursar.setSelection(5)
                }
            }

            cicloACursar.setSelection(cursoAEditar!!.num_semestre_a_llevar)

            var i = 1
            for (carrera in carreras) {
                if (carrera.codigo_carrera == cursoAEditar!!.carrera.codigo_carrera) {
                    carreraSpinner.setSelection(i)
                    break
                }
                i++
            }
        }
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
         * @param cursoAEditar Curso a editar.
         * @param carreraElegida Carrera de las cuales se están visualizando los cursos.
         * @return A new instance of fragment EditarCursoFragment.
         */
        @JvmStatic
        fun newInstance(cursoAEditar: CursoComplejo, carreraElegida: CarreraCompleja? = null) =
            EditarCursoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, cursoAEditar)
                    if (carreraElegida != null) putSerializable(ARG_PARAM2, carreraElegida)
                }
            }
    }
}