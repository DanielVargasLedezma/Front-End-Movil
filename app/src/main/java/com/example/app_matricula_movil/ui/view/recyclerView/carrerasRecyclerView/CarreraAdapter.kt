package com.example.app_matricula_movil.ui.view.recyclerView.carrerasRecyclerView

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.carrera.CarreraCompleja
import com.example.app_matricula_movil.data.models.curso.CursoComplejo
import java.util.*
import kotlin.collections.ArrayList

class CarreraAdapter(
    private val carreras: ArrayList<CarreraCompleja>,
    private val onClickListener: (CarreraCompleja) -> Unit
) : RecyclerView.Adapter<CarreraViewHolder>(), Filterable {
    var itemsList: ArrayList<CarreraCompleja> = carreras

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarreraViewHolder {
        return CarreraViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.carrera_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CarreraViewHolder, position: Int) {
        holder.render(itemsList[position], onClickListener)
    }

    override fun getItemCount(): Int = itemsList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                itemsList = if (charSearch.isEmpty()) {
                    carreras
                } else {
                    val resultList = ArrayList<CarreraCompleja>()

                    for (row in carreras) {
                        if (row.codigo_carrera.lowercase(Locale.getDefault())
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
                itemsList = results?.values as ArrayList<CarreraCompleja>
                notifyDataSetChanged()
            }
        }
    }
}