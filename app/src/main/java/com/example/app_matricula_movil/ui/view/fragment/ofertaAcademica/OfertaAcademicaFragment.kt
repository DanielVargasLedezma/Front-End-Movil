package com.example.app_matricula_movil.ui.view.fragment.ofertaAcademica

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.Usuario
import com.example.app_matricula_movil.data.models.carrera.CarreraCompleja
import com.example.app_matricula_movil.data.models.curso.CursoComplejo
import com.example.app_matricula_movil.data.repository.CarreraRepository
import com.example.app_matricula_movil.databinding.FragmentOfertaAcademicaBinding
import com.example.app_matricula_movil.ui.view.fragment.cursos.CursosFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [OfertaAcademicaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OfertaAcademicaFragment : Fragment() {
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"

    private var usuarioLoggeado: Usuario? = null
    private var token: String? = null

    private var _binding: FragmentOfertaAcademicaBinding? = null
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

    private val carreraRepository = CarreraRepository()

    private var carreraComplejaSeleccionada: CarreraCompleja? = null
    private var yearSelected = ""
    private var cycleSelected = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            usuarioLoggeado = it.getSerializable(ARG_PARAM1) as Usuario?
            token = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOfertaAcademicaBinding.inflate(inflater, container, false)

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
            }
        }

        binding.apply {
            buscar.setOnClickListener {
                if (carreraComplejaSeleccionada != null && yearSelected != "" && cycleSelected != 0) {
                    val cursosFiltrados =
                        carreraComplejaSeleccionada!!.cursos.filter {
                            it.anyo_a_llevar == yearSelected && it.num_semestre_a_llevar == cycleSelected
                        }

                    swapFragments(
                        CursosFragment.newInstance(
                            token!!, usuarioLoggeado!!, CarreraCompleja(
                                carreraComplejaSeleccionada!!.codigo_carrera,
                                carreraComplejaSeleccionada!!.nombre,
                                carreraComplejaSeleccionada!!.creditos,
                                ArrayList<CursoComplejo>(cursosFiltrados),
                            ), "OfertaAcademica"
                        )
                    )

                } else {
                    Toast.makeText(
                        this@OfertaAcademicaFragment.context,
                        "Campos sin elegir",
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
                this@OfertaAcademicaFragment.context!!,
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
                    carreraComplejaSeleccionada = if (position > 0) carreras[position - 1]
                    else null
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    private fun setAdapterCiclo() {
        binding.apply {
            adapterCiclo =
                ArrayAdapter<String>(this@OfertaAcademicaFragment.context!!, R.layout.spinner_list_cycle_first)

            adapterCiclo.setDropDownViewResource(R.layout.spinner_list_item)

            adapterCiclo.addAll(listOf("Seleccionar ciclo", "Primer Ciclo", "Segundo Ciclo"))

            cicloABuscar.adapter = adapterCiclo

            cicloABuscar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    cycleSelected = position
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}

            }
        }
    }

    private fun setAdapterYear() {
        binding.apply {
            adapterYear =
                ArrayAdapter<String>(this@OfertaAcademicaFragment.context!!, R.layout.spinner_list_year_first)

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

            anyoABuscar.adapter = adapterYear

            anyoABuscar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    when (position) {
                        0 -> {
                            yearSelected = ""
                        }
                        1 -> {
                            yearSelected = "Primero"
                        }
                        2 -> {
                            yearSelected = "Segundo"
                        }
                        3 -> {
                            yearSelected = "Tercer"
                        }
                        4 -> {
                            yearSelected = "Cuarto"
                        }
                        5 -> {
                            yearSelected = "Quinto"
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
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
         * @return A new instance of fragment OfertaAcademicaFragment.
         */
        @JvmStatic
        fun newInstance(param1: Usuario, param2: String) =
            OfertaAcademicaFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}