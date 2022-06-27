package com.example.app_matricula_movil.ui.view.fragment.grupos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.alumno.AlumnoComplejo
import com.example.app_matricula_movil.data.models.curso.CursoComplejo
import com.example.app_matricula_movil.data.models.grupo.GrupoComplejo
import com.example.app_matricula_movil.databinding.FragmentGrupoBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import com.example.app_matricula_movil.ui.view.fragment.alumnos.AlumnosFragment

/**
 * A simple [Fragment] subclass.
 * Use the [GrupoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GrupoFragment : Fragment() {
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"
    private val ARG_PARAM3 = "param3"
    private val ARG_PARAM4 = "param4"

    private var grupoAVer: GrupoComplejo? = null
    private var cursoElegido: CursoComplejo? = null
    private var viendoVista: String? = null
    private var alumnoElegido: AlumnoComplejo? = null

    private var _binding: FragmentGrupoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            grupoAVer = it.getSerializable(ARG_PARAM1) as GrupoComplejo?
            cursoElegido = it.getSerializable(ARG_PARAM2) as CursoComplejo?
            viendoVista = it.getString(ARG_PARAM3)
            alumnoElegido = it.getSerializable(ARG_PARAM4) as AlumnoComplejo?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGrupoBinding.inflate(inflater, container, false)

        binding.apply {
            if (viendoVista == "GruposAsignados") alumnos.visibility = View.VISIBLE

            alumnos.setOnClickListener {
                (activity as NavdrawActivity).supportActionBar?.title =
                    "Alumnos del Grupo"
                (activity as NavdrawActivity).supportActionBar?.subtitle =
                    "Grupo ${grupoAVer!!.numero_grupo}"

                swapFragments(
                    AlumnosFragment.newInstance(
                        (activity as NavdrawActivity).token!!,
                        (activity as NavdrawActivity).userLogged!!,
                        viendoVista,
                        grupoAVer
                    )
                )
            }

            goBack.setOnClickListener {
                when (viendoVista) {
                    "OfertaAcademica" -> {
                        (activity as NavdrawActivity).supportActionBar?.title =
                            "Grupos Programados"
                        (activity as NavdrawActivity).supportActionBar?.subtitle =
                            "Curso ${cursoElegido!!.codigo_curso}"
                        swapFragments(
                            GruposFragment.newInstance(
                                cursoElegido!!, viendoVista!!
                            )
                        )
                    }
                    "GruposAsignados" -> {
                        (activity as NavdrawActivity).supportActionBar?.title =
                            "Grupos Asignados"
                        swapFragments(
                            GruposFragment.newInstance(
                                null, viendoVista!!
                            )
                        )
                    }
                    "GruposMatriculadosAlumno" -> {
                        (activity as NavdrawActivity).supportActionBar?.title =
                            "Grupos Matriculados"

                        (activity as NavdrawActivity).supportActionBar?.subtitle =
                            "Alumno ${alumnoElegido!!.cedula_alumno}"

                        swapFragments(
                            GruposFragment.newInstance(null, viendoVista, alumnoElegido)
                        )
                    }
                }
            }

            numeroGrupo.setText(grupoAVer!!.numero_grupo)
            nombreProfesor.setText(grupoAVer!!.profesor.nombre)
            ciclo.setText(getCicloText(grupoAVer!!))
            horarioGrupo.setText(getHorarioText(grupoAVer!!))
            carrera.setText("${grupoAVer!!.curso.carrera.codigo_carrera} ${grupoAVer!!.curso.carrera.nombre}")
        }

        return binding.root
    }

    private fun getHorarioText(grupoComplejo: GrupoComplejo): String {
        return if (grupoComplejo.dia_dos != null && grupoComplejo.dia_dos != "" && grupoComplejo.dia_dos != "null")
            "${grupoComplejo.dia_uno} / ${grupoComplejo.dia_dos} - ${grupoComplejo.horario}"
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
         * @param grupoAVer Grupo a visualizar.
         * @param cursoElegido Curso del que se pueden estar viendo los grupos.
         * @param tipoVista Si se está en oferta académica, grupos aisgnados o matrícula.
         * @param alumnoElegido Alumno al que se le está matriculando.
         * @return A new instance of fragment GrupoFragment.
         */
        @JvmStatic
        fun newInstance(
            grupoAVer: GrupoComplejo,
            cursoElegido: CursoComplejo? = null,
            tipoVista: String? = null,
            alumnoElegido: AlumnoComplejo? = null
        ) =
            GrupoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, grupoAVer)
                    if (cursoElegido != null) putSerializable(ARG_PARAM2, cursoElegido)
                    if (tipoVista != null) putString(ARG_PARAM3, tipoVista)
                    if (alumnoElegido != null) putSerializable(ARG_PARAM4, alumnoElegido)
                }
            }
    }
}