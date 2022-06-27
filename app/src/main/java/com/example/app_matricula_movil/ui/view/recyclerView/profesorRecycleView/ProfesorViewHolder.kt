package com.example.app_matricula_movil.ui.view.recyclerView.profesorRecycleView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.app_matricula_movil.data.models.carrera.CarreraCompleja
import com.example.app_matricula_movil.data.models.profesor.Profesor
import com.example.app_matricula_movil.databinding.CarreraItemBinding
import com.example.app_matricula_movil.databinding.ProfesorItemBinding

class ProfesorViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ProfesorItemBinding.bind(view)

    fun render(profesor: Profesor, onClickListener: (Profesor) -> Unit) {
        binding.apply {
            cedulaProfesor.text = profesor.cedula_profesor
            nombreProfesor.text = profesor.nombre
             emailProfesor.text = profesor.correo
        }

        itemView.setOnClickListener {
            onClickListener(profesor)
        }
    }
}