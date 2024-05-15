package com.mvsss.retrofitproductsapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.mvsss.retrofitproductsapp.model.Product
import com.mvsss.retrofitproductsapp.ui.theme.RetrofitProductsAppTheme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ProductsViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory{
            override fun < T: ViewModel> create( modelClass: Class<T>) : T {
                return ProductsViewModel(
                    ProductRepositoryImp(RetrofitInstance.api)
                ) as T
            }
        }
    })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RetrofitProductsAppTheme {

                val productsList = viewModel.products.collectAsState().value

                LaunchedEffect(key1 = viewModel.showError) {
                    viewModel.showError.collectLatest {  show ->
                        if(show){
                            Toast.makeText(this@MainActivity,"error", Toast.LENGTH_SHORT ).show()
                        }
                    }

                }
                if(productsList.isEmpty()){

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator()
                    }

                }

                else {

                    Spacer(modifier = Modifier.height(18.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        items(productsList.size) { index ->
                            showProduct(productsList[index])
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
    @Composable
    private fun showProduct(product: Product) {

        val imageState = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(product.thumbnail)
                .size((Size.ORIGINAL))
                .build()
        ).state




        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .height(300.dp)
                .background(MaterialTheme.colorScheme.inversePrimary)

        ) {

            if(imageState is AsyncImagePainter.State.Error) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
                if(imageState is AsyncImagePainter.State.Success){

                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    painter = imageState.painter ,
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop
                )

                    Spacer(modifier = Modifier.height(6.dp))


                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = "${product.title} -- Price : ${product.price}$",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = product.description,
                        fontSize = 14.sp
                    )

            }
        }


    }
}
