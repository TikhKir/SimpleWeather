package com.example.simpleweather.utils.datawrappers

data class Result<T>(
    var resultType: ResultType,
    val data: T? = null,
    val error: Exception? = null
) {

    companion object {
        fun <T> success(data: T?): Result<T> {
            return Result(ResultType.SUCCESS, data)
        }

        fun <T> error(error: Exception? = null): Result<T> {
            return Result(ResultType.ERROR, error = error)
        }
    }

    fun <R> transformResult(transform: (T?) -> R?): Result<R> {
        return if (this.resultType == ResultType.SUCCESS) {
            success(transform(this.data))
        } else {
            error(this.error)
        }
    }

}