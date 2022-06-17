package com.example.app_matricula_movil.ui.view.fragment.usuarios

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.Usuario
import com.example.app_matricula_movil.databinding.FragmentUsuarioBinding

/**
 * A simple [Fragment] subclass.
 * Use the [UsuarioFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UsuarioFragment : Fragment() {
    private val ARG_PARAM1 = "param1"

    private var usuarioAVer: Usuario? = null

    private var _binding: FragmentUsuarioBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            usuarioAVer = it.getSerializable(ARG_PARAM1) as Usuario?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsuarioBinding.inflate(inflater, container, false)

        binding.apply {
            cedulaUsuario.setText(usuarioAVer!!.cedula_usuario)
            nombreUsuario.setText(usuarioAVer!!.nombre)
            correoUsuario.setText(usuarioAVer!!.correo)

            estadoUsuario.isChecked = usuarioAVer!!.estado == 1
            tipoUsuario.isChecked = usuarioAVer!!.tipo_usuario == 1
        }

        return binding.root
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
         * @return A new instance of fragment UsuarioFragment.
         */
        @JvmStatic
        fun newInstance(param1: Usuario) =
            UsuarioFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                }
            }
    }
}