package tunanh.test_app.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import tunanh.test_app.api.model.AccountRequest
import tunanh.test_app.api.model.TokenResponse

interface Cs {
    @POST
    @Multipart
    suspend fun tokenize(
        @Body requestBody: AccountRequest,
    ): Response<TokenResponse>
}