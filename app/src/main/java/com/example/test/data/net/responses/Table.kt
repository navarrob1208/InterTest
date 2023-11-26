package com.example.test.data.net.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Table {
    @SerializedName("NombreTabla")
    @Expose
    val tableName = ""

    @SerializedName("Pk")
    @Expose
    val primaryKey = ""

    @SerializedName("QueryCreacion")
    @Expose
    var queryCreation = ""

    @SerializedName("BatchSize")
    @Expose
    val batchSize = 0

    @SerializedName("Filtro")
    @Expose
    val filter = ""

    @SerializedName("Error")
    @Expose
    val error: String? = null

    @SerializedName("NumeroCampos")
    @Expose
    val fieldsNumber = ""

    @SerializedName("MetodoApp")
    @Expose
    val appMethod = ""

    @SerializedName("FechaActualizacionSincro")
    @Expose
    val updateDate = ""

}