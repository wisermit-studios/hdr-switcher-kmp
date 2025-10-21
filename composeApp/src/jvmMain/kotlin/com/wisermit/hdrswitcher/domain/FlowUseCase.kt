package com.wisermit.hdrswitcher.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

abstract class ResultFlowUseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher = COROUTINE_DISPATCHER,
) : CoreUseCase() {
    operator fun invoke(parameters: P): Flow<Result<R>> {
        logInvoke(parameters)

        return execute(parameters)
            .catch { e ->
                logFailure(e)
                emit(Result.failure(e))
            }
            .flowOn(coroutineDispatcher)
    }

    protected abstract fun execute(parameters: P): Flow<Result<R>>
}

abstract class FlowUseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher = COROUTINE_DISPATCHER,
) : CoreUseCase() {
    operator fun invoke(parameters: P): Flow<R> {
        logInvoke(parameters)

        return execute(parameters)
            .catch { e ->
                logFailure(e)

                // TODO: Throw only in debug.
                throw e
            }
            .flowOn(coroutineDispatcher)
    }

    protected abstract fun execute(parameters: P): Flow<R>
}