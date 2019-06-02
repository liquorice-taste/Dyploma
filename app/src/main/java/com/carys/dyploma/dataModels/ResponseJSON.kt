package com.carys.dyploma.dataModels

data class ResponseJSON<T> (val code: Int, val results: List<T>)