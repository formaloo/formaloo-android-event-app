package com.formaloo.remote.di


import com.formaloo.remote.FormAllDatasource
import com.formaloo.remote.FormService
import com.formaloo.remote.di.remoteAllFormModulConstant.ClientName
import com.formaloo.remote.di.remoteAllFormModulConstant.DataSourceName
import com.formaloo.remote.di.remoteAllFormModulConstant.InterceptorName
import com.formaloo.remote.di.remoteAllFormModulConstant.RetrofitName
import com.formaloo.remote.di.remoteAllFormModulConstant.ServiceName
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun createRemoteAllFormzModule(
    url: String,
    xapikey: String
) = module {

    single(named(InterceptorName)) {
        Interceptor { chain ->
            val original = chain.request()
            val request =
                original.newBuilder()
                    .header("x-api-key", xapikey)

                    .method(original.method, original.body)
                    .build()

            chain.proceed(request)
        }
    }
//

    single(named(ClientName)) {
        OkHttpClient.Builder()

            .addInterceptor(get(named(InterceptorName)) as Interceptor)
            .connectTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .build()
    }

    single(named(RetrofitName)) {

        Retrofit.Builder()
            .client(get(named(ClientName)))
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    single(named(ServiceName)) {
        get<Retrofit>(named(RetrofitName)).create(
            FormService::class.java
        )
    }

    single(named(DataSourceName)) { FormAllDatasource(get(named(ServiceName))) }
}

object remoteAllFormModulConstant {
    const val DataSourceName = "FormzDatasource"
    const val ServiceName = "formzService"
    const val ClientName = "formzClient"
    const val RetrofitName = "formzRetrofit"
    const val InterceptorName = "formzInterceptor"
}


