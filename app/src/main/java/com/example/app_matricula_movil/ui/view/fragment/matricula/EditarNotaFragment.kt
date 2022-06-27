package com.example.app_matricula_movil.ui.view.fragment.matricula

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.alumno.AlumnoComplejo
import com.example.app_matricula_movil.data.models.grupo.GrupoComplejo
import com.example.app_matricula_movil.data.models.matricula.Matricula
import com.example.app_matricula_movil.data.repository.MatriculaRepository
import com.example.app_matricula_movil.databinding.FragmentEditarNotaBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import com.example.app_matricula_movil.ui.view.fragment.alumnos.AlumnosFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [EditarNotaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditarNotaFragment : Fragment() {
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"
    private val ARG_PARAM3 = "param3"

    private var alumnoAPuntuar: AlumnoComplejo? = null
    private var grupoMatriculado: GrupoComplejo? = null
    private var tipoVista: String? = null

    private var _binding: FragmentEditarNotaBinding? = null
    private val binding get() = _binding!!

    private val matriculaRepository = MatriculaRepository()

    private var matriculaAEditar = Matricula()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            alumnoAPuntuar = it.getSerializable(ARG_PARAM1) as AlumnoComplejo?
            grupoMatriculado = it.getSerializable(ARG_PARAM2) as GrupoComplejo?
            tipoVista = it.getString(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditarNotaBinding.inflate(inflater, container, false)

        CoroutineScope(Dispatchers.IO).launch {
            val response = matriculaRepository.getMatriculaAlumno(
                alumnoAPuntuar!!.cedula_alumno,
                grupoMatriculado!!.numero_grupo,
                (activity as NavdrawActivity).token!!
            )

            activity!!.runOnUiThread {
                if (response != null) {
                    matriculaAEditar.numero_matricula = response.matricula.numero_matricula
                    matriculaAEditar.nota = response.matricula.nota ?: 0
                }

                matriculaAEditar.cedula_alumno = alumnoAPuntuar!!.cedula_alumno
                matriculaAEditar.numero_grupo = grupoMatriculado!!.numero_grupo

                setDefaultInput()
            }
        }

        binding.apply {
            goBack.setOnClickListener {
                iniciarAlumnos()
            }

            descartar.setOnClickListener {
                setDefaultInput()
            }

            insertar.setOnClickListener {
                if (nota.text.isNotEmpty()) {
                    if (nota.text.toString().toInt() in 0..100) {
                        matriculaAEditar.nota = nota.text.toString().toInt()

                        CoroutineScope(Dispatchers.IO).launch {
                            val response = matriculaRepository.registrarNota(
                                matriculaAEditar, (activity as NavdrawActivity).token!!
                            )

                            if (response) {
                                activity!!.runOnUiThread {
                                    iniciarAlumnos()
                                }
                            } else {
                                activity!!.runOnUiThread {
                                    Toast.makeText(
                                        this@EditarNotaFragment.context,
                                        "Error al editar",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@EditarNotaFragment.context,
                            "Valor de nota inválido",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                } else {
                    Toast.makeText(
                        this@EditarNotaFragment.context,
                        "Campos sin rellenar",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        return binding.root
    }

    private fun iniciarAlumnos() {
        (activity as NavdrawActivity).supportActionBar?.title =
            "Alumnos Registrados"

        (activity as NavdrawActivity).supportActionBar?.subtitle = ""

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

    private fun setDefaultInput() {
        binding.apply {
            nota.setText(matriculaAEditar.nota.toString())
        }
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
         * @param alumnoComplejo Alumno al que se le va a puntuar la nota.
         * @param grupoComplejo Grupo donde se va a puntuar.
         * @return A new instance of fragment EditarNotaFragment.
         */
        @JvmStatic
        fun newInstance(alumnoComplejo: AlumnoComplejo, grupoComplejo: GrupoComplejo, tipoVista: String? = null) =
            EditarNotaFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, alumnoComplejo)
                    putSerializable(ARG_PARAM2, grupoComplejo)
                    putString(ARG_PARAM3, tipoVista)
                }
            }
    }
}