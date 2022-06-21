package com.example.app_matricula_movil.ui.view.recyclerView.carrerasRecyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.app_matricula_movil.data.models.carrera.CarreraCompleja
import com.example.app_matricula_movil.databinding.CarreraItemBinding

class CarreraViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = CarreraItemBinding.bind(view)

    fun render(carrera: CarreraCompleja, onClickListener: (CarreraCompleja) -> Unit) {
        binding.apply {
            codigoCarrera.text = carrera.codigo_carrera
            nombreCarrera.text = carrera.nombre
            tituloCarrera.text = carrera.titulo
        }

        itemView.setOnClickListener {
            onClickListener(carrera)
        }
    }
}