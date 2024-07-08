package com.codesultan.plaqueclothing.network

import com.codesultan.plaqueclothing.data.ResponseItem
import com.codesultan.plaqueclothing.data.ResponseItems
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Defines network API interface for Retrofit to fetch character data from the Rick and Morty API
interface ApiService {
    @GET("character") // Endpoint to get character list
    suspend fun getCharacters(): ResponseItems // Asynchronously fetches characters

    @GET("products/{product_Id}")
    suspend fun getProduct(
        @Path("product_Id") productId: String,
        @Query("Apikey") apiKey:String,
        // @Query("product_id") productParam: String,
        @Query("organization_id") orgId: String,
        @Query("Appid") appId: String
    ): ResponseItem

    @GET("products")
    fun getProducts(
        @Query("Apikey") apiKey:String,
        @Query("organization_id") organizationId: String,
        @Query("Appid") appId: String
    ): ResponseItems
}