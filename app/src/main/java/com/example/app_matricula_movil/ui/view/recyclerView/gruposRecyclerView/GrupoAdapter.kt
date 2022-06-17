package com.example.app_matricula_movil.ui.view.recyclerView.gruposRecyclerView

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.grupo.GrupoComplejo
import java.util.*
import kotlin.collections.ArrayList

class GrupoAdapter(private val grupos: ArrayList<GrupoComplejo>, private val onClickListener: (GrupoComplejo) -> Unit) :
    RecyclerView.Adapter<GrupoViewHolder>(),
    Filterable {

    private var itemsList: ArrayList<GrupoComplejo> = grupos

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrupoViewHolder {
        return GrupoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.grupo_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GrupoViewHolder, position: Int) {
        holder.render(itemsList[position], onClickListener)
    }

    override fun getItemCount(): Int = itemsList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                itemsList = if (charSearch.isEmpty()) {
                    grupos
                } else {
                    val resultList = ArrayList<GrupoComplejo>()

                    for (row in grupos) {
                        if (row.numero_grupo.lowercase(Locale.getDefault())
                                .contains(charSearch.lowercase(Locale.getDefault())) ||
                            row.profesor.nombre.lowercase(Locale.getDefault())
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
                itemsList = results?.values as ArrayList<GrupoComplejo>
                notifyDataSetChanged()
            }
        }
    }
}