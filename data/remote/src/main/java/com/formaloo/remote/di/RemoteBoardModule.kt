package com.formaloo.remote.di


import com.formaloo.remote.boards.BoardsDatasource
import com.formaloo.remote.boards.BoardsService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun createRemoteBoardModule(
    baseUrl: String,
    appToken: String
) = module {

    factory(named(remoteBoardsModulConstant.InterceptorName)) {
        Interceptor { chain ->
            val original = chain.request()

            val request =
                original.newBuilder()
//                    .header("x-api-key", appToken)
                    .header("x-api-key", appToken)
                    .method(original.method, original.body)
                    .build()

            chain.proceed(request)
        }
    }


    single(named(remoteBoardsModulConstant.ClientName)) {
        OkHttpClient.Builder()
            .addInterceptor(get(named(remoteBoardsModulConstant.InterceptorName)) as Interceptor)
            .connectTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .build()
    }

    factory(named(remoteBoardsModulConstant.RetrofitName)) {
        Retrofit.Builder()
            .client(get(named(remoteBoardsModulConstant.ClientName)))
//            .baseUrl(baseUrl)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    factory(named(remoteBoardsModulConstant.ServiceName)) {
        get<Retrofit>(named(remoteBoardsModulConstant.RetrofitName)).create(
            BoardsService::class.java
        )
    }

    factory(named(remoteBoardsModulConstant.DataSourceName)) {
        BoardsDatasource(
            get(
                named(
                    remoteBoardsModulConstant.ServiceName
                )
            )
        )
    }
}

object remoteBoardsModulConstant {
    const val DataSourceName = "boardsDatasource"
    const val ServiceName = "boardService"
    const val ClientName = "boardClient"
    const val RetrofitName = "boardRetrofit"
    const val InterceptorName = "boardInterceptor"
}
