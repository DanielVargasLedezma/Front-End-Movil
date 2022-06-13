package com.example.app_matricula_movil.ui.view.fragment.cursos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.curso.CursoComplejo
import com.example.app_matricula_movil.databinding.FragmentCursoBinding

/**
 * A simple [Fragment] subclass.
 * Use the [CursoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CursoFragment : Fragment() {
    private val ARG_PARAM1 = "param1"

    private var cursoAVer: CursoComplejo? = null

    private var _binding: FragmentCursoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cursoAVer = it.getSerializable(ARG_PARAM1) as CursoComplejo?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCursoBinding.inflate(inflater, container, false)

        binding.apply {
            codigoCurso.setText(cursoAVer!!.codigo_curso)
            nombreCurso.setText(cursoAVer!!.nombre)
            horasSemanales.setText("${cursoAVer!!.horas_semanales} h por semana")
            creditosCurso.setText("${cursoAVer!!.creditos} créditos")
            anyoACursar.setText(getStringYearMamalon(cursoAVer!!.anyo_a_llevar))
            cicloACursar.setText("${getStringCiclo()} ciclo")
            carrera.setText(cursoAVer!!.carrera.nombre)
        }

        return binding.root
    }

    private fun getStringCiclo(): String {
        if (cursoAVer!!.num_semestre_a_llevar == 1) return "Primer"
        return "Segundo"
    }

    private fun getStringYearMamalon(year: String): String {
        return if (year == "Primero") "Primer Año"
        else "$year Año"
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
        fun newInstance(param1: CursoComplejo) =
            CursoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                }
            }
    }
}