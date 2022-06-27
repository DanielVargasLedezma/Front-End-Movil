package com.example.app_matricula_movil.ui.view.recyclerView.ciclosRecyclerView

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.carrera.CarreraCompleja
import com.example.app_matricula_movil.data.models.ciclo.Ciclo
import com.example.app_matricula_movil.databinding.CarreraItemBinding
import com.example.app_matricula_movil.databinding.CicloItemBinding
import com.example.app_matricula_movil.ui.view.recyclerView.carrerasRecyclerView.CarreraViewHolder
import java.util.*
import kotlin.collections.ArrayList

class CicloAdapter (
    private val ciclos: ArrayList<Ciclo>,
    private  val onClickListener: (Ciclo) -> Unit
) : RecyclerView.Adapter<CicloViewHolder>(), Filterable {
    var itemsList: ArrayList<Ciclo> = ciclos

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CicloViewHolder{
        return CicloViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.ciclo_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CicloViewHolder, position: Int) {
        holder.render(itemsList[position], onClickListener)
    }

    override fun getItemCount(): Int = itemsList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                itemsList = if (charSearch.isEmpty()) {
                    ciclos
                } else {
                    val resultList = ArrayList<Ciclo>()

                    for (row in ciclos) {
                        /*if (row.numero_ciclo.lowercase(Locale.getDefault())
                                .contains(charSearch.lowercase(Locale.getDefault())) ||
                            row.year.lowercase(Locale.getDefault())
                                .contains(charSearch.lowercase(Locale.getDefault()))
                        ) {
                            resultList.add(row)
                        }*/
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
                itemsList = results?.values as ArrayList<Ciclo>
                notifyDataSetChanged()
            }
        }
    }
}