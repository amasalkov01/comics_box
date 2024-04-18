package com.div05.comicsbox.data

sealed class Operation<out R> {
    data class Success<out T>(val data: T) : Operation<T>()
    data class Error(val exception: Exception) : Operation<Nothing>()
}

fun <T> Operation<T>.successOr(fallback: T): T {
    return (this as? Operation.Success<T>)?.data ?: fallback
}
