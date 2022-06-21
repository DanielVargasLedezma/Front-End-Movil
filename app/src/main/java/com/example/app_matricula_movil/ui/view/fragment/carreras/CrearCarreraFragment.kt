package com.example.app_matricula_movil.ui.view.fragment.carreras

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.carrera.Carrera
import com.example.app_matricula_movil.data.repository.CarreraRepository
import com.example.app_matricula_movil.databinding.FragmentCrearCarreraBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [CrearCarreraFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CrearCarreraFragment : Fragment() {

    private var _binding: FragmentCrearCarreraBinding? = null
    private val binding get() = _binding!!

    private val carreraRepository = CarreraRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrearCarreraBinding.inflate(inflater, container, false)

        binding.apply {
            insertar.setOnClickListener {
                if (codigoCarrera.text.isNotEmpty() && nombreCarrera.text.isNotEmpty()
                    && tituloCarrera.text.isNotEmpty()
                ) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val response = carreraRepository.insertarCarrera(
                            Carrera(
                                codigoCarrera.text.toString(),
                                nombreCarrera.text.toString(),
                                tituloCarrera.text.toString()
                            ),
                            (activity as NavdrawActivity).token!!
                        )

                        if (response) {
                            activity!!.runOnUiThread {
                                iniciarCarreras()
                            }
                        } else {
                            activity!!.runOnUiThread {
                                Toast.makeText(
                                    this@CrearCarreraFragment.context,
                                    "Error al insertar",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        this@CrearCarreraFragment.context,
                        "Campos sin rellenar",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            goBack.setOnClickListener {
                iniciarCarreras()
            }
        }

        return binding.root
    }

    private fun iniciarCarreras() {
        (activity as NavdrawActivity).supportActionBar?.title = "Carreras Registradas"

        swapFragments(
            CarrerasFragment.newInstance()
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
         * @return A new instance of fragment CrearCarreraFragment.
         */
        @JvmStatic
        fun newInstance() =
            CrearCarreraFragment().apply {
                arguments = Bundle().apply { }
            }
    }
}