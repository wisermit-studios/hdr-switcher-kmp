package com.wisermit.hdrswitcher.domain

import com.wisermit.hdrswitcher.framework.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class UseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher = COROUTINE_DISPATCHER,
) : CoreUseCase() {
    suspend operator fun invoke(parameters: P): Result<R> =
        runCatching {
            logInvoke(parameters)

            withContext(coroutineDispatcher) {
                execute(parameters)
            }
        }.onFailure { e ->
            logFailure(e)
        }

    protected abstract suspend fun execute(parameters: P): R
}

abstract class CoreUseCase {

    private val tag = javaClass.simpleName

    protected fun logInvoke(parameters: Any?) {
        val message: String = when (parameters) {
            is Unit -> ""
            else -> "parameters: $parameters"
        }

        Log.d(tag, message)
    }

    protected fun logFailure(throwable: Throwable) {
        Log.e(tag, "failure: ${throwable.javaClass.simpleName}", throwable)
    }

    companion object {
        val COROUTINE_DISPATCHER = Dispatchers.Default
    }
}

