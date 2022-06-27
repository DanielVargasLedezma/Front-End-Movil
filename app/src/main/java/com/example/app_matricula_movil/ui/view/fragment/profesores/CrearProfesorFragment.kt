package com.example.app_matricula_movil.ui.view.fragment.profesores

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.profesor.Profesor
import com.example.app_matricula_movil.data.repository.ProfesorRepository
import com.example.app_matricula_movil.databinding.FragmentCrearProfesorBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CrearProfesorFragment : Fragment() {
    private var _binding: FragmentCrearProfesorBinding? = null
    private val binding get() = _binding!!

    private val profesorRepository = ProfesorRepository()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCrearProfesorBinding.inflate(inflater, container, false)

        binding.apply {
            insertar.setOnClickListener {
                if (cedulaProfesor.text.isNotEmpty() && nombreProfesor.text.isNotEmpty()
                    && telProfesor.text.isNotEmpty() && emailProfesor.text.isNotEmpty()
                ) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val response = profesorRepository.registrarProfesor(
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
                                inciarProfesores()
                            }
                        } else {
                            activity!!.runOnUiThread {
                                Toast.makeText(
                                    this@CrearProfesorFragment.context,
                                    "Error al insertar",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        this@CrearProfesorFragment.context,
                        "Campos sin rellenar",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            goBack.setOnClickListener {
                inciarProfesores()
            }
        }

        return binding.root
    }
    private fun inciarProfesores() {
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
         * @return A new instance of fragment CrearProfesorFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            CrearProfesorFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}