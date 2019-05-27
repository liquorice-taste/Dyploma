package com.carys.dyploma.activities.dataModels

data class ResponseJSON<T> (val code: Int, val results: List<T>){
}

data class Responset (val response: Int)