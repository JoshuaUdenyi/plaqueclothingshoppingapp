package com.codesultan.plaqueclothing.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.codesultan.plaqueclothing.ui.theme.PlaqueClothingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewProductDetail(navController: NavController) {
    val viewModel: ViewProductDetailsViewModel = hiltViewModel()

    Scaffold(topBar = {
        CenterAlignedTopAppBar(navigationIcon = {
            IconButton(onClick = {
                navController.navigateUp()
            }) {

                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "")
            }
        }, title = { Text(text = "Details") })
    }) {

        ViewProductDetailsItem(modifier = Modifier.padding(it), viewModel)
    }
}

@Composable
fun ViewProductDetailsItem(modifier: Modifier = Modifier, viewModel: ViewProductDetailsViewModel) {
    val product = viewModel.productState.collectAsState().value
    Spacer(modifier = Modifier.padding(top = 16.dp))
    val errorState = viewModel.productError.collectAsState().value
    val loadingState = viewModel.productloading.collectAsState().value
    val retry = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(retry.value) {
        viewModel.fetchProduct()
    }

    if (errorState.state) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Column {
                Text(text = errorState.message)
                Button(onClick = {
                    retry.value = !retry.value
                }) {
                    Text(text = "Retry")
                }
            }
        }
    }

    if (loadingState) {

        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp)
            )
        }
    }
    if (!loadingState && !errorState.state) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),

            horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .width(337.dp)
                        .height(193.dp)
                        .padding(top = 16.dp),
                    model = "https://api.timbu.cloud/images/${product.imageUrl}",
                    contentDescription = null
                )


                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {


                    Text(
                        text = product.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }


            Spacer(modifier = Modifier.padding(8.dp))


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .size(width = 240.dp, height = 400.dp),
                ) {
                    Text(
                        text = "Product Description", fontSize = 20.sp, fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = product.description ?: "No description available",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    Button(
                        onClick = { /*TODO*/ }, modifier = modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "CheckIcon",
                                tint = Color.White
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = product.price + "NGN",
                                fontSize = 16.sp,
                                fontWeight = FontWeight(600),
                                color = Color(0xFFFFFFFF)
                            )
                        }
                    }

                }


            }


        }
    }
}