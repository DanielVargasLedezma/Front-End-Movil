package com.example.app_matricula_movil.ui.view.recyclerView.cursosRecyclerView

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.curso.CursoComplejo
import java.util.*
import kotlin.collections.ArrayList

class CursoAdapter(
    private val cursos: ArrayList<CursoComplejo>,
    private val onClickListener: (CursoComplejo) -> Unit
) : RecyclerView.Adapter<CursoViewHolder>(), Filterable {

    private var itemsList: ArrayList<CursoComplejo> = cursos

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CursoViewHolder {
        return CursoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.curso_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CursoViewHolder, position: Int) {
        holder.render(itemsList[position], onClickListener)
    }

    override fun getItemCount(): Int = itemsList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                itemsList = if (charSearch.isEmpty()) {
                    cursos
                } else {
                    val resultList = ArrayList<CursoComplejo>()

                    for (row in cursos) {
                        if (row.codigo_curso.lowercase(Locale.getDefault())
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
                itemsList = results?.values as ArrayList<CursoComplejo>
                notifyDataSetChanged()
            }
        }
    }
}