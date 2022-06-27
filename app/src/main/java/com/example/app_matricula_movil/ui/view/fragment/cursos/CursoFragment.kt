package com.example.app_matricula_movil.ui.view.fragment.cursos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.Usuario
import com.example.app_matricula_movil.data.models.carrera.CarreraCompleja
import com.example.app_matricula_movil.data.models.curso.CursoComplejo
import com.example.app_matricula_movil.databinding.FragmentCursoBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import com.example.app_matricula_movil.ui.view.fragment.grupos.GruposFragment

/**
 * A simple [Fragment] subclass.
 * Use the [CursoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CursoFragment : Fragment() {
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"
    private val ARG_PARAM3 = "param3"

    private var cursoAVer: CursoComplejo? = null
    private var carreraCompleja: CarreraCompleja? = null
    private var viendoVista: String? = null

    private var _binding: FragmentCursoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cursoAVer = it.getSerializable(ARG_PARAM1) as CursoComplejo?
            carreraCompleja = it.getSerializable(ARG_PARAM2) as CarreraCompleja?
            viendoVista = it.getString(ARG_PARAM3)
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

            if (viendoVista != null && viendoVista!! == "OfertaAcademica") grupos.visibility = View.VISIBLE

            grupos.setOnClickListener {
                (activity as NavdrawActivity).supportActionBar?.title = "Grupos Programados"

                (activity as NavdrawActivity).supportActionBar?.subtitle =
                    "Curso ${cursoAVer!!.codigo_curso}"

                swapFragments(
                    GruposFragment.newInstance(
                        cursoAVer!!, viendoVista
                    )
                )
            }

            goBack.setOnClickListener {
                iniciarCursos()
            }
        }

        return binding.root
    }

    private fun iniciarCursos() {
        if (carreraCompleja == null) {
            (activity as NavdrawActivity).supportActionBar?.title = "Cursos Registrados"

            (activity as NavdrawActivity).supportActionBar?.subtitle = ""
        } else {
            (activity as NavdrawActivity).supportActionBar?.title = "Cursos de la Carrera"

            (activity as NavdrawActivity).supportActionBar?.subtitle =
                "Carrera ${carreraCompleja!!.codigo_carrera}"
        }

        swapFragments(
            CursosFragment.newInstance(carreraCompleja, viendoVista)
        )
    }

    private fun swapFragments(fragment: Fragment) {
        val fragmentTransaction = parentFragmentManager.beginTransaction()

        fragmentTransaction.replace(
            R.id.contentMain, fragment
        )

        fragmentTransaction.commit()
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
         * @param cursoAVer Curso a visualizar.
         * @param carreraElegida Carrera de la que se están viendo los cursos.
         * @param tipoVista Como se está viendo (Oferta Académica o normal).
         * @return A new instance of fragment CursoFragment.
         */
        @JvmStatic
        fun newInstance(
            cursoAVer: CursoComplejo, carreraElegida: CarreraCompleja? = null, tipoVista: String? = null
        ) =
            CursoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, cursoAVer)
                    if (carreraElegida != null) putSerializable(ARG_PARAM2, carreraElegida)
                    if (tipoVista != null) putString(ARG_PARAM3, tipoVista)
                }
            }
    }
}