package com.example.app_matricula_movil.ui.view.fragment.alumnos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.Usuario
import com.example.app_matricula_movil.data.models.alumno.Alumno
import com.example.app_matricula_movil.data.models.carrera.CarreraCompleja
import com.example.app_matricula_movil.data.repository.AlumnoRepository
import com.example.app_matricula_movil.data.repository.CarreraRepository
import com.example.app_matricula_movil.databinding.FragmentCrearAlumnosBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CrearAlumnoFragment : Fragment() {
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"

    private var token: String? = null
    private var usuarioLoggeado: Usuario? = null
    private var carreraCompleja: CarreraCompleja? = null

    private var _binding: FragmentCrearAlumnosBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterCarrera: ArrayAdapter<String>

    private val carreras: ArrayList<CarreraCompleja> = arrayListOf()

    private val alumnoAInsertar = Alumno()

    private val carreraRepository = CarreraRepository()
    private val alumnoRepository = AlumnoRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            token = it.getString(ARG_PARAM1)
            usuarioLoggeado = it.getSerializable(ARG_PARAM2) as Usuario?

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrearAlumnosBinding.inflate(inflater, container, false)

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
            goBack.setOnClickListener {
                iniciarAlumnos()
            }

            insertar.setOnClickListener {
                if (cedulaAlumno.text.isNotEmpty() && nombreAlumno.text.isNotEmpty() && telAlumno.text.isNotEmpty()
                    && emailAlumno.text.isNotEmpty() && alumnoAInsertar.codigo_carrera.isNotEmpty()
                ) {

                    alumnoAInsertar.cedula_alumno = cedulaAlumno.text.toString()
                    alumnoAInsertar.nombre = nombreAlumno.text.toString()
                    alumnoAInsertar.telefono = telAlumno.text.toString().toInt()
                    alumnoAInsertar.correo = emailAlumno.text.toString()
                    alumnoAInsertar.fecha_nacimiento = getGoodDate()

                    CoroutineScope(Dispatchers.IO).launch {
                        val response = alumnoRepository.registrarAlumno(alumnoAInsertar, token!!)

                        if (response) {
                            activity!!.runOnUiThread {
                                iniciarAlumnos()
                            }
                        } else {
                            activity!!.runOnUiThread {
                                Toast.makeText(
                                    this@CrearAlumnoFragment.context,
                                    "Error al insertar",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        this@CrearAlumnoFragment.context,
                        "Campos sin rellenar o sin elegir",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        return binding.root
    }

    private fun getGoodDate(): String {
        val values = binding.fechaNacimiento.text!!.toString().substring(0, 10).split("-")

        return "${values[2]}-${values[1]}-${values[0]}"
    }

    /*
     * Método encapsulado para no tener mucho desmadre en el código. Primero se inicializa el array adapter en cuestión
     * con el contexto y un tipo de layout de item; diría que es personalizable.
     */
    private fun setAdapterCarrera() {
        binding.apply {
            adapterCarrera = ArrayAdapter<String>(
                this@CrearAlumnoFragment.context!!,
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
                    if (position > 0) alumnoAInsertar.codigo_carrera = carreras[position - 1].codigo_carrera
                    else alumnoAInsertar.codigo_carrera = ""
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }


    private fun iniciarAlumnos() {
        val fragmentTransaction = parentFragmentManager.beginTransaction()

        fragmentTransaction.replace(
            R.id.contentMain, AlumnosFragment.newInstance(
                token!!, usuarioLoggeado!!
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
        fun newInstance(param1: String, param2: Usuario) =
            CrearAlumnoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putSerializable(ARG_PARAM2, param2)


                }
            }
    }
}