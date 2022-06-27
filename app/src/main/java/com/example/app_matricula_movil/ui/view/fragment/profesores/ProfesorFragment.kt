package com.example.app_matricula_movil.ui.view.fragment.profesores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.alumno.AlumnoComplejo
import com.example.app_matricula_movil.data.models.carrera.CarreraCompleja
import com.example.app_matricula_movil.data.models.profesor.Profesor
import com.example.app_matricula_movil.databinding.FragmentAlumnoBinding
import com.example.app_matricula_movil.databinding.FragmentProfesorBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import com.example.app_matricula_movil.ui.view.fragment.alumnos.AlumnosFragment
import com.example.app_matricula_movil.ui.view.fragment.carreras.CarreraFragment
import com.example.app_matricula_movil.ui.view.fragment.cursos.CursosFragment
import com.example.app_matricula_movil.ui.view.fragment.grupos.GruposFragment
import com.example.app_matricula_movil.ui.view.fragment.matricula.MatriculasFragment

class ProfesorFragment : Fragment() {
    private val ARG_PARAM1 = "param1"

    private var profesorAVer: Profesor? = null

    private var _binding: FragmentProfesorBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            profesorAVer = it.getSerializable(ARG_PARAM1) as Profesor?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfesorBinding.inflate(inflater, container, false)

        binding.apply {
            cedulaProfesor.setText(profesorAVer!!.cedula_profesor)
            nombreProfesor.setText(profesorAVer!!.nombre)
            telProfesor.setText(profesorAVer!!.telefono.toString())
            emailProfesor.setText(profesorAVer!!.correo)

            goBack.setOnClickListener {
                iniciarProfesores()
            }

            return binding.root
        }
    }

    private fun iniciarProfesores() {
        (activity as NavdrawActivity).supportActionBar?.title =
            "Profesores Registrados"

        swapFragments(
            AlumnosFragment.newInstance(
                (activity as NavdrawActivity).token!!, (activity as NavdrawActivity).userLogged!!
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
         * @param carreraAVer Carrera seleccionada a ver.
         * @return A new instance of fragment CarreraFragment.
         */
        @JvmStatic
        fun newInstance(profesorAVer: Profesor) =
            ProfesorFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, profesorAVer)
                }
            }
    }

}