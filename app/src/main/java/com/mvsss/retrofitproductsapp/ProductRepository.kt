package com.mvsss.retrofitproductsapp

import com.mvsss.retrofitproductsapp.model.Product
import com.mvsss.retrofitproductsapp.model.Products
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts(): Flow<Result<List<Product>>>
}