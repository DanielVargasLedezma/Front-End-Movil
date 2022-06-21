package com.example.app_matricula_movil.ui.view.fragment.matricula

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.alumno.Alumno
import com.example.app_matricula_movil.data.models.grupo.GrupoComplejo
import com.example.app_matricula_movil.data.models.matricula.MatriculaCompleja
import com.example.app_matricula_movil.data.repository.MatriculaRepository
import com.example.app_matricula_movil.databinding.FragmentMatriculasBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import com.example.app_matricula_movil.ui.view.fragment.grupos.GrupoFragment
import com.example.app_matricula_movil.ui.view.recyclerView.gruposRecyclerView.GrupoAdapter
import com.example.app_matricula_movil.ui.view.recyclerView.matriculaRecyclerView.MatriculaAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [MatriculasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MatriculasFragment : Fragment() {
    private val ARG_PARAM1 = "param1"

    private var alumno: Alumno? = null

    private var _binding: FragmentMatriculasBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MatriculaAdapter

    private val matriculas: ArrayList<MatriculaCompleja> = arrayListOf()

    private val matriculaRepository = MatriculaRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            alumno = it.getSerializable(ARG_PARAM1) as Alumno?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMatriculasBinding.inflate(inflater, container, false)

        val id_alumno = if (alumno != null) alumno!!.cedula_alumno
        else (activity as NavdrawActivity).userLogged!!.cedula_usuario

        CoroutineScope(Dispatchers.IO).launch {
            val response = matriculaRepository.getMatriculasAlumno(id_alumno, (activity as NavdrawActivity).token!!)

            if (response != null) {
                activity!!.runOnUiThread {
                    matriculas.clear()
                    matriculas.addAll(response.matriculas)
                }
            }

            activity!!.runOnUiThread {
                initRecyclerView()
                setSearchBar()
            }
        }

        return binding.root
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@MatriculasFragment.context)
            adapter = MatriculaAdapter(matriculas) { onItemSelected(it) }
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)
        }
    }

    private fun setSearchBar() {
        binding.apply {
            applicationSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    adapter.filter.filter(newText)
                    return false
                }
            })
        }
    }

    private fun onItemSelected(matricula: MatriculaCompleja) {
        (activity as NavdrawActivity).supportActionBar?.title = "Visualizar Historial Académico"

        swapFragments(
            MatriculaFragment.newInstance(
                matricula, alumno
            )
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
         * @param alumno Alumno a cargar matriculas.
         * @return A new instance of fragment MatriculasFragment.
         */
        @JvmStatic
        fun newInstance(alumno: Alumno? = null) =
            MatriculasFragment().apply {
                arguments = Bundle().apply {
                    if (alumno != null) putSerializable(ARG_PARAM1, alumno)
                }
            }
    }
}