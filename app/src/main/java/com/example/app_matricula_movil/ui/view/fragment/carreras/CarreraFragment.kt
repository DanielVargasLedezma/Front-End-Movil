package com.example.app_matricula_movil.ui.view.fragment.carreras

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.carrera.CarreraCompleja
import com.example.app_matricula_movil.databinding.FragmentCarreraBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import com.example.app_matricula_movil.ui.view.fragment.cursos.CursosFragment

/**
 * A simple [Fragment] subclass.
 * Use the [CarreraFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CarreraFragment : Fragment() {
    private val ARG_PARAM1 = "param1"

    private var carreraAVer: CarreraCompleja? = null

    private var _binding: FragmentCarreraBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            carreraAVer = it.getSerializable(ARG_PARAM1) as CarreraCompleja?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCarreraBinding.inflate(inflater, container, false)

        binding.apply {
            codigoCarrera.setText(carreraAVer!!.codigo_carrera)
            nombreCarrera.setText(carreraAVer!!.nombre)
            tituloCarrera.setText(carreraAVer!!.titulo)

            goBack.setOnClickListener {
                iniciarCarreras()
            }

            cursosCarrera.setOnClickListener {
                swapFragments(
                    CursosFragment.newInstance(carreraAVer)
                )
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
         * @param carreraAVer Carrera seleccionada a ver.
         * @return A new instance of fragment CarreraFragment.
         */
        @JvmStatic
        fun newInstance(carreraAVer: CarreraCompleja) =
            CarreraFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, carreraAVer)
                }
            }
    }
}