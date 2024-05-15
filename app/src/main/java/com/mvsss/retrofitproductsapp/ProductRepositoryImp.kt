package com.mvsss.retrofitproductsapp

import API
import com.mvsss.retrofitproductsapp.model.Product
import com.mvsss.retrofitproductsapp.model.Products
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepositoryImp(val api : API) : ProductRepository {
    override suspend fun getProducts(): Flow<Result<List<Product>>> {
        return flow {
            try {
                val data = api.getProducts()
                emit(Result.Success(data.products))
            }
            catch (e : Exception){
                e.printStackTrace()
                emit(Result.Error(msg = "Error"))
            }
        }
    }
}