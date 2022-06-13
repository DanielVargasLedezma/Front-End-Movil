package com.example.app_matricula_movil.ui.view.recyclerView.cursosRecyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.app_matricula_movil.data.models.curso.CursoComplejo
import com.example.app_matricula_movil.databinding.CursoItemBinding

class CursoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = CursoItemBinding.bind(view)

    fun render(cursoComplejo: CursoComplejo, onClickListener: (CursoComplejo) -> Unit) {
        binding.apply {
            codigoCurso.text = cursoComplejo.codigo_curso
            nombreCurso.text = cursoComplejo.nombre
            creditosCurso.text =  "Total de créditos ${ cursoComplejo.creditos.toString() }"
        }

        itemView.setOnClickListener {
            onClickListener(cursoComplejo)
        }
    }
}