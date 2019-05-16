package com.carys.dyploma.activities

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

interface Requester {
    companion object {
        fun sendRequest(url: String, reqMethod: String = "GET", parameters: ArrayList<Pair<String, String>> = ArrayList(), headers: ArrayList<Pair<String, String>> = ArrayList()): Pair<Int, String> {
            var reqParam = ""
            val mURL = URL(url)
            parameters.forEach {
                if (it == parameters.last()){
                    reqParam += URLEncoder.encode(it.first, "UTF-8") + "=" + URLEncoder.encode(it.second, "UTF-8")
                } else {
                    reqParam += URLEncoder.encode(it.first, "UTF-8") + "=" + URLEncoder.encode(it.second, "UTF-8") + "&"
                }
            }
            //try {
                with(mURL.openConnection() as HttpURLConnection) {
                    requestMethod = reqMethod
                    headers.forEach {
                        setRequestProperty(it.first, it.second)
                    }
                    val response = StringBuffer()
                    if (requestMethod == "POST") {
                        val wr = OutputStreamWriter(outputStream)
                        wr.write(reqParam)
                        wr.flush()
                    }
                    inputStream?.bufferedReader().use {
                        var inputLine = it?.readLine()
                        while (inputLine != null) {
                            response.append(inputLine)
                            inputLine = it?.readLine()
                        }
                        it?.close()
                    }
                    return responseCode to response.toString()
                }

            /*} catch (e: Exception) {
                return -1 to ""
            }*/
        }
    }
}