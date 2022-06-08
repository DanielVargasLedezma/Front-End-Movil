package com.example.app_matricula_movil.ui.view.fragment.usuarios

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.Usuario
import com.example.app_matricula_movil.data.repository.UsuarioRepository
import com.example.app_matricula_movil.databinding.FragmentCrearUsuarioBinding
import com.example.app_matricula_movil.ui.view.NavdrawActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [CrearUsuarioFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CrearUsuarioFragment : Fragment() {
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"

    private var usuarioLoggeado: Usuario? = null
    private var token: String? = null

    private var _binding: FragmentCrearUsuarioBinding? = null
    private val binding get() = _binding!!

    private val repository = UsuarioRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            usuarioLoggeado = it.getSerializable(ARG_PARAM1) as Usuario?
            token = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrearUsuarioBinding.inflate(inflater, container, false)

        binding.apply {
            tipoUsuarioAdmin.setOnClickListener {
                tipoUsuarioMatriculador.isChecked = !tipoUsuarioAdmin.isChecked
            }

            tipoUsuarioMatriculador.setOnClickListener {
                tipoUsuarioAdmin.isChecked = !tipoUsuarioMatriculador.isChecked
            }

            insertarUsuario.setOnClickListener {
                if (
                    cedulaUsuario.text.isNotEmpty() && correoUsuario.text.isNotEmpty() && nombreUsuario.text.isNotEmpty()
                    && (tipoUsuarioAdmin.isChecked || tipoUsuarioMatriculador.isChecked)
                ) {
                    AlertDialog.Builder(this@CrearUsuarioFragment.context!!).apply {
                        setTitle("¿Está seguro de insertar este usuario?")
                        setMessage("Esta acción insertar al usuario en el sistema.")

                        setPositiveButton("Insertar") { _: DialogInterface, _: Int ->
                            CoroutineScope(Dispatchers.IO).launch {
                                val response = repository.insertarUsuario(
                                    Usuario(
                                        cedulaUsuario.text.toString(),
                                        "",
                                        getUserTypeChosen(),
                                        nombreUsuario.text.toString(),
                                        getStateChosen(),
                                        correoUsuario.text.toString()
                                    ), token!!
                                )

                                if (response) {
                                    activity!!.runOnUiThread {
                                        iniciarUsuarios()
                                    }
                                } else {
                                    activity!!.runOnUiThread {
                                        Toast.makeText(
                                            this@CrearUsuarioFragment.context,
                                            "Error al insertar",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                            }
                        }

                        setNegativeButton("Cancelar", null)
                    }.show()

                } else {
                    Toast.makeText(
                        this@CrearUsuarioFragment.context,
                        "Campos sin rellenar",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        return binding.root
    }

    private fun iniciarUsuarios() {
        (activity as NavdrawActivity).supportActionBar?.title = "Usuarios Registrados"

        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.replace(
            R.id.contentMain, UsuariosFragment.newInstance(
                token!!, usuarioLoggeado!!
            )
        )
        fragmentTransaction.commit()
    }

    private fun getUserTypeChosen(): Int {
        if (binding.tipoUsuarioAdmin.isChecked) return 1
        return 2
    }

    private fun getStateChosen(): Int {
        if (binding.estadoUsuario.isChecked) return 1
        return 0
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
         * @return A new instance of fragment CrearUsuarioFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Usuario, param2: String) =
            CrearUsuarioFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}