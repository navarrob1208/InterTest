package com.example.test.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.ui.callbacks.TablesAdapterCallbacks

class TablesAdapter(private val tables: ArrayList<String>, private val callbacks: TablesAdapterCallbacks) : RecyclerView.Adapter<TablesAdapter.ViewHolder>(), Filterable{
    private val defaultTables: ArrayList<String> = arrayListOf()

    init {
        defaultTables.addAll(tables)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var tableNameTextview: TextView = view.findViewById(R.id.cardview_table_texview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_table, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return defaultTables.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val table = defaultTables[position]
        holder.tableNameTextview.text = table
    }

    override fun getFilter(): Filter {
        val filteredList: ArrayList<String> = arrayListOf()

        return object : Filter(){
            override fun performFiltering(text: CharSequence?): FilterResults {
                val searchText = text.toString()
                filteredList.clear()

                if(searchText.isNotEmpty()){
                    for (name in tables){
                        if(name.lowercase().contains(searchText.lowercase())){
                            filteredList.add(name)
                        }
                    }
                }

                val result = FilterResults()
                result.values = filteredList
                return result
            }

            override fun publishResults(text: CharSequence?, filtered: FilterResults?) {
                defaultTables.clear()
                if(filteredList.size == 0 && !text.isNullOrEmpty()){
                    callbacks.isEmpty(true)
                }else if(filteredList.size > 0){
                    defaultTables.addAll(filteredList)
                    callbacks.isEmpty(false)
                }else{
                    defaultTables.addAll(tables)
                    callbacks.isEmpty(false)
                }
                notifyDataSetChanged()
            }
        }
    }

    fun isLoaded():Boolean {
        return defaultTables.size > 0
    }

}