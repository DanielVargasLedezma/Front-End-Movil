package com.example.app_matricula_movil.ui.view.fragment.alumnos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.app_matricula_movil.data.models.alumno.AlumnoComplejo
import com.example.app_matricula_movil.databinding.FragmentAlumnoBinding


class AlumnoFragment : Fragment() {
    private val ARG_PARAM1 = "param1"

    private var alumnoAVer: AlumnoComplejo? = null

    private var _binding: FragmentAlumnoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            alumnoAVer = it.getSerializable(ARG_PARAM1) as AlumnoComplejo?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlumnoBinding.inflate(inflater, container, false)

        binding.apply {
            cedulaAlumno.setText(alumnoAVer!!.cedula_alumno)
            nombreAlumno.setText(alumnoAVer!!.nombre)
            telAlumno.setText(alumnoAVer!!.telefono.toString())
            emailAlumno.setText(alumnoAVer!!.correo)
            fechaNacimiento.setText(alumnoAVer!!.fecha_nacimiento)
            carrera.setText(alumnoAVer!!.carrera.nombre)

        }

        return binding.root
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
         * @return A new instance of fragment CursoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: AlumnoComplejo) =
            AlumnoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                }
            }
    }

}