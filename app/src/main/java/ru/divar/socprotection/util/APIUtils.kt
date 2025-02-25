package ru.divar.socprotection.util

import kotlinx.coroutines.runBlocking
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import ru.divar.socprotection.data.SocialResponse
import java.util.concurrent.TimeUnit

class APIUtils {
    companion object {

        internal suspend fun post(
            url: String,
            jsonObject: JSONObject,
            contentType: String
        ): SocialResponse {
            val request = preparePostRequest(url, jsonObject, contentType)
            val response = sendRequest(request)

            val code = response.code
            val body = response.body.string()

            response.close()

            return SocialResponse(code, body)
        }

        internal fun get(
            url: String
        ): SocialResponse =
            runBlocking {
                val request = prepareGetRequest(url)
                val response = sendRequest(request)

                val code = response.code
                val body = response.body.string()

                response.close()

                return@runBlocking SocialResponse(code, body)
            }

        private fun jsonToRequestBody(jsonObject: JSONObject) =
            "$jsonObject".toRequestBody("application/json; charset=utf-8".toMediaType())

        private fun jsonToFormRequestBody(jsonObject: JSONObject): FormBody {
            val requestBody = FormBody.Builder()
            val temp: Iterator<String> = jsonObject.keys()
            var key: String
            while (temp.hasNext()) {
                key = temp.next()
                val value: Any = jsonObject.get(key)
                requestBody.add(key, value as String)
            }
            return requestBody.build()
        }

        private fun preparePostRequest(
            url: String,
            jsonObject: JSONObject,
            contentType: String
        ): Request? {
            return when (contentType) {
                JSON ->
                    Request.Builder()
                        .url("${APIConfig.url}$url")
                        .post(jsonToRequestBody(jsonObject))
                        .build()

                URL_ENCODED -> {
                    Request.Builder()
                        .url("${APIConfig.url}$url")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .post(jsonToFormRequestBody(jsonObject))
                        .build()
                }

                else -> null
            }
        }

        private fun prepareGetRequest(url: String) =
            Request.Builder()
                .url("${APIConfig.url}$url")
                .addHeader("charset", "utf-8")
                .get()
                .build()

        private fun sendRequest(request: Request?): Response {
            if (request == null) error("empty request")

            return OkHttpClient
                .Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .callTimeout(2, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true)
                .build()
                .newCall(request)
                .execute()
        }

        const val JSON = "application/json"
        const val URL_ENCODED = "application/x-www-form-urlencoded"

        fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
            when (val value = this[it]) {
                is JSONArray -> {
                    val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
                    JSONObject(map).toMap().values.toList()
                }

                is JSONObject -> value.toMap()
                JSONObject.NULL -> null
                else -> value
            }
        }
    }
}