package com.example.test.ui.mainView

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.R
import com.example.test.databinding.ActivityMainBinding
import com.example.test.ui.adapters.TablesAdapter
import com.example.test.ui.callbacks.TablesAdapterCallbacks
import com.example.test.ui.mainViewModel.TableViewModel
import com.example.test.ui.mainViewModel.TableViewModelFactory
import com.example.test.utils.NetworkMonitor

class MainActivity : AppCompatActivity(), LifecycleOwner, TablesAdapterCallbacks {
    private lateinit var binding: ActivityMainBinding
    private var adapter: TablesAdapter? = null

    private val viewModel: TableViewModel by viewModels { TableViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.fab.setOnClickListener { view ->
            if((adapter == null || adapter?.isLoaded() == false) && viewModel.loading.value == false){
                viewModel.getQueries()
            }
        }

        viewModel.loading.observe(this){
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.tablesNames.observe(this){
            setupAdapter(it)
        }

        listenToMessages()

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)

        menu?.let { it ->
            val searchItem: MenuItem = it.findItem(R.id.action_search)
            val searchView: SearchView = searchItem.actionView as SearchView

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    adapter?.filter?.filter(newText)
                    return false
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupAdapter(tables: ArrayList<String>){
        adapter = TablesAdapter(tables, this)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.recyclerview.layoutManager = linearLayoutManager
        binding.recyclerview.adapter = adapter


    }

    override fun isEmpty(empty: Boolean) {
        if(empty){
            binding.recyclerview.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        }else {
            binding.recyclerview.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE
        }
    }

    private fun listenToMessages(){
        viewModel.message.observe(this){
            if(it.isNotEmpty()){
                Toast.makeText(this,it, Toast.LENGTH_LONG).show()
            }
        }
    }
}