package com.example.app_matricula_movil.ui.view.fragment.cursos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.Usuario
import com.example.app_matricula_movil.data.models.carrera.CarreraCompleja
import com.example.app_matricula_movil.data.models.curso.Curso
import com.example.app_matricula_movil.data.repository.CarreraRepository
import com.example.app_matricula_movil.data.repository.CursoRepository
import com.example.app_matricula_movil.databinding.FragmentCrearCursoBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [CrearCursoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CrearCursoFragment : Fragment() {
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"
    private val ARG_PARAM3 = "param3"

    private var token: String? = null
    private var usuarioLoggeado: Usuario? = null
    private var carreraCompleja: CarreraCompleja? = null

    private var _binding: FragmentCrearCursoBinding? = null
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

    private val cursoAInsertar = Curso()

    private val carreraRepository = CarreraRepository()
    private val cursoRepository = CursoRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            token = it.getString(ARG_PARAM1)
            usuarioLoggeado = it.getSerializable(ARG_PARAM2) as Usuario?
            carreraCompleja = it.getSerializable(ARG_PARAM3) as CarreraCompleja?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrearCursoBinding.inflate(inflater, container, false)

        setAdapterYear()
        setAdapterCiclo()

        CoroutineScope(Dispatchers.IO).launch {
            val response = carreraRepository.getCarreras(token!!)

            if (response != null) {
                activity!!.runOnUiThread {
                    carreras.addAll(response.carreras)
                }
            }

            activity!!.runOnUiThread {
                setAdapterCarrera()

                if (carreraCompleja != null) {
                    binding.apply {
                        var i = 1
                        for (carrera in carreras) {
                            if (carrera.codigo_carrera == carreraCompleja!!.codigo_carrera) {
                                carreraSpinner.setSelection(i)
                                carreraSpinner.isEnabled = false
                                break
                            }
                            i++
                        }
                    }
                }
            }
        }

        binding.apply {
            insertar.setOnClickListener {
                if (codigoCurso.text.isNotEmpty() && nombreCurso.text.isNotEmpty() && horasSemanales.text.isNotEmpty()
                    && creditosCurso.text.isNotEmpty() && cursoAInsertar.codigo_carrera.isNotEmpty()
                    && cursoAInsertar.anyo_a_llevar.isNotEmpty() && cursoAInsertar.num_semestre_a_llevar != 0
                ) {

                    cursoAInsertar.codigo_curso = codigoCurso.text.toString()
                    cursoAInsertar.nombre = nombreCurso.text.toString()
                    cursoAInsertar.horas_semanales = horasSemanales.text.toString().toInt()
                    cursoAInsertar.creditos = creditosCurso.text.toString().toInt()

                    CoroutineScope(Dispatchers.IO).launch {
                        val response = cursoRepository.insertarCurso(cursoAInsertar, token!!)

                        if (response) {
                            activity!!.runOnUiThread {
                                iniciarCursos()
                            }
                        } else {
                            activity!!.runOnUiThread {
                                Toast.makeText(
                                    this@CrearCursoFragment.context,
                                    "Error al insertar",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        this@CrearCursoFragment.context,
                        "Campos sin rellenar o sin elegir",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
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
                this@CrearCursoFragment.context!!,
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
            carreraSpinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    if (position > 0) cursoAInsertar.codigo_carrera = carreras[position - 1].codigo_carrera
                    else cursoAInsertar.codigo_carrera = ""
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    private fun setAdapterCiclo() {
        binding.apply {
            adapterCiclo =
                ArrayAdapter<String>(this@CrearCursoFragment.context!!, R.layout.spinner_list_cycle_first)

            adapterCiclo.setDropDownViewResource(R.layout.spinner_list_item)

            adapterCiclo.addAll(listOf("Seleccionar ciclo", "Primer Ciclo", "Segundo Ciclo"))

            cicloACursar.adapter = adapterCiclo

            cicloACursar.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    cursoAInsertar.num_semestre_a_llevar = position
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}

            }
        }
    }

    private fun setAdapterYear() {
        binding.apply {
            adapterYear =
                ArrayAdapter<String>(this@CrearCursoFragment.context!!, R.layout.spinner_list_year_first)

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

            anyoACursar.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    when (position) {
                        0 -> {
                            cursoAInsertar.anyo_a_llevar = ""
                        }
                        1 -> {
                            cursoAInsertar.anyo_a_llevar = "Primero"
                        }
                        2 -> {
                            cursoAInsertar.anyo_a_llevar = "Segundo"
                        }
                        3 -> {
                            cursoAInsertar.anyo_a_llevar = "Tercer"
                        }
                        4 -> {
                            cursoAInsertar.anyo_a_llevar = "Cuarto"
                        }
                        5 -> {
                            cursoAInsertar.anyo_a_llevar = "Quinto"
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    private fun iniciarCursos() {
        val fragmentTransaction = parentFragmentManager.beginTransaction()

        if (carreraCompleja == null) {
            (activity as NavdrawActivity).supportActionBar?.title = "Cursos Registrados"
        } else {
            (activity as NavdrawActivity).supportActionBar?.title = "Cursos de ${carreraCompleja!!.codigo_carrera}"
        }

        fragmentTransaction.replace(
            R.id.contentMain, CursosFragment.newInstance(
                token!!, usuarioLoggeado!!, carreraCompleja
            )
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
         * @return A new instance of fragment CrearCursoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: Usuario, param3: CarreraCompleja? = null) =
            CrearCursoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putSerializable(ARG_PARAM2, param2)

                    if (param3 != null) {
                        putSerializable(ARG_PARAM3, param3)
                    }
                }
            }
    }
}