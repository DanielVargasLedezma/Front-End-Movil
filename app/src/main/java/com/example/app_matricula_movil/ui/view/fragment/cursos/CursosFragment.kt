package com.example.app_matricula_movil.ui.view.fragment.cursos

import android.content.DialogInterface
import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.Usuario
import com.example.app_matricula_movil.data.models.carrera.CarreraCompleja
import com.example.app_matricula_movil.data.models.curso.CursoComplejo
import com.example.app_matricula_movil.data.repository.CursoRepository
import com.example.app_matricula_movil.databinding.FragmentCursosBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import com.example.app_matricula_movil.ui.view.fragment.usuarios.EditarUsuarioFragment
import com.example.app_matricula_movil.ui.view.recyclerView.cursosRecyclerView.CursoAdapter
import com.example.app_matricula_movil.ui.view.recyclerView.usuariosRecyclerView.UsuarioAdapter
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [CursosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CursosFragment : Fragment() {
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"

    private var carreraCompleja: CarreraCompleja? = null
    private var viendoVista: String? = null

    private var _binding: FragmentCursosBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CursoAdapter

    private var cursos: ArrayList<CursoComplejo> = arrayListOf()

    private val cursoRepository = CursoRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            carreraCompleja = it.getSerializable(ARG_PARAM1) as CarreraCompleja?
            viendoVista = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCursosBinding.inflate(inflater, container, false)

        if (carreraCompleja == null) {
            CoroutineScope(Dispatchers.IO).launch {
                val response = cursoRepository.getCursos((activity as NavdrawActivity).token!!)

                if (response != null) {
                    activity!!.runOnUiThread {
                        cursos.addAll(response.cursos)
                    }
                }

                activity!!.runOnUiThread {
                    initRecyclerView()
                    setSearchBar()
                    setRecyclerViewsItemsTouchHelper()
                }
            }
        } else {
            (activity as NavdrawActivity).supportActionBar?.title = "Cursos de ${carreraCompleja!!.codigo_carrera}"
            cursos.addAll(carreraCompleja!!.cursos)

            initRecyclerView()
            setSearchBar()

            if (viendoVista == null) setRecyclerViewsItemsTouchHelper()
            else binding.fab.visibility = View.GONE
        }

        binding.apply {
            fab.setOnClickListener {
                (activity as NavdrawActivity).supportActionBar?.title = "Registrar Curso"

                swapFragments(
                    CrearCursoFragment.newInstance(
                        carreraCompleja
                    )
                )
            }
        }

        return binding.root
    }

    private fun getCursos() {
        if (carreraCompleja == null) {
            CoroutineScope(Dispatchers.IO).launch {
                val response = cursoRepository.getCursos((activity as NavdrawActivity).token!!)

                if (response != null) {
                    activity!!.runOnUiThread {
                        cursos.clear()
                        cursos.addAll(response.cursos)

                        actualizarRecyclerView()
                    }
                } else {
                    activity!!.runOnUiThread {
                        cursos = arrayListOf()

                        actualizarRecyclerView()
                    }
                }
            }
        } else {
            cursos.clear()
            cursos.addAll(carreraCompleja!!.cursos)
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@CursosFragment.context)
            adapter = CursoAdapter(cursos) { onItemSelected(it) }
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

    private fun actualizarRecyclerView() {
        binding.apply {
            adapter = CursoAdapter(cursos) { onItemSelected(it) }

            recyclerView.adapter = adapter
        }
    }

    private fun setRecyclerViewsItemsTouchHelper() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition: Int = viewHolder.adapterPosition
                val toPosition: Int = target.adapterPosition

                Collections.swap(cursos, fromPosition, toPosition)

                binding.apply {
                    recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
                }

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                if (direction == ItemTouchHelper.RIGHT) {
                    binding.apply {
                        (activity as NavdrawActivity).supportActionBar?.title = "Editar Curso"

                        swapFragments(
                            EditarCursoFragment.newInstance(
                                cursos[position], carreraCompleja
                            )
                        )
                    }
                } else {
                    binding.apply {
                        AlertDialog.Builder(this@CursosFragment.context!!).apply {
                            setTitle("¿Está seguro de eliminar este usuario?")
                            setMessage("Esta acción removerá al usuario del sistema y es irreversible.")

                            setPositiveButton("Aceptar") { _: DialogInterface, _: Int ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    val response =
                                        cursoRepository.eliminarCurso(
                                            cursos[position].codigo_curso,
                                            (activity as NavdrawActivity).token!!
                                        )

                                    if (response) {
                                        activity!!.runOnUiThread {
                                            getCursos()

                                            cursos.removeAt(position)
                                            recyclerView.adapter?.notifyItemRemoved(position)

                                            actualizarRecyclerView()
                                        }
                                    } else {
                                        activity!!.runOnUiThread {
                                            actualizarRecyclerView()

                                            Toast.makeText(
                                                this@CursosFragment.context,
                                                "Error al eliminar",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    }
                                }
                            }

                            setNegativeButton("Cancelar") { _: DialogInterface, _: Int ->
                                actualizarRecyclerView()

                                Toast.makeText(
                                    this@CursosFragment.context,
                                    "Acción cancelada",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }.show()
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                RecyclerViewSwipeDecorator.Builder(
                    this@CursosFragment.context,
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeLeftBackgroundColor(
                        ContextCompat.getColor(
                            this@CursosFragment.context!!,
                            R.color.red
                        )
                    )
                    .addSwipeLeftActionIcon(R.drawable.delete_icon)
                    .addSwipeRightBackgroundColor(
                        ContextCompat.getColor(
                            this@CursosFragment.context!!,
                            R.color.green
                        )
                    )
                    .addSwipeRightActionIcon(R.drawable.edit)
                    .create()
                    .decorate()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)

        binding.apply {
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }
    }

    private fun onItemSelected(curso: CursoComplejo) {
        (activity as NavdrawActivity).supportActionBar?.title = "Visualizar Curso"

        swapFragments(
            CursoFragment.newInstance(
                curso, carreraCompleja, viendoVista
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
         * @param carreraElegida Parameter 1.
         * @param tipoVista Parameter 2.
         * @return A new instance of fragment CursosFragment.
         */
        @JvmStatic
        fun newInstance(carreraElegida: CarreraCompleja? = null, tipoVista: String? = null) =
            CursosFragment().apply {
                arguments = Bundle().apply {
                    if (carreraElegida != null) putSerializable(ARG_PARAM1, carreraElegida)
                    if (tipoVista != null) putString(ARG_PARAM2, tipoVista)
                }
            }
    }
}