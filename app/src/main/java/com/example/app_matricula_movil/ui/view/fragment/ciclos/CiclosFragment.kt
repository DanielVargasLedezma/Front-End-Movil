package com.example.app_matricula_movil.ui.view.fragment.ciclos

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
import com.example.app_matricula_movil.data.models.carrera.CarreraCompleja
import com.example.app_matricula_movil.data.models.ciclo.Ciclo
import com.example.app_matricula_movil.data.repository.CicloRepository
import com.example.app_matricula_movil.databinding.FragmentCiclosBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import com.example.app_matricula_movil.ui.view.fragment.carreras.CarreraFragment
import com.example.app_matricula_movil.ui.view.fragment.carreras.CrearCarreraFragment
import com.example.app_matricula_movil.ui.view.fragment.carreras.EditarCarreraFragment
import com.example.app_matricula_movil.ui.view.recyclerView.carrerasRecyclerView.CarreraAdapter
import com.example.app_matricula_movil.ui.view.recyclerView.ciclosRecyclerView.CicloAdapter
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [CiclosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CiclosFragment : Fragment() {

    private var _binding: FragmentCiclosBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CicloAdapter

    private val ciclos: ArrayList<Ciclo> = arrayListOf()
    private val cicloRepository =  CicloRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCiclosBinding.inflate(inflater, container, false)

        CoroutineScope(Dispatchers.IO).launch {
            val response =
                cicloRepository.getCiclos((activity as NavdrawActivity).token!!)

            if (response != null){
                activity!!.runOnUiThread{
                    ciclos.addAll(response.ciclos)
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
                (activity as NavdrawActivity).supportActionBar?.title = "Insertar Ciclo"

                swapFragments(
                    CrearCicloFragment.newInstance()
                )
            }
        }

        return binding.root
    }

    private fun getCiclos() {
        CoroutineScope(Dispatchers.IO).launch {
            val response =
                cicloRepository.getCiclos((activity as NavdrawActivity).token!!)

            if (response != null) {
                activity!!.runOnUiThread {
                    ciclos.clear()
                    ciclos.addAll(response.ciclos)
                }
            }

            activity!!.runOnUiThread {
                actualizarRecyclerView()
            }
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@CiclosFragment.context)
            adapter = CicloAdapter(ciclos) { onItemSelected(it) }
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
            adapter = CicloAdapter(ciclos) { onItemSelected(it) }

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

                Collections.swap(ciclos, fromPosition, toPosition)

                binding.apply {
                    recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
                }

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                if (direction == ItemTouchHelper.RIGHT) {
                    binding.apply {
                        (activity as NavdrawActivity).supportActionBar?.title = "Editar Carrera"

                        swapFragments(
                            EdiarCicloFragment.newInstance(
                                adapter.itemsList[position]
                            )
                        )
                    }
                } else {
                    binding.apply {
                        AlertDialog.Builder(this@CiclosFragment.context!!).apply {
                            setTitle("¿Está seguro de eliminar este ciclo?")
                            setMessage("Esta acción removerá el ciclo del sistema y es irreversible.")

                            setPositiveButton("Aceptar") { _: DialogInterface, _: Int ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    val response =
                                        cicloRepository.eliminarCiclo(
                                            adapter.itemsList[position].id_ciclo,
                                            (activity as NavdrawActivity).token!!
                                        )

                                    if (response) {
                                        activity!!.runOnUiThread {
                                            getCiclos()

                                            ciclos.removeAt(position)
                                            recyclerView.adapter?.notifyItemRemoved(position)

                                            actualizarRecyclerView()
                                        }
                                    } else {
                                        activity!!.runOnUiThread {
                                            actualizarRecyclerView()
                                            Toast.makeText(
                                                this@CiclosFragment.context,
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
                                    this@CiclosFragment.context,
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
                            this@CiclosFragment.context!!,
                            R.color.red
                        )
                    )
                    .addSwipeLeftActionIcon(R.drawable.delete_icon)
                    .addSwipeRightBackgroundColor(
                        ContextCompat.getColor(
                            this@CiclosFragment.context!!,
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

    private fun onItemSelected(ciclo: Ciclo) {
        (activity as NavdrawActivity).supportActionBar?.title = "Visualizar Ciclo"

        swapFragments(
            CicloFragment.newInstance(
                ciclo
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
         * @return A new instance of fragment CiclosFragment.
         */
        @JvmStatic
        fun newInstance() =
            CiclosFragment().apply {
                arguments = Bundle().apply { }
            }
    }
}