package com.example.app_matricula_movil.ui.view.recyclerView.usuariosRecyclerView

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.Usuario
import java.util.*
import kotlin.collections.ArrayList

class UsuarioAdapter(
    private val usuarios: ArrayList<Usuario>,
    private val onClickListener: (Usuario) -> Unit
) : RecyclerView.Adapter<UsuarioViewHolder>(), Filterable {

    private var itemsList: ArrayList<Usuario> = usuarios

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        return UsuarioViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.usuario_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        holder.render(itemsList[position], onClickListener)
    }

    override fun getItemCount(): Int = itemsList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                itemsList = if (charSearch.isEmpty()) {
                    usuarios
                } else {
                    val resultList = ArrayList<Usuario>()

                    for (row in usuarios) {
                        if (row.cedula_usuario.lowercase(Locale.getDefault())
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
                itemsList = results?.values as ArrayList<Usuario>
                notifyDataSetChanged()
            }
        }
    }
}