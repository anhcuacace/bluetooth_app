package tunanh.test_app.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import tunanh.test_app.api.model.AccountRequest

class CallApiRepository private constructor() {
    companion object {
        private var instance: CallApiRepository? = null
        fun getInstance(): CallApiRepository = instance ?: synchronized(this) {
            instance ?: CallApiRepository()
                .also { instance = it }
        }
    }

    @get:Synchronized
    private val cardPointService = ApiClient.getCardPointService()

    @get:Synchronized
    private val csCardPointService = ApiClient.getCsCardPointService()

    private suspend fun <T> callApi(call: suspend () -> Response<T>): APiResponse<T> =
        withContext(Dispatchers.IO) {
            try {
                val response = call()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) return@withContext APiResponse.DataSuccess(body)
                }
                return@withContext APiResponse.DataError()
            } catch (e: Exception) {
                return@withContext APiResponse.DataError()
            }
        }


    suspend fun getToken(acc: AccountRequest) =
        callApi { csCardPointService.tokenize(acc) }

}