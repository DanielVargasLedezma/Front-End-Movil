package com.example.app_matricula_movil.ui.view.fragment.alumnos

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.Usuario
import com.example.app_matricula_movil.data.models.alumno.Alumno
import com.example.app_matricula_movil.data.models.alumno.AlumnoComplejo
import com.example.app_matricula_movil.data.models.carrera.CarreraCompleja
import com.example.app_matricula_movil.data.models.curso.CursoComplejo
import com.example.app_matricula_movil.data.repository.AlumnoRepository
import com.example.app_matricula_movil.data.repository.CarreraRepository
import com.example.app_matricula_movil.databinding.FragmentEditarAlumnoBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import com.example.app_matricula_movil.ui.view.fragment.cursos.CursosFragment
import com.example.app_matricula_movil.ui.view.fragment.cursos.EditarCursoFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditarAlumnoFragment : Fragment(){

    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"
    private val ARG_PARAM3 = "param3"
    private val ARG_PARAM4 = "param4"

    private var alumnoAEditar: AlumnoComplejo? = null
    private var token: String? = null
    private var usuarioLoggeado: Usuario? = null


    private var _binding: FragmentEditarAlumnoBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterCarrera: ArrayAdapter<String>

    private val carreras: ArrayList<CarreraCompleja> = arrayListOf()

    private val alumnoAEnviar: Alumno = Alumno()

    private val alumnoRepository = AlumnoRepository()
    private val carreraRepository = CarreraRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            alumnoAEditar = it.getSerializable(ARG_PARAM1) as AlumnoComplejo?
            token = it.getString(ARG_PARAM2)
            usuarioLoggeado = it.getSerializable(ARG_PARAM3) as Usuario?

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditarAlumnoBinding.inflate(inflater, container, false)


        CoroutineScope(Dispatchers.IO).launch {
            val response = carreraRepository.getCarreras(token!!)

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

            goBack.setOnClickListener {
                iniciarAlumnos()
            }

            insertar.setOnClickListener {
                if (cedulaAlumno.text.isNotEmpty() && nombreAlumno.text.isNotEmpty() && telAlumno.text.isNotEmpty()
                    && emailAlumno.text.isNotEmpty() && alumnoAEnviar.codigo_carrera.isNotEmpty()
                ) {
                    AlertDialog.Builder(this@EditarAlumnoFragment.context!!).apply {
                        setTitle("¿Está seguro de guardar los cambios de este alumno?")
                        setMessage("Esta acción editará el alumno del sistema.")

                        setPositiveButton("Aceptar") { _: DialogInterface, _: Int ->
                            alumnoAEnviar.cedula_alumno = cedulaAlumno.text.toString()
                            alumnoAEnviar.nombre = nombreAlumno.text.toString()
                            alumnoAEnviar.telefono = telAlumno.text.toString().toInt()
                            alumnoAEnviar.correo = emailAlumno.text.toString()
                            alumnoAEnviar.fecha_nacimiento = fechaNacimiento.text.toString()

                            CoroutineScope(Dispatchers.IO).launch {
                                val response = alumnoRepository.editarAlumno(alumnoAEnviar, token!!)

                                if (response) {
                                    activity!!.runOnUiThread {
                                        iniciarAlumnos()
                                    }

                                } else {
                                    activity!!.runOnUiThread {
                                        Toast.makeText(
                                            this@EditarAlumnoFragment.context,
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
                        this@EditarAlumnoFragment.context,
                        "Campos sin rellenar o sin elegir",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        return binding.root
    }
    private fun setAdapterCarrera() {
        binding.apply {
            adapterCarrera = ArrayAdapter<String>(
                this@EditarAlumnoFragment.context!!,
                R.layout.spinner_list_carreer_first
            )

            adapterCarrera.setDropDownViewResource(R.layout.spinner_list_item)

            adapterCarrera.add("Seleccionar carrera")


            for (carrera in carreras) {
                adapterCarrera.add("${carrera.codigo_carrera}  ${carrera.nombre}")
            }

            carreraSpinner.adapter = adapterCarrera

            carreraSpinner.textAlignment = View.TEXT_ALIGNMENT_CENTER


            carreraSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    if (position > 0) alumnoAEnviar.codigo_carrera = carreras[position - 1].codigo_carrera
                    else alumnoAEnviar.codigo_carrera = ""
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

        }
    }
    private fun fillInputsByDefault() {
        binding.apply {
            cedulaAlumno.setText(alumnoAEditar!!.cedula_alumno)
            nombreAlumno.setText(alumnoAEditar!!.nombre)
            telAlumno.setText(alumnoAEditar!!.telefono.toString())
            emailAlumno.setText(alumnoAEditar!!.correo)
            fechaNacimiento.setText(alumnoAEditar!!.fecha_nacimiento)


            var i = 1
            for (carrera in carreras) {
                if (carrera.codigo_carrera == alumnoAEditar!!.carrera.codigo_carrera) {
                    carreraSpinner.setSelection(i)
                    break
                }
                i++
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
         * @return A new instance of fragment EditarCursoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: AlumnoComplejo, param2: String, param3: Usuario, ) =
            EditarAlumnoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putSerializable(ARG_PARAM3, param3)


                }
            }
    }

}