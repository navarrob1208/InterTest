package com.example.test.ui.mainViewModel

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.test.data.database.SQLiteDatabaseHelper
import com.example.test.data.net.API
import com.example.test.data.net.responses.Table
import com.example.test.utils.NetworkMonitor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class TableViewModel(private val context: Context): ViewModel() {
    private val api = API()

    private val _hasInternet = MutableLiveData(false)
    val hasInternet: LiveData<Boolean> get() = _hasInternet

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    private var _queries = MutableLiveData<ArrayList<Table>>(arrayListOf())
    private val queries: LiveData<ArrayList<Table>> get() = _queries

    private var _tablesNames = MutableLiveData<ArrayList<String>>(arrayListOf())
    val tablesNames: LiveData<ArrayList<String>> get() = _tablesNames

    private val _message = MutableLiveData("")
    val message: LiveData<String> get() = _message

    init {
        checkConnectivity()
    }

    fun getQueries(){
        viewModelScope.launch {
            loading(true)
            if(!areTablesCreated()){
                if(hasInternet.value == true){
                    _queries = getQueriesInformation()

                    queries.asFlow().collect{
                        if(it.size > 0 && !areTablesCreated()){
                            createTables(it)
                            getAndShowTables()
                        }
                    }
                }else {
                    showMessage("No estas conectado a internet")
                }
            }else{
                getAndShowTables()
            }
            loading(false)
        }
    }

    private fun loading(loading: Boolean){
        _loading.value = loading
    }

    private fun showMessage(message: String){
        _message.value = message
    }

    private fun createTables(tables: ArrayList<Table>) {
        val db = SQLiteDatabaseHelper(context).writableDatabase
        if(tables.size > 0){
            for (i in tables){
                val parsedTable = parseQueryInformation(i)
                db.execSQL(parsedTable.queryCreation)
            }
        }
        db.close()
    }

    private fun areTablesCreated(): Boolean {
        val db = SQLiteDatabaseHelper(context).readableDatabase
        val cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table' AND name != 'android_metadata';", null)
        val created = cursor.count > 1
        cursor.close()
        return created
    }

    private fun getAndShowTables(): ArrayList<String> {
        val db = SQLiteDatabaseHelper(context).readableDatabase
        val cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table' AND name != 'android_metadata';", null)
        val names: ArrayList<String> = arrayListOf()

        with(cursor) {
            while (moveToNext()) {
                names.add(getString(0))
            }
            _tablesNames.value = names
        }
        cursor.close()
        loading(false)
        return names
    }

    private fun getQueriesInformation() : MutableLiveData<ArrayList<Table>> {
        return api.getData("admin")
    }

    private fun parseQueryInformation(table: Table): Table {
        val pattern = "\\r\\n"
        val parsedQuery = Regex(pattern).replace(table.queryCreation, " ")
        table.queryCreation = "$parsedQuery;"
        return table
    }

    private fun checkConnectivity(){
        viewModelScope.launch {
            val connectivity = NetworkMonitor(context)
            connectivity.asFlow().collect {
                _hasInternet.value = it
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class TableViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TableViewModel(context) as T
    }
}