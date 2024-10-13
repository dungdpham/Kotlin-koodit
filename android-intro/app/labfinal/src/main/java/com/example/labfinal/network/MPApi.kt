package com.example.labfinal.network

import com.example.labfinal.data.model.MPData
import com.example.labfinal.data.model.MPDataExtra
import retrofit2.http.GET

// Dung Pham 2217963 10.10.24
// API interface for Retrofit to fetch data from the provided endpoints, which are appended to
// the base URL specified in the Retrofit instance
interface MPApi {
    @GET("seating.json")
    suspend fun getMPsData(): List<MPData>

    @GET("extras.json")
    suspend fun getMPsExtraData(): List<MPDataExtra>
}