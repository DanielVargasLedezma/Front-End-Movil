package com.example.app_matricula_movil.ui.view.recyclerView.usuariosRecyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.app_matricula_movil.data.models.Usuario
import com.example.app_matricula_movil.databinding.UsuarioItemBinding

class UsuarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = UsuarioItemBinding.bind(view)

    fun render(usuario: Usuario, onClickListener: (Usuario) -> Unit) {

        binding.apply {
            nombreUsuario.text = usuario.nombre
            cedulaUsuario.text = usuario.cedula_usuario
            emailUsuario.text = usuario.correo
        }

        itemView.setOnClickListener {
            onClickListener(usuario)
        }
    }
}