package com.formaloo.repository

import com.formaloo.common.exception.Failure
import com.formaloo.common.exception.ViewFailure
import com.formaloo.common.functional.Either
import com.formaloo.model.Result
import org.json.JSONObject
import retrofit2.Call
import timber.log.Timber
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseRepo {

    fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Either<Failure, R> {
        return try {
            val response = call.execute()

            var jObjError: JSONObject? = null
            try {
                jObjError = JSONObject(response.errorBody()?.string() ?: "")

            } catch (e: Exception) {
                Timber.e("request: ${response.raw()}")
            }

            when (response.code()) {
                200 -> Either.Right(transform((response.body() ?: default)))
                201 -> Either.Right(transform((response.body() ?: default)))
                400 -> Either.Left(ViewFailure.responseError("$jObjError"))
                401 -> Either.Left(Failure.UNAUTHORIZED_Error)
                500 -> Either.Left(Failure.ServerError)
                else -> Either.Left(ViewFailure.responseError("$jObjError"))
            }

        } catch (exception: IOException) {
            Either.Left(ViewFailure.responseError("IOException++>  $exception"))

        } catch (exception: SocketException) {
            Either.Left(Failure.NetworkConnection)

        } catch (exception: SocketTimeoutException) {
            Either.Left(Failure.NetworkConnection)

        } catch (exception: UnknownHostException) {
            Either.Left(Failure.NetworkConnection)

        } catch (exception: Throwable) {
            Timber.e("request: $exception")
            Either.Left(ViewFailure.responseError("exception++>  $exception"))
        }

    }

    fun <T, R> callRequest(call: Call<T>, transform: (T) -> R, default: T): Result<R> {
        return try {
            val response = call.execute()
            when (response.code()) {
                200 -> Result.Success(transform((response.body() ?: default)))
                201 -> Result.Success(transform((response.body() ?: default)))
                else -> Result.Error(null)
            }

        } catch (exception: IOException) {
            Result.Error(exception)

        } catch (exception: SocketException) {
            Result.Error(exception)

        } catch (exception: SocketTimeoutException) {
            Result.Error(exception)

        } catch (exception: UnknownHostException) {
            Result.Error(exception)

        } catch (exception: Throwable) {
            Timber.e("request: $exception")
            Result.Error(null)
        }

    }

}
