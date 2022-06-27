package com.example.app_matricula_movil.ui.view.fragment.ciclos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.ciclo.Ciclo
import com.example.app_matricula_movil.data.repository.CicloRepository
import com.example.app_matricula_movil.databinding.FragmentCrearCicloBinding
import com.example.app_matricula_movil.databinding.FragmentEdiarCicloBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [EdiarCicloFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EdiarCicloFragment : Fragment() {
    private val ARG_PARAM1 = "param1"
    private var _binding: FragmentEdiarCicloBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterNumeroCiclo: ArrayAdapter<String>

    private val cicloAEditar = Ciclo()
    private val ciclo = Ciclo()

    private val cicloRepository = CicloRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
         }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEdiarCicloBinding.inflate(inflater, container, false)

        setAdapterNumeroCiclo()
        fillInputsByDefault()

        binding.apply {
            goBack.setOnClickListener {
                iniciarCiclos()
            }

            insertar.setOnClickListener {
                if (annoQueseLleva.text.isNotEmpty() && fechaInicio.text.isNotEmpty() && fechaFinalizacion.text.isNotEmpty() && cicloAEditar.numero_ciclo != 0) {
                    ciclo.id_ciclo = cicloAEditar.id_ciclo
                    ciclo.year = annoQueseLleva.text.toString().toInt()
                    ciclo.fecha_inicio = getGoodDate(fechaInicio.text.toString())
                    ciclo.fecha_finalizacion = getGoodDate(fechaFinalizacion.text.toString())
                    ciclo.ciclo_activo = getCicloActivoInt()

                    CoroutineScope(Dispatchers.IO).launch {
                        val response = cicloRepository.editarCiclo(
                            ciclo, (activity as NavdrawActivity).token!!)

                        if (response) {
                            activity!!.runOnUiThread {
                                iniciarCiclos()
                            }
                        } else {
                            activity!!.runOnUiThread {
                                Toast.makeText(
                                    this@EdiarCicloFragment.context,
                                    "Error al insertar",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        this@EdiarCicloFragment.context,
                        "Campos sin rellenar o sin elegir",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
        return binding.root

    }
    private fun getGoodDate(fecha: String): String {
        val values = fecha.substring(0, 10).split("-")

        return "${values[2]}-${values[1]}-${values[0]}"
    }

    private fun getCicloActivoInt(): Int {
        binding.apply {
            if (estadoUsuario.isChecked) return 1
            else return 0
        }
    }
    private fun iniciarCiclos() {
        (activity as NavdrawActivity).supportActionBar?.title = "Editar Ciclo"

        swapFragments(
            CiclosFragment.newInstance()
        )
    }
    private fun setAdapterNumeroCiclo() {
        binding.apply {
            adapterNumeroCiclo =
                ArrayAdapter<String>(this@EdiarCicloFragment.context!!, R.layout.spinner_list_year_first)

            adapterNumeroCiclo.setDropDownViewResource(R.layout.spinner_list_item)

            adapterNumeroCiclo.addAll(
                listOf(
                    "Seleccionar año",
                    "Primero",
                    "Segundo",
                )
            )

            numCiclo.adapter = adapterNumeroCiclo

            numCiclo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    ciclo.numero_ciclo = position
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    private fun fillInputsByDefault() {
        binding.apply {
            //numCiclo.set(cicloAEditar!!.numero_ciclo)
            annoQueseLleva.setText(cicloAEditar!!.year.toString())
            fechaInicio.setText(cicloAEditar!!.fecha_inicio)
            fechaFinalizacion.setText(cicloAEditar!!.fecha_finalizacion)

            numCiclo.setSelection(cicloAEditar!!.numero_ciclo)

        }
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
         * @return A new instance of fragment EdiarCicloFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(cicloAEditar: Ciclo) =
            EdiarCicloFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, cicloAEditar)
                }
            }
    }
}