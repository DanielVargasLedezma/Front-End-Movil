package com.example.app_matricula_movil.ui.view.recyclerView.ciclosRecyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.app_matricula_movil.data.models.ciclo.Ciclo
import com.example.app_matricula_movil.databinding.CicloItemBinding

class CicloViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    private val binding = CicloItemBinding.bind(view)

    fun render(ciclo: Ciclo, onClickListener: (Ciclo) -> Unit){
        binding.apply {
            numCiclo.text = getNumCiclo(ciclo)
            annoCiclo.text = "Año ${ciclo.year}"
            estad.text = getEstadoString(ciclo)

        }
        itemView.setOnClickListener {
            onClickListener(ciclo)
        }
    }

    private fun getNumCiclo(ciclo: Ciclo): String {
        return if (ciclo.numero_ciclo == 1) "Primer Ciclo"
        else "Segundo Ciclo"
    }

    private fun getEstadoString(ciclo: Ciclo): String {
        return if (ciclo.ciclo_activo == 1) "Ciclo Activo"
        else "Ciclo Inactivo"
    }
}