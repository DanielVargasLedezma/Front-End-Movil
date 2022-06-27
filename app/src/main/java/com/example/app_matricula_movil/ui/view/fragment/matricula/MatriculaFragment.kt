package com.example.app_matricula_movil.ui.view.fragment.matricula

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.alumno.Alumno
import com.example.app_matricula_movil.data.models.alumno.AlumnoComplejo
import com.example.app_matricula_movil.data.models.grupo.GrupoComplejo
import com.example.app_matricula_movil.data.models.matricula.MatriculaCompleja
import com.example.app_matricula_movil.databinding.FragmentMatriculaBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity

/**
 * A simple [Fragment] subclass.
 * Use the [MatriculaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MatriculaFragment : Fragment() {
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"

    private var matriculaAVer: MatriculaCompleja? = null
    private var alumno: AlumnoComplejo? = null

    private var _binding: FragmentMatriculaBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            matriculaAVer = it.getSerializable(ARG_PARAM1) as MatriculaCompleja?
            alumno = it.getSerializable(ARG_PARAM2) as AlumnoComplejo?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMatriculaBinding.inflate(inflater, container, false)

        binding.apply {
            codigoGrupo.setText(matriculaAVer!!.grupo.numero_grupo)
            nombreProfesor.setText(matriculaAVer!!.grupo.profesor.nombre)
            ciclo.setText(getCicloText(matriculaAVer!!.grupo))
            horario.setText(getHorarioText(matriculaAVer!!.grupo))
            curso.setText(matriculaAVer!!.grupo.curso.nombre)
            nota.setText(getNota(matriculaAVer!!.nota))

            goBack.setOnClickListener {
                if (alumno == null) (activity as NavdrawActivity).supportActionBar?.title =
                    "Historial Académico"
                else (activity as NavdrawActivity).supportActionBar?.title =
                    "Historial Académico de ${alumno!!.cedula_alumno}"

                swapFragments(
                    MatriculasFragment.newInstance(
                        alumno
                    )
                )
            }
        }

        return binding.root
    }

    private fun getNota(nota: Int): String {
        return if (nota != 0) nota.toString()
        else "*Sin nota"
    }

    private fun getHorarioText(grupoComplejo: GrupoComplejo): String {
        return if (grupoComplejo.dia_dos != null && grupoComplejo.dia_dos != "" && grupoComplejo.dia_dos != "null") "${grupoComplejo.dia_uno} / ${grupoComplejo.dia_dos} - ${grupoComplejo.horario}"
        else "${grupoComplejo.dia_uno} - ${grupoComplejo.horario}"
    }

    private fun getCicloText(grupoComplejo: GrupoComplejo): String {
        var returnValue = "Ciclo "

        when (grupoComplejo.ciclo.numero_ciclo) {
            1 -> returnValue += "I de ${grupoComplejo.ciclo.year}"
            2 -> returnValue += "II de ${grupoComplejo.ciclo.year}"
        }

        return returnValue
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
         * @param matricula Matricular a ver.
         * @param alumno Alumno del que se están viendo las matrículas.
         * @return A new instance of fragment MatriculaFragment.
         */
        @JvmStatic
        fun newInstance(matricula: MatriculaCompleja, alumno: AlumnoComplejo? = null) = MatriculaFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_PARAM1, matricula)
                if (alumno != null) putSerializable(ARG_PARAM2, alumno)
            }
        }
    }
}