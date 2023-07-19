package tunanh.test_app.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val CONNECT_TIMEOUT = 10L
    private const val READ_TIMEOUT = 10L
    private const val WRITE_TIMEOUT = 10L
    private const val site = "isv-uat"
    private const val URL = "https://$site.cardconnect.com/cardconnect/rest"
    private const val CsURL = "https://$site.cardconnect.com/cardsecure/api/v1/ccn/tokenize"
    const val Merchid = "800000009175"
    const val Authorization = "Basic dGVzdGluZzp0ZXN0aW5nMTIz"

    private val retrofit: ((String) -> Retrofit) = {

        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .addHeader("Authorization", "Basic dGVzdGluZzp0ZXN0aW5nMTIz")
                            .addHeader("Content-Type", "application/json")
                            .build()
                        chain.proceed(request)
                    }
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .build()
            )
            .baseUrl(it)
            .build()
    }


    fun getCardPointService(): CardPoint = retrofit(URL).create()


    fun getCsCardPointService(): Cs = retrofit(CsURL).create()

}