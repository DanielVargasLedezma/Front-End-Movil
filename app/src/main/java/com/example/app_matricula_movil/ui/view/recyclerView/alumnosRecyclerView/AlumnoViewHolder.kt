package com.example.app_matricula_movil.ui.view.recyclerView.alumnosRecyclerView

import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.app_matricula_movil.data.models.alumno.AlumnoComplejo
import com.example.app_matricula_movil.databinding.AlumnoItemBinding

class AlumnoViewHolder(view: View) : RecyclerView.ViewHolder(view){
    private val binding = AlumnoItemBinding.bind(view)

    fun render(alumnoComplejo: AlumnoComplejo, onClickListener: (AlumnoComplejo) -> Unit){
        binding.apply {
            cedulaAlumno.text = alumnoComplejo.cedula_alumno
            nombreAlumno.text = alumnoComplejo.nombre
            emailAlumno.text = alumnoComplejo.correo
        }
        itemView.setOnClickListener {
            onClickListener(alumnoComplejo)
        }
    }
}