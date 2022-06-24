package com.example.app_matricula_movil.ui.view.fragment.matricula

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.alumno.Alumno
import com.example.app_matricula_movil.data.models.alumno.AlumnoComplejo
import com.example.app_matricula_movil.data.models.grupo.GrupoComplejo
import com.example.app_matricula_movil.data.models.matricula.Matricula
import com.example.app_matricula_movil.data.repository.GrupoRepository
import com.example.app_matricula_movil.data.repository.MatriculaRepository
import com.example.app_matricula_movil.databinding.FragmentHacerMatriculaBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import com.example.app_matricula_movil.ui.view.fragment.grupos.GruposFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [HacerMatriculaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HacerMatriculaFragment : Fragment() {
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"

    private var alumnoElegido: AlumnoComplejo? = null
    private var tipoVista: String? = null

    private var _binding: FragmentHacerMatriculaBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterGrupo: ArrayAdapter<String>

    private val grupos: ArrayList<GrupoComplejo> = arrayListOf()

    private var matriculaAInsertar = Matricula()

    private val grupoRepository = GrupoRepository()
    private val matriculaRepository = MatriculaRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            alumnoElegido = it.getSerializable(ARG_PARAM1) as AlumnoComplejo?
            tipoVista = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHacerMatriculaBinding.inflate(inflater, container, false)

        CoroutineScope(Dispatchers.IO).launch {
            val response = grupoRepository.getGruposDeCarrera(
                alumnoElegido!!.carrera.codigo_carrera,
                (activity as NavdrawActivity).token!!
            )

            if (response != null) {
                activity!!.runOnUiThread {
                    grupos.addAll(response.grupos.filter {
                        it.ciclo.ciclo_activo == 1
                    })
                }
            }

            activity!!.runOnUiThread {
                setAdapterGrupo()
            }
        }

        binding.apply {
            insertar.setOnClickListener {
                if (matriculaAInsertar.numero_grupo.isNotEmpty()) {
                    matriculaAInsertar.cedula_alumno = alumnoElegido!!.cedula_alumno

                    CoroutineScope(Dispatchers.IO).launch {
                        val response = matriculaRepository.crearMatricula(
                            matriculaAInsertar,
                            (activity as NavdrawActivity).token!!
                        )

                        if (response) {
                            activity!!.runOnUiThread {
                                iniciarGrupos()
                            }
                        } else {
                            activity!!.runOnUiThread {
                                Toast.makeText(
                                    this@HacerMatriculaFragment.context,
                                    "Error al matricular",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        this@HacerMatriculaFragment.context,
                        "Campos sin elegir",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            goBack.setOnClickListener {
                iniciarGrupos()
            }
        }

        return binding.root
    }

    /*
     * Método encapsulado para no tener mucho desmadre en el código. Primero se inicializa el array adapter en cuestión
     * con el contexto y un tipo de layout de item; diría que es personalizable.
     */
    private fun setAdapterGrupo() {
        binding.apply {
            adapterGrupo = ArrayAdapter<String>(
                this@HacerMatriculaFragment.context!!,
                R.layout.spinner_list_group_first
            )

            adapterGrupo.setDropDownViewResource(R.layout.spinner_list_item)

            adapterGrupo.add("Seleccionar grupo")

            /*
             * En este caso, como llenamos con datos que están dentro de una entidad, recorremos el array que sacamos del
             * API con las carreras; esto para conseguir el nombre y el código de la carrera en este caso.
             */
            for (grupo in grupos) {
                adapterGrupo.add("${grupo.numero_grupo} - ${grupo.curso.codigo_curso} / ${grupo.curso.nombre}")
            }

            /*
             * Se le asigna el adapter que recién se llenó al spinner respectivo; esto hace que se muestren los datos con
             * los que se llenaron.
             */
            spinnerGrupos.adapter = adapterGrupo

            /*
             * Le ponemos un listener custom al spinner para cuando se seleccione un item, esto por medio de un objeto
             * que implementa la interfaz de listener para así implementar los métodos de una forma diferente para cada
             * spinner. En general solo se implementa el primer método, pero el segundo podría servir como validación
             * en caso de que no seleccione nada; en este caso le asignamos al curso a insertar en una posición menos a
             * la seleccionada por si se seleccionó el "seleccionar"
             */
            spinnerGrupos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    if (position > 0) matriculaAInsertar.numero_grupo = grupos[position - 1].numero_grupo
                    else matriculaAInsertar.numero_grupo = ""
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    private fun iniciarGrupos() {
        (activity as NavdrawActivity).supportActionBar?.title =
            "Grupos matriculados de ${alumnoElegido!!.cedula_alumno}"

        swapFragments(
            GruposFragment.newInstance(null, tipoVista, alumnoElegido)
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
         * @param alumno Alumno al que se le va a hacer una matrícula.
         * @param tipoVista Tipo de vista que se está viendo.
         * @return A new instance of fragment HacerMatriculaFragment.
         */
        @JvmStatic
        fun newInstance(alumno: AlumnoComplejo, tipoVista: String? = null) =
            HacerMatriculaFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, alumno)
                    if (tipoVista != null) putString(ARG_PARAM2, tipoVista)
                }
            }
    }
}