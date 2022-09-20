package com.example.triecitysearch.model

data class JsonEntry (var country:String, var name: String, var _id: Long, var coord:CoordClass)
data class CoordClass( var lon: Double, var lat: Double)