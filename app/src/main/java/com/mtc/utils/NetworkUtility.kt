package com.mtc.utils

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NetworkUtility {

    companion object {
        val mRequestQueue: RequestQueue by lazy {
            Volley.newRequestQueue(MyAppContext.get())
        }

        suspend fun getData(url : String) = suspendCoroutine<String> { cont ->

            val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response ->
                    cont.resume("Response is: ${response.substring(0, 500)}")
                },
                Response.ErrorListener { cont.resume("Something went wrong!") })

            mRequestQueue.add(stringRequest)
        }

        suspend fun APIrequest(url: String): JSONObject {
            Log.v("URL Req", url)
            return suspendCancellableCoroutine { continuation ->
                try {
                    // SucessListener
                    val success = Response.Listener<JSONObject> { response ->
                        if (continuation.isActive) {
                            Log.v("URL RESPONSE", response.toString())
                            continuation.resume(response, null)
                        }
                    }

                    // Error Listener
                    val error = Response.ErrorListener { error ->
                        if (continuation.isActive) {
                            continuation.resume(JSONObject(), null)
                        }
                    }

                    Request.Priority.HIGH

                    val jsonObjectRequest =
                        JsonObjectRequest(Request.Method.GET, url, null, success, error)

                    mRequestQueue.add(jsonObjectRequest)

                } catch (e: Exception) {
                    e.printStackTrace()
//                    if (continuation.isActive) {
//                        if (continuation.isActive) {
//                            continuation.resumeWithException(e)
//                        }
//                    }
                }
            }
        }
    }
}