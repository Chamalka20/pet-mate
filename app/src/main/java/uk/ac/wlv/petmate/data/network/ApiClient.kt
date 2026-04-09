package uk.ac.wlv.petmate.data.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.ac.wlv.petmate.core.utils.Constants
import uk.ac.wlv.petmate.core.utils.Constants.BASE_URL

object ApiClient {

    //  Logging (only for debug builds ideally)
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Auth Interceptor (adds token automatically)
    private val authInterceptor = Interceptor { chain ->
        val token = ""

        val request = chain.request().newBuilder().apply {
            if (token.isNotEmpty()) {
                addHeader("Authorization", "Bearer $token")
            }
        }.build()

        chain.proceed(request)
    }

    //  OkHttp Client
    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(logging)
        .build()

    //  Single Retrofit instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //  APIs (split by feature)
    val authApi: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    val petApi: PetApiService by lazy {
        retrofit.create(PetApiService::class.java)
    }
    val vetApi: VetApiService by lazy {
        retrofit.create(VetApiService::class.java)
    }
}