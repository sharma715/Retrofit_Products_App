import com.mvsss.retrofitproductsapp.model.Product
import com.mvsss.retrofitproductsapp.model.Products
import retrofit2.http.GET

interface API {

    @GET("/products")
    suspend fun getProducts(): Products
    companion object{
        const val BASE_URL = "https://dummyjson.com"
    }
}