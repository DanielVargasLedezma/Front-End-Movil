package com.example.app_matricula_movil.ui.view.fragment.grupos

import android.content.DialogInterface
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
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
import com.example.app_matricula_movil.data.models.curso.CursoComplejo
import com.example.app_matricula_movil.data.models.grupo.GrupoComplejo
import com.example.app_matricula_movil.data.repository.GrupoRepository
import com.example.app_matricula_movil.data.repository.MatriculaRepository
import com.example.app_matricula_movil.databinding.FragmentGruposBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import com.example.app_matricula_movil.ui.view.fragment.matricula.HacerMatriculaFragment
import com.example.app_matricula_movil.ui.view.recyclerView.gruposRecyclerView.GrupoAdapter
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [GruposFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GruposFragment : Fragment() {
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"
    private val ARG_PARAM3 = "param3"

    private var cursoElegido: CursoComplejo? = null
    private var viendoVista: String? = null
    private var alumnoElegido: Alumno? = null

    private var _binding: FragmentGruposBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: GrupoAdapter

    private var grupos: ArrayList<GrupoComplejo> = arrayListOf()

    private val grupoRepository = GrupoRepository()
    private val matriculaRepository = MatriculaRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cursoElegido = it.getSerializable(ARG_PARAM1) as CursoComplejo?
            viendoVista = it.getString(ARG_PARAM2)
            alumnoElegido = it.getSerializable(ARG_PARAM3) as Alumno?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGruposBinding.inflate(inflater, container, false)

        when (viendoVista) {
            "OfertaAcademica" -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val response =
                        grupoRepository.getGruposDeCurso(
                            cursoElegido!!.codigo_curso,
                            (activity as NavdrawActivity).token!!
                        )

                    if (response != null) {
                        activity!!.runOnUiThread {
                            grupos.clear()
                            grupos.addAll(response.grupos.filter {
                                it.ciclo.ciclo_activo == 1
                            })
                        }
                    }

                    activity!!.runOnUiThread {
                        initRecyclerView()
                        setSearchBar()
                        setRecyclerViewsItemsTouchHelper()
                    }
                }
            }
            "GruposAsignados" -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val response =
                        grupoRepository.getGruposDeProfesor(
                            (activity as NavdrawActivity).userLogged!!.cedula_usuario,
                            (activity as NavdrawActivity).token!!
                        )

                    if (response != null) {
                        activity!!.runOnUiThread {
                            grupos.clear()
                            grupos.addAll(response.grupos.filter {
                                it.ciclo.ciclo_activo == 1
                            })
                        }
                    }

                    activity!!.runOnUiThread {
                        initRecyclerView()
                        setSearchBar()
                    }
                }
            }
            "GruposMatriculadosAlumno" -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val response =
                        grupoRepository.getGruposMatriculadosDeAlumno(
                            alumnoElegido!!.cedula_alumno,
                            (activity as NavdrawActivity).token!!
                        )

                    if (response != null) {
                        activity!!.runOnUiThread {
                            grupos.clear()
                            grupos.addAll(response.grupos.filter {
                                it.ciclo.ciclo_activo == 1
                            })
                        }
                    }

                    activity!!.runOnUiThread {
                        initRecyclerView()
                        setSearchBar()
                        setRecyclerViewsItemsTouchHelperMatricula()
                    }
                }
            }
        }


        binding.apply {
            when (viendoVista) {
                "OfertaAcademica" -> {
                    fab.setOnClickListener {
                        (activity as NavdrawActivity).supportActionBar?.title = "Registrar Grupo"

                        swapFragments(
                            CrearGrupoFragment.newInstance(
                                cursoElegido!!, viendoVista!!
                            )
                        )
                    }
                }
                "GruposMatriculadosAlumno" -> {
                    fab.setOnClickListener {
                        (activity as NavdrawActivity).supportActionBar?.title =
                            "Matricular grupo a ${alumnoElegido!!.cedula_alumno}"

                        swapFragments(
                            HacerMatriculaFragment.newInstance(alumnoElegido!!, viendoVista)
                        )
                    }
                }
                else -> {
                    fab.visibility = View.GONE
                }
            }
        }

        return binding.root
    }

    private fun getGrupos() {
        when (viendoVista) {
            "OfertaAcademica" -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val response =
                        grupoRepository.getGruposDeCurso(
                            cursoElegido!!.codigo_curso,
                            (activity as NavdrawActivity).token!!
                        )

                    if (response != null) {
                        activity!!.runOnUiThread {
                            grupos.clear()
                            grupos.addAll(response.grupos.filter {
                                it.ciclo.ciclo_activo == 1
                            })

                            actualizarRecyclerView()
                        }
                    } else {
                        activity!!.runOnUiThread {
                            grupos.clear()
                            actualizarRecyclerView()
                        }
                    }
                }
            }
            "GruposMatriculadosAlumno" -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val response =
                        grupoRepository.getGruposMatriculadosDeAlumno(
                            alumnoElegido!!.cedula_alumno,
                            (activity as NavdrawActivity).token!!
                        )

                    if (response != null) {
                        activity!!.runOnUiThread {
                            grupos.clear()
                            grupos.addAll(response.grupos.filter {
                                it.ciclo.ciclo_activo == 1
                            })

                            actualizarRecyclerView()
                        }
                    } else {
                        activity!!.runOnUiThread {
                            grupos.clear()
                            actualizarRecyclerView()
                        }
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@GruposFragment.context)
            adapter = GrupoAdapter(grupos) { onItemSelected(it) }
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
            adapter = GrupoAdapter(grupos) { onItemSelected(it) }

            recyclerView.adapter = adapter
        }
    }

    private fun setRecyclerViewsItemsTouchHelperMatricula() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition: Int = viewHolder.adapterPosition
                val toPosition: Int = target.adapterPosition

                Collections.swap(grupos, fromPosition, toPosition)

                binding.apply {
                    recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
                }

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                if (direction == ItemTouchHelper.LEFT) {
                    binding.apply {
                        AlertDialog.Builder(this@GruposFragment.context!!).apply {
                            setTitle("¿Está seguro de desmatricular este grupo?")
                            setMessage("Esta acción removerá desmatriculará el grupo y es irreversible.")

                            setPositiveButton("Aceptar") { _: DialogInterface, _: Int ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    val response =
                                        matriculaRepository.desmatricularGrupo(
                                            alumnoElegido!!.cedula_alumno,
                                            grupos[position].numero_grupo,
                                            (activity as NavdrawActivity).token!!
                                        )

                                    if (response) {
                                        activity!!.runOnUiThread {
                                            getGrupos()

                                            grupos.removeAt(position)
                                            recyclerView.adapter?.notifyItemRemoved(position)

                                            actualizarRecyclerView()
                                        }
                                    } else {
                                        activity!!.runOnUiThread {
                                            actualizarRecyclerView()
                                            Toast.makeText(
                                                this@GruposFragment.context,
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
                                    this@GruposFragment.context,
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
                    this@GruposFragment.context,
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
                            this@GruposFragment.context!!,
                            R.color.red
                        )
                    )
                    .addSwipeLeftActionIcon(R.drawable.delete_icon)
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

                Collections.swap(grupos, fromPosition, toPosition)

                binding.apply {
                    recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
                }

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                if (direction == ItemTouchHelper.RIGHT) {
                    binding.apply {
                        (activity as NavdrawActivity).supportActionBar?.title = "Editar Grupo"

                        swapFragments(
                            EditarGrupoFragment.newInstance(
                                grupos[position], cursoElegido!!, viendoVista!!
                            )
                        )
                    }
                } else {
                    binding.apply {
                        AlertDialog.Builder(this@GruposFragment.context!!).apply {
                            setTitle("¿Está seguro de eliminar este grupo?")
                            setMessage("Esta acción removerá al grupo del sistema y es irreversible.")

                            setPositiveButton("Aceptar") { _: DialogInterface, _: Int ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    val response =
                                        grupoRepository.eliminarGrupo(
                                            grupos[position].numero_grupo,
                                            (activity as NavdrawActivity).token!!
                                        )

                                    if (response) {
                                        activity!!.runOnUiThread {
                                            getGrupos()

                                            grupos.removeAt(position)
                                            recyclerView.adapter?.notifyItemRemoved(position)

                                            actualizarRecyclerView()
                                        }
                                    } else {
                                        activity!!.runOnUiThread {
                                            actualizarRecyclerView()
                                            Toast.makeText(
                                                this@GruposFragment.context,
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
                                    this@GruposFragment.context,
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
                    this@GruposFragment.context,
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
                            this@GruposFragment.context!!,
                            R.color.red
                        )
                    )
                    .addSwipeLeftActionIcon(R.drawable.delete_icon)
                    .addSwipeRightBackgroundColor(
                        ContextCompat.getColor(
                            this@GruposFragment.context!!,
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

    private fun onItemSelected(grupo: GrupoComplejo) {
        if (alumnoElegido == null) (activity as NavdrawActivity).supportActionBar?.title = "Visualizar Grupo"
        else (activity as NavdrawActivity).supportActionBar?.title =
            "Matrícula Realizada de ${alumnoElegido!!.cedula_alumno}"

        swapFragments(
            GrupoFragment.newInstance(
                grupo, cursoElegido, viendoVista, alumnoElegido
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
         * @param cursoElegido Curso del cual se van a ver los grupos.
         * @param tipoVista Qué se está visualizando.
         * @param alumnoElegido Alumno del que se van a ver los grupos matriculados.
         * @return A new instance of fragment GruposFragment.
         */
        @JvmStatic
        fun newInstance(
            cursoElegido: CursoComplejo? = null,
            tipoVista: String? = null,
            alumnoElegido: Alumno? = null
        ) =
            GruposFragment().apply {
                arguments = Bundle().apply {
                    if (cursoElegido != null) putSerializable(ARG_PARAM1, cursoElegido)
                    if (tipoVista != null) putString(ARG_PARAM2, tipoVista)
                    if (alumnoElegido != null) putSerializable(ARG_PARAM3, alumnoElegido)
                }
            }
    }
}