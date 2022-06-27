package com.example.app_matricula_movil.ui.view.recyclerView.profesorRecycleView

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.profesor.Profesor
import java.util.*
import kotlin.collections.ArrayList


class ProfesorAdapter (
    private val profesores: ArrayList<Profesor>,
    private val onClickListener: (Profesor) -> Unit
) : RecyclerView.Adapter<ProfesorViewHolder>(), Filterable {

    var itemsList: ArrayList<Profesor> = profesores

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfesorViewHolder {
        return ProfesorViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.profesor_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProfesorViewHolder, position: Int) {
        holder.render(itemsList[position], onClickListener)
    }

    override fun getItemCount(): Int = itemsList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                itemsList = if (charSearch.isEmpty()) {
                    profesores
                } else {
                    val resultList = ArrayList<Profesor>()

                    for (row in profesores) {
                        if (row.cedula_profesor.lowercase(Locale.getDefault())
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
                itemsList = results?.values as ArrayList<Profesor>
                notifyDataSetChanged()
            }
        }
    }

}