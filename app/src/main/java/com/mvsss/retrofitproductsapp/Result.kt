package com.mvsss.retrofitproductsapp

sealed class Result<T>(
    val data:T ?=null,
    val msg: String?=null
) {
    class Success<T>(data:T ):Result<T>(data)
    class Error<T>(data:T?= null, msg: String ):Result<T>(data,msg)
}