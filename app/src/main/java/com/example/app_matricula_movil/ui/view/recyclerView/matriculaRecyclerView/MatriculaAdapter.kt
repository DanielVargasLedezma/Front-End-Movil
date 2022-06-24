package com.example.app_matricula_movil.ui.view.recyclerView.matriculaRecyclerView

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.matricula.MatriculaCompleja
import java.util.*
import kotlin.collections.ArrayList

class MatriculaAdapter(
    private val matriculas: ArrayList<MatriculaCompleja>, private val onClickListener: (MatriculaCompleja) -> Unit
) : RecyclerView.Adapter<MatriculaViewHolder>(), Filterable {
    private var itemsList: ArrayList<MatriculaCompleja> = matriculas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatriculaViewHolder {
        return MatriculaViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.matricula_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MatriculaViewHolder, position: Int) {
        holder.render(matriculas[position], onClickListener)
    }

    override fun getItemCount(): Int = itemsList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                itemsList = if (charSearch.isEmpty()) {
                    matriculas
                } else {
                    val resultList = ArrayList<MatriculaCompleja>()

                    for (row in matriculas) {
                        if (row.grupo.numero_grupo.lowercase(Locale.getDefault())
                                .contains(charSearch.lowercase(Locale.getDefault())) ||
                            row.grupo.profesor.nombre.lowercase(Locale.getDefault())
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
                itemsList = results?.values as ArrayList<MatriculaCompleja>
                notifyDataSetChanged()
            }
        }
    }

}