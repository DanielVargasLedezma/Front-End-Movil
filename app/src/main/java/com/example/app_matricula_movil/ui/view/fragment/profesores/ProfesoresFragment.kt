package com.example.app_matricula_movil.ui.view.fragment.profesores

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
import com.example.app_matricula_movil.data.models.profesor.Profesor
import com.example.app_matricula_movil.data.repository.ProfesorRepository
import com.example.app_matricula_movil.databinding.FragmentCarrerasBinding
import com.example.app_matricula_movil.databinding.FragmentProfesorBinding
import com.example.app_matricula_movil.databinding.FragmentProfesoresBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import com.example.app_matricula_movil.ui.view.fragment.carreras.CrearCarreraFragment
import com.example.app_matricula_movil.ui.view.recyclerView.profesorRecycleView.ProfesorAdapter
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ProfesoresFragment : Fragment() {
    private var _binding: FragmentProfesoresBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ProfesorAdapter

    private val profesores: ArrayList<Profesor> = arrayListOf()

    private val profesorRepository = ProfesorRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfesoresBinding.inflate(inflater, container, false)
        CoroutineScope(Dispatchers.IO).launch {
            val response =
                profesorRepository.getProfesores((activity as NavdrawActivity).token!!)

            if (response != null) {
                activity!!.runOnUiThread {
                    profesores.addAll(response.profesores)
                }
            }

            activity!!.runOnUiThread {
                initRecyclerView()
                setSearchBar()
                setRecyclerViewsItemsTouchHelper()
            }
        }

        binding.apply {
            fab.setOnClickListener {
                (activity as NavdrawActivity).supportActionBar?.title = "Registrar Profesor"

                swapFragments(
                    CrearProfesorFragment.newInstance()
                )
            }
        }

        return binding.root
    }

    private fun getProfesores() {
        CoroutineScope(Dispatchers.IO).launch {
            val response =
                profesorRepository.getProfesores((activity as NavdrawActivity).token!!)

            if (response != null) {
                activity!!.runOnUiThread {
                    profesores.clear()
                    profesores.addAll(response.profesores)
                }
            }

            activity!!.runOnUiThread {
                actualizarRecyclerView()
            }
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@ProfesoresFragment.context)
            adapter = ProfesorAdapter(profesores) { onItemSelected(it) }
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
            adapter = ProfesorAdapter(profesores) { onItemSelected(it) }

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

                Collections.swap(profesores, fromPosition, toPosition)

                binding.apply {
                    recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
                }

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                if (direction == ItemTouchHelper.RIGHT) {
                    binding.apply {
                        (activity as NavdrawActivity).supportActionBar?.title = "Editar Profesor"

                        swapFragments(
                            EditarProfesorFragment.newInstance(
                                adapter.itemsList[position]
                            )
                        )
                    }
                } else {
                    binding.apply {
                        AlertDialog.Builder(this@ProfesoresFragment.context!!).apply {
                            setTitle("¿Está seguro de eliminar este profesor?")
                            setMessage("Esta acción removerá el profesor del sistema y es irreversible.")

                            setPositiveButton("Aceptar") { _: DialogInterface, _: Int ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    val response =
                                        profesorRepository.eliminarProfesor(
                                            adapter.itemsList[position].cedula_profesor,
                                            (activity as NavdrawActivity).token!!
                                        )

                                    if (response) {
                                        activity!!.runOnUiThread {
                                            getProfesores()

                                            profesores.removeAt(position)
                                            recyclerView.adapter?.notifyItemRemoved(position)

                                            actualizarRecyclerView()
                                        }
                                    } else {
                                        activity!!.runOnUiThread {
                                            actualizarRecyclerView()
                                            Toast.makeText(
                                                this@ProfesoresFragment.context,
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
                                    this@ProfesoresFragment.context,
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
                    this@ProfesoresFragment.context,
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
                            this@ProfesoresFragment.context!!,
                            R.color.red
                        )
                    )
                    .addSwipeLeftActionIcon(R.drawable.delete_icon)
                    .addSwipeRightBackgroundColor(
                        ContextCompat.getColor(
                            this@ProfesoresFragment.context!!,
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

    private fun onItemSelected(profesor: Profesor) {
        (activity as NavdrawActivity).supportActionBar?.title = "Visualizar Profesor"

        swapFragments(
            ProfesorFragment.newInstance(
                profesor
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
         * @return A new instance of fragment ProfesoresFragment.
         */
        @JvmStatic
        fun newInstance() =
            ProfesoresFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}