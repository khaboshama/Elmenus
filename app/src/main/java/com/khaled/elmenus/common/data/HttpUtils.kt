package com.khaled.elmenus.common.data

import com.khaled.elmenus.R
import com.khaled.elmenus.common.remote.ConnectivityUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.io.InterruptedIOException
import java.net.UnknownHostException

object HttpUtils {
    private val noInternetError = AppResult.Error(
        errorMessageRes = R.string.message_error_no_internet
    )

    private val unexpectedError = AppResult.Error(
        errorMessageRes = R.string.something_went_wrong
    )

    private fun getHttpError(error: String) = AppResult.Error(
        errorMessage = error
    )

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): AppResult<T> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                if (ConnectivityUtils.isNetworkConnected().not()) return@withContext noInternetError
                val response = call()
                return@withContext handleResult(response)

            } catch (error: Throwable) {
                parseErrorResponse(error)
            }
        }
    }

    fun parseErrorResponse(error: Throwable) =
        when (error) {
            is IOException, is InterruptedIOException, is UnknownHostException -> noInternetError
            is HttpException -> getHttpError(error.message())
            else -> unexpectedError
        }

    private fun <T : Any> handleResult(response: Response<T>): AppResult<T> {
        return when {
            response.body() != null -> AppResult.Success(response.body()!!)
            else -> unexpectedError
        }
    }

}