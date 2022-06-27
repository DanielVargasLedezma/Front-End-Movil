package com.example.app_matricula_movil.ui.view.fragment.profesores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.profesor.Profesor
import com.example.app_matricula_movil.data.repository.ProfesorRepository
import com.example.app_matricula_movil.databinding.FragmentEditarProfesorBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditarProfesorFragment : Fragment() {

    private val ARG_PARAM1 = "param1"

    private var profesorAeditar: Profesor? = null


    private var _binding: FragmentEditarProfesorBinding? = null
    private val binding get() = _binding!!

    private val profesorRepository = ProfesorRepository()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            profesorAeditar = it.getSerializable(ARG_PARAM1) as Profesor?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditarProfesorBinding.inflate(inflater, container, false)

        fillInputsByDefault()

        binding.apply {
            insertar.setOnClickListener {
                if (cedulaProfesor.text.isNotEmpty() && nombreProfesor.text.isNotEmpty()
                    && telProfesor.text.isNotEmpty() && emailProfesor.text.isNotEmpty()
                ) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val response = profesorRepository.editarProfesor(
                            Profesor(
                                cedulaProfesor.text.toString(),
                                "",
                                nombreProfesor.text.toString(),
                                telProfesor.text.toString().toInt(),
                                emailProfesor.text.toString()

                            ),
                            (activity as NavdrawActivity).token!!
                        )

                        if (response) {
                            activity!!.runOnUiThread {
                                iniciarProfesores()
                            }
                        } else {
                            activity!!.runOnUiThread {
                                Toast.makeText(
                                    this@EditarProfesorFragment.context,
                                    "Error al insertar",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        this@EditarProfesorFragment.context,
                        "Campos sin rellenar",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            descartar.setOnClickListener {
                fillInputsByDefault()
            }

            goBack.setOnClickListener {
                iniciarProfesores()
            }
        }

        return binding.root
    }

    private fun iniciarProfesores() {
        (activity as NavdrawActivity).supportActionBar?.title = "Profesores Registradas"

        swapFragments(
            ProfesoresFragment.newInstance()
        )
    }
    private fun swapFragments(fragment: Fragment) {
        val fragmentTransaction = parentFragmentManager.beginTransaction()

        fragmentTransaction.replace(
            R.id.contentMain, fragment
        )

        fragmentTransaction.commit()
    }
    private fun fillInputsByDefault() {
        binding.apply {
            cedulaProfesor.setText(profesorAeditar!!.cedula_profesor)
            nombreProfesor.setText(profesorAeditar!!.nombre)
            telProfesor.setText(profesorAeditar!!.telefono.toString())
            emailProfesor.setText(profesorAeditar!!.correo)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance(profesor: Profesor) =
            EditarProfesorFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, profesor)
                }
            }
    }
}

