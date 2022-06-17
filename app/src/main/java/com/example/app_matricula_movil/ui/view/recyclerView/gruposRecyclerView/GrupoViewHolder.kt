package com.example.app_matricula_movil.ui.view.recyclerView.gruposRecyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.app_matricula_movil.data.models.grupo.GrupoComplejo
import com.example.app_matricula_movil.databinding.GrupoItemBinding

class GrupoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = GrupoItemBinding.bind(view)

    fun render(grupoComplejo: GrupoComplejo, onClickListener: (GrupoComplejo) -> Unit) {
        binding.apply {
            numeroGrupo.text = grupoComplejo.numero_grupo
            nombreProfesor.text = grupoComplejo.profesor.nombre
            horarioGrupo.text = getHorario(grupoComplejo)
        }

        itemView.setOnClickListener { onClickListener(grupoComplejo) }
    }

    private fun getHorario(grupoComplejo: GrupoComplejo): String {
        return if (grupoComplejo.dia_dos != null && grupoComplejo.dia_dos != "" && grupoComplejo.dia_dos != "null") "${grupoComplejo.dia_uno} / ${grupoComplejo.dia_dos} - ${grupoComplejo.horario}"
        else "${grupoComplejo.dia_uno} - ${grupoComplejo.horario}"
    }
}