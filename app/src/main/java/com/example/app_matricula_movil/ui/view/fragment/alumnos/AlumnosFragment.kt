package com.example.app_matricula_movil.ui.view.fragment.alumnos

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
import com.example.app_matricula_movil.data.models.alumno.Alumno
import com.example.app_matricula_movil.data.models.alumno.AlumnoComplejo
import com.example.app_matricula_movil.data.models.carrera.CarreraCompleja
import com.example.app_matricula_movil.data.models.grupo.GrupoComplejo
import com.example.app_matricula_movil.data.repository.AlumnoRepository
import com.example.app_matricula_movil.databinding.FragmentAlumnosBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import com.example.app_matricula_movil.ui.view.fragment.cursos.CrearCursoFragment
import com.example.app_matricula_movil.ui.view.fragment.matricula.EditarNotaFragment
import com.example.app_matricula_movil.ui.view.recyclerView.alumnosRecyclerView.AlumnoAdapter
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [AlumnosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlumnosFragment : Fragment() {
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"
    private val ARG_PARAM3 = "param3"
    private val ARG_PARAM4 = "param4"

    private var token: String? = null
    private var usuarioLoggeado: Usuario? = null
    private var tipoVista: String? = null
    private var grupoComplejo: GrupoComplejo? = null

    private var _binding: FragmentAlumnosBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AlumnoAdapter

    private var alumnos: ArrayList<AlumnoComplejo> = arrayListOf()

    private val alumnoRepository = AlumnoRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            token = it.getString(ARG_PARAM1)
            usuarioLoggeado = it.getSerializable(ARG_PARAM2) as Usuario?
            tipoVista = it.getString(ARG_PARAM3)
            grupoComplejo = it.getSerializable(ARG_PARAM4) as GrupoComplejo?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlumnosBinding.inflate(inflater, container, false)

        if (tipoVista == null) tipoVista = ""

        when (tipoVista) {
            "GruposAsignados" -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val response = alumnoRepository.getAlumnosMatriculados(grupoComplejo!!.numero_grupo, token!!)

                    if (response != null) {
                        activity!!.runOnUiThread {
                            alumnos.addAll(response.alumnos)
                        }
                    }

                    activity!!.runOnUiThread {
                        initRecyclerView()
                        setSearchBar()
                        setRecyclerViewsItemsTouchHelperMatricula()
                        binding.fab.visibility = View.GONE
                    }

                }
            }
            else -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val response = alumnoRepository.getAlumnos(token!!)

                    if (response != null) {
                        activity!!.runOnUiThread {
                            alumnos.addAll(response.alumnos)
                        }
                    }

                    activity!!.runOnUiThread {
                        initRecyclerView()
                        setSearchBar()
                        if (tipoVista == null || tipoVista == "") setRecyclerViewsItemsTouchHelper()
                        else binding.fab.visibility = View.GONE
                    }

                }
            }
        }

        binding.apply {
            fab.setOnClickListener {
                (activity as NavdrawActivity).supportActionBar?.title = "Registrar Alumno"
                swapFragments(
                    CrearAlumnoFragment.newInstance(
                        token!!, usuarioLoggeado!!
                    )
                )
            }
        }

        return binding.root
    }

    private fun getAlumnos() {
        when (tipoVista) {
            "GruposAsignados" -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val response = alumnoRepository.getAlumnosMatriculados(grupoComplejo!!.numero_grupo, token!!)

                    if (response != null) {
                        activity!!.runOnUiThread {
                            alumnos.clear()
                            alumnos.addAll(response.alumnos)

                            actualizarRecyclerView()
                        }
                    } else {
                        activity!!.runOnUiThread {
                            alumnos = arrayListOf()

                            actualizarRecyclerView()
                        }
                    }
                }
            }
            else -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val response = alumnoRepository.getAlumnos(token!!)

                    if (response != null) {
                        activity!!.runOnUiThread {
                            alumnos.clear()
                            alumnos.addAll(response.alumnos)

                            actualizarRecyclerView()
                        }
                    } else {
                        activity!!.runOnUiThread {
                            alumnos = arrayListOf()

                            actualizarRecyclerView()
                        }
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@AlumnosFragment.context)
            adapter = AlumnoAdapter(alumnos) { onItemSelected(it) }
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
            adapter = AlumnoAdapter(alumnos) { onItemSelected(it) }

            recyclerView.adapter = adapter
        }
    }

    private fun setRecyclerViewsItemsTouchHelperMatricula() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
            ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition: Int = viewHolder.adapterPosition
                val toPosition: Int = target.adapterPosition

                Collections.swap(alumnos, fromPosition, toPosition)

                binding.apply {
                    recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
                }

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                if (direction == ItemTouchHelper.RIGHT) {
                    binding.apply {
                        (activity as NavdrawActivity).supportActionBar?.title = "Editar/Registrar Nota"
                        (activity as NavdrawActivity).supportActionBar?.subtitle =
                            "Alumno ${alumnos[position].cedula_alumno}"

                        swapFragments(
                            EditarNotaFragment.newInstance(
                                adapter.itemsList[position], grupoComplejo!!, tipoVista
                            )
                        )
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
                    this@AlumnosFragment.context,
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeRightBackgroundColor(
                        ContextCompat.getColor(
                            this@AlumnosFragment.context!!,
                            R.color.disabled_color
                        )
                    )
                    .addSwipeRightActionIcon(R.drawable.nota)
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

                Collections.swap(alumnos, fromPosition, toPosition)

                binding.apply {
                    recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
                }

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                if (direction == ItemTouchHelper.RIGHT) {
                    binding.apply {
                        (activity as NavdrawActivity).supportActionBar?.title = "Editar Alumno"

                        swapFragments(
                            EditarAlumnoFragment.newInstance(
                                adapter.itemsList[position], token!!, usuarioLoggeado!!
                            )
                        )
                    }
                } else {
                    binding.apply {
                        AlertDialog.Builder(this@AlumnosFragment.context!!).apply {
                            setTitle("¿Está seguro de eliminar este alumno?")
                            setMessage("Esta acción removerá al alumno del sistema y es irreversible.")

                            setPositiveButton("Aceptar") { _: DialogInterface, _: Int ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    val response =
                                        alumnoRepository.eliminarAlumno(
                                            adapter.itemsList[position].cedula_alumno,
                                            token!!
                                        )

                                    if (response) {
                                        activity!!.runOnUiThread {
                                            getAlumnos()

                                            alumnos.removeAt(position)
                                            recyclerView.adapter?.notifyItemRemoved(position)

                                            actualizarRecyclerView()
                                        }
                                    } else {
                                        activity!!.runOnUiThread {
                                            actualizarRecyclerView()

                                            Toast.makeText(
                                                this@AlumnosFragment.context,
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
                                    this@AlumnosFragment.context,
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
                            this@AlumnosFragment.context!!,
                            R.color.red
                        )
                    )
                    .addSwipeLeftActionIcon(R.drawable.delete_icon)
                    .addSwipeRightBackgroundColor(
                        ContextCompat.getColor(
                            this@AlumnosFragment.context!!,
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

    private fun onItemSelected(alumno: AlumnoComplejo) {
        when (tipoVista) {
            "GruposAsignados" -> {
                (activity as NavdrawActivity).supportActionBar?.title = "Visualizar Alumno"
                (activity as NavdrawActivity).supportActionBar?.subtitle =
                    "Alumno del Grupo ${grupoComplejo!!.numero_grupo}"
            }
            else -> {
                (activity as NavdrawActivity).supportActionBar?.title = "Visualizar Alumno"
            }
        }

        swapFragments(
            AlumnoFragment.newInstance(
                alumno, tipoVista, grupoComplejo
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
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @param tipoVista Para qué se está usando el fragment.
         * @param grupoComplejo Grupo del que se cargarán los alumnos.
         * @return A new instance of fragment AlumnosFragment.
         */
        @JvmStatic
        fun newInstance(
            param1: String,
            param2: Usuario,
            tipoVista: String? = null,
            grupoComplejo: GrupoComplejo? = null
        ) =
            AlumnosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putSerializable(ARG_PARAM2, param2)
                    if (tipoVista != null) putString(ARG_PARAM3, tipoVista)
                    if (grupoComplejo != null) putSerializable(ARG_PARAM4, grupoComplejo)
                }
            }
    }
}