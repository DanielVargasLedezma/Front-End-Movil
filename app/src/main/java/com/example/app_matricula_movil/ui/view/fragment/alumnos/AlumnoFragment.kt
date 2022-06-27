package com.example.app_matricula_movil.ui.view.fragment.alumnos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.alumno.AlumnoComplejo
import com.example.app_matricula_movil.data.models.grupo.GrupoComplejo
import com.example.app_matricula_movil.data.repository.MatriculaRepository
import com.example.app_matricula_movil.databinding.FragmentAlumnoBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import com.example.app_matricula_movil.ui.view.fragment.grupos.GruposFragment
import com.example.app_matricula_movil.ui.view.fragment.matricula.MatriculasFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AlumnoFragment : Fragment() {
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"
    private val ARG_PARAM3 = "param3"

    private var alumnoAVer: AlumnoComplejo? = null
    private var tipoVista: String? = null
    private var grupoMatriculado: GrupoComplejo? = null

    private var _binding: FragmentAlumnoBinding? = null
    private val binding get() = _binding!!

    private val matriculaRepository = MatriculaRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            alumnoAVer = it.getSerializable(ARG_PARAM1) as AlumnoComplejo?
            tipoVista = it.getString(ARG_PARAM2)
            grupoMatriculado = it.getSerializable(ARG_PARAM3) as GrupoComplejo?
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
            fechaNacimiento.setText(getGoodDate())
            carrera.setText(alumnoAVer!!.carrera.nombre)

            val tipo_usuario = (activity as NavdrawActivity).userLogged!!.tipo_usuario

            if (tipo_usuario == 1) historialAcademico.visibility =
                View.VISIBLE

            if (tipo_usuario == 1 || tipo_usuario == 2) matriculas.visibility = View.VISIBLE

            goBack.setOnClickListener {
                iniciarAlumnos()
            }

            matriculas.setOnClickListener {
                (activity as NavdrawActivity).supportActionBar?.title =
                    "Grupos Matriculados"

                (activity as NavdrawActivity).supportActionBar?.subtitle = "Estudiante ${alumnoAVer!!.cedula_alumno}"

                if (tipo_usuario == 1) tipoVista = "GruposMatriculadosAlumno"

                swapFragments(
                    GruposFragment.newInstance(null, tipoVista, alumnoAVer)
                )
            }

            historialAcademico.setOnClickListener {
                (activity as NavdrawActivity).supportActionBar?.title =
                    "Historial Académico"

                (activity as NavdrawActivity).supportActionBar?.subtitle = "Estudiante ${alumnoAVer!!.cedula_alumno}"

                swapFragments(
                    MatriculasFragment.newInstance(alumnoAVer!!)
                )
            }

            if (tipo_usuario == 3) {
                nota.visibility = View.VISIBLE

                CoroutineScope(Dispatchers.IO).launch {
                    val response = matriculaRepository.getMatriculaAlumno(
                        alumnoAVer!!.cedula_alumno,
                        grupoMatriculado!!.numero_grupo,
                        (activity as NavdrawActivity).token!!
                    )

                    if (response != null) {
                        activity!!.runOnUiThread {
                            nota.setText(response.matricula.nota.toString())
                        }
                    }
                }
            }
        }

        return binding.root
    }

    private fun getGoodDate(): String {
        val values = alumnoAVer!!.fecha_nacimiento.substring(0, 10).split("-")

        return "${values[2]}-${values[1]}-${values[0]}"
    }

    private fun iniciarAlumnos() {
        (activity as NavdrawActivity).supportActionBar?.title =
            "Alumnos Registrados"

        val fragmentTransaction = parentFragmentManager.beginTransaction()

        fragmentTransaction.replace(
            R.id.contentMain, AlumnosFragment.newInstance(
                (activity as NavdrawActivity).token!!,
                (activity as NavdrawActivity).userLogged!!,
                tipoVista,
                grupoMatriculado
            )
        )

        fragmentTransaction.commit()
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
         * @param tipoVista Tipo de vista que se desea ver.
         * @return A new instance of fragment CursoFragment.
         */
        @JvmStatic
        fun newInstance(param1: AlumnoComplejo, tipoVista: String? = null, grupoComplejo: GrupoComplejo? = null) =
            AlumnoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    if (tipoVista != null) putString(ARG_PARAM2, tipoVista)
                    if (grupoComplejo != null) putSerializable(ARG_PARAM3, grupoComplejo)
                }
            }
    }

}