package com.codesultan.plaqueclothing.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitAPI {
    @GET("products/{product_Id}")
    suspend fun getProduct(
        @Path("product_Id") productId: String,
        @Query("Apikey") apiKey:String,
        @Query("organization_id") orgId: String,
        @Query("Appid") appId: String
    ): Response<ResponseItem>

    @GET("products")
    fun getProducts(
        @Query("Apikey") apiKey:String,
        @Query("organization_id") organizationId: String,
        @Query("Appid") appId: String
    ): Call<ResponseItems>
}

class ApiService {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.timbu.cloud/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()



    suspend fun getProduct(productId: String): ProductState {
       val apiService: RetrofitAPI = retrofit.create(RetrofitAPI::class.java)
        try {
            val response = apiService.getProduct(
                productId = productId,
                apiKey = "d10476c5a1854eb69993e450db7a2fac20240705153725665871",
                orgId = "7bd290907cfa4e28a024d5826323f953",
                appId = "9UFHP10Q4H9AW64"
            )

            return if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    Log.d("APIService","$data")
                    ProductState.Success(data)
                } else {
                    ProductState.Error("No Response")
                }
            } else {
                ProductState.Error("Failed to get product data")
            }
        } catch (e: Exception) {
            return ProductState.Error("Network error: Ensure your internet is on")
        }
    }
    suspend fun getProducts(): Flow<ProductsState> {
       val apiService: RetrofitAPI = retrofit.create(RetrofitAPI::class.java)
        val responseFlow: MutableStateFlow<ProductsState> = MutableStateFlow(ProductsState.Loading)
        val request: Call<ResponseItems> = apiService.getProducts(
            apiKey = "d10476c5a1854eb69993e450db7a2fac20240705153725665871",
            organizationId = "7bd290907cfa4e28a024d5826323f953",
            appId = "9UFHP10Q4H9AW64"
        )
        request.enqueue(object : Callback<ResponseItems> {
            override fun onResponse(p0: Call<ResponseItems>, p1: Response<ResponseItems>) {
                Log.d("Response", "${p1.body()}")

                val responseItems: ResponseItems? = p1.body()
                var items: List<ResponseItem> = responseItems?.items ?: mutableListOf()
              if (items.isNotEmpty())  {
                  responseFlow.value = ProductsState.Success(items)
              }
            }

            override fun onFailure(p0: Call<ResponseItems>, p1: Throwable) {
                Log.d("API", "$p1")
                responseFlow.value = ProductsState.Error("Network Error: Ensure your internet is on")
            }

        })

        return responseFlow

    }
}

