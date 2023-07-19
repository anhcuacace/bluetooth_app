package tunanh.test_app.api.model

import tunanh.test_app.api.ApiClient

data class AuthRequest(
    val account: String,
    val expiry: String,
    val amount: String,
    val name: String,
    val currency: String = "USA",
    val merchid: String = ApiClient.Merchid,
)
