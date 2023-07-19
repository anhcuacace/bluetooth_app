package tunanh.test_app.api

import retrofit2.http.Body
import retrofit2.http.PUT
import tunanh.test_app.api.model.AuthRequest
import tunanh.test_app.api.model.AuthResponse
import tunanh.test_app.api.model.CaptureRequest
import tunanh.test_app.api.model.CaptureResponse
import tunanh.test_app.api.model.VoidByIdRequest
import tunanh.test_app.api.model.VoidByOrderidResponse
import tunanh.test_app.api.model.VoidByRetref
import tunanh.test_app.api.model.VoidResponse

interface CardPoint {
    @PUT("/auth")
    fun putAuth(@Body auth: AuthRequest): AuthResponse


    @PUT("/capture")
    fun putCapture(@Body captureRequest: CaptureRequest): CaptureResponse

    @PUT("/void")
    fun void(@Body voidByRetref: VoidByRetref): VoidResponse

    @PUT("voidByOrderId")
    fun void(@Body voidByIdRequest: VoidByIdRequest): VoidByOrderidResponse


}