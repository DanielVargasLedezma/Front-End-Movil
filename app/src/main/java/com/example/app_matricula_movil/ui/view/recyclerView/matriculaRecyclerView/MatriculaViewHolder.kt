package com.example.app_matricula_movil.ui.view.recyclerView.matriculaRecyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.app_matricula_movil.data.models.grupo.GrupoComplejo
import com.example.app_matricula_movil.data.models.matricula.MatriculaCompleja
import com.example.app_matricula_movil.databinding.MatriculaItemBinding

class MatriculaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = MatriculaItemBinding.bind(view)

    fun render(matricula: MatriculaCompleja, onClickListener: (MatriculaCompleja) -> Unit) {
        binding.apply {
            codigoGrupo.text = matricula.grupo.numero_grupo
            nombreCurso.text = "${matricula.grupo.curso.codigo_curso} ${matricula.grupo.curso.nombre}"
            nota.text = matricula.nota.toString()
        }

        itemView.setOnClickListener {
            onClickListener(matricula)
        }
    }
}