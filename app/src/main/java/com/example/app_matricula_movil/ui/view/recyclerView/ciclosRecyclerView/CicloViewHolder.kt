package com.example.app_matricula_movil.ui.view.recyclerView.ciclosRecyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.app_matricula_movil.data.models.ciclo.Ciclo
import com.example.app_matricula_movil.databinding.CicloItemBinding

class CicloViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    private val binding = CicloItemBinding.bind(view)

    fun render(ciclo: Ciclo, onClickListener: (Ciclo) -> Unit){
        binding.apply {
            numCiclo.text = ciclo.numero_ciclo.toString()
            annoCiclo.text = ciclo.year.toString()
            estad.text = ciclo.ciclo_activo.toString()

        }
        itemView.setOnClickListener {
            onClickListener(ciclo)
        }
    }
}