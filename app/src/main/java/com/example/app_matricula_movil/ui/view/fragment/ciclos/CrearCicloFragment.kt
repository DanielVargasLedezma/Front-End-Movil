package com.example.app_matricula_movil.ui.view.fragment.ciclos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.Toast
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.ciclo.Ciclo
import com.example.app_matricula_movil.data.repository.CicloRepository
import com.example.app_matricula_movil.databinding.FragmentCrearCicloBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [CrearCicloFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CrearCicloFragment : Fragment() {

    private var _binding: FragmentCrearCicloBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterNumeroCiclo: ArrayAdapter<String>

    private val cicloAInsertar = Ciclo()

    private val cicloRepository = CicloRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrearCicloBinding.inflate(inflater, container, false)

        setAdapterNumeroCiclo()

        binding.apply {
            goBack.setOnClickListener {
                iniciarCiclos()
            }

            insertar.setOnClickListener {
                if (annoQueseLleva.text.isNotEmpty() && fechaInicio.text.isNotEmpty() && fechaFinalizacion.text.isNotEmpty() && cicloAInsertar.numero_ciclo != 0) {

                    cicloAInsertar.year = annoQueseLleva.text.toString().toInt()
                    cicloAInsertar.fecha_inicio = getGoodDate(fechaInicio.text.toString())
                    cicloAInsertar.fecha_finalizacion = getGoodDate(fechaFinalizacion.text.toString())
                    cicloAInsertar.ciclo_activo = getCicloActivoInt()

                    CoroutineScope(Dispatchers.IO).launch {
                        val response = cicloRepository.insertarCiclo(cicloAInsertar, (activity as NavdrawActivity).token!!)

                        if (response) {
                            activity!!.runOnUiThread {
                                iniciarCiclos()
                            }
                        } else {
                            activity!!.runOnUiThread {
                                Toast.makeText(
                                    this@CrearCicloFragment.context,
                                    "Error al insertar",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        this@CrearCicloFragment.context,
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

    private fun setAdapterNumeroCiclo() {
        binding.apply {
            adapterNumeroCiclo =
                ArrayAdapter<String>(this@CrearCicloFragment.context!!, R.layout.spinner_list_year_first)

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
                    cicloAInsertar.numero_ciclo = position
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
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
         * @return A new instance of fragment CrearCicloFragment.
         */
        @JvmStatic
        fun newInstance() =
            CrearCicloFragment().apply {
                arguments = Bundle().apply { }
            }
    }
}