package com.example.app_matricula_movil.ui.view.recyclerView.alumnosRecyclerView

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.alumno.AlumnoComplejo
import java.util.*
import kotlin.collections.ArrayList


class AlumnoAdapter (
    private val alumnos: ArrayList<AlumnoComplejo>,
    private val onClickListener: (AlumnoComplejo) -> Unit
) : RecyclerView.Adapter<AlumnoViewHolder>(), Filterable{

    private var itemsList: ArrayList<AlumnoComplejo> = alumnos

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumnoViewHolder {
        return AlumnoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.alumno_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AlumnoViewHolder, position: Int) {
        holder.render(itemsList[position], onClickListener)
    }

    override fun getItemCount(): Int = itemsList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                itemsList = if (charSearch.isEmpty()) {
                    alumnos
                } else {
                    val resultList = ArrayList<AlumnoComplejo>()

                    for (row in alumnos) {
                        if (row.cedula_alumno.lowercase(Locale.getDefault())
                                .contains(charSearch.lowercase(Locale.getDefault())) ||
                            row.nombre.lowercase(Locale.getDefault())
                                .contains(charSearch.lowercase(Locale.getDefault()))
                        ) {
                            resultList.add(row)
                        }
                    }

                    resultList
                }

                val filterResults = FilterResults()

                filterResults.values = itemsList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                itemsList = results?.values as ArrayList<AlumnoComplejo>
                notifyDataSetChanged()
            }
        }
    }

}

