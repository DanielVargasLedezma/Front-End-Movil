package com.example.app_matricula_movil.ui.view.fragment.ciclos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.ciclo.Ciclo
import com.example.app_matricula_movil.databinding.FragmentCicloBinding
import com.example.app_matricula_movil.databinding.FragmentCiclosBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import com.example.app_matricula_movil.ui.view.fragment.carreras.CarrerasFragment

/**
 * A simple [Fragment] subclass.
 * Use the [CicloFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CicloFragment : Fragment() {
    private val ARG_PARAM1 = "param1"
    private var cicloAver: Ciclo? = null

    private var _binding: FragmentCicloBinding? = null
    private val binding get() =  _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cicloAver = it.getSerializable(ARG_PARAM1) as Ciclo?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCicloBinding.inflate(inflater, container, false)

        binding.apply {
            numCiclo.setText(cicloAver!!.numero_ciclo.toString())
            annoQueseLleva.setText(cicloAver!!.year.toString())
            fechaInicio.setText(cicloAver!!.fecha_inicio)
            fechaFinalizacion.setText(cicloAver!!.fecha_finalizacion)

            estadoUsuario.isChecked = cicloAver!!.ciclo_activo == 1

            goBack.setOnClickListener {
                iniciarCiclos()
            }
        }
        return binding.root
    }
    private fun iniciarCiclos() {
        (activity as NavdrawActivity).supportActionBar?.title = "Ciclos Registrados"

        swapFragments(
            CiclosFragment.newInstance()
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
         * @return A new instance of fragment CicloFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(cicloAver: Ciclo) =
            CicloFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, cicloAver)

                }
            }
    }
}