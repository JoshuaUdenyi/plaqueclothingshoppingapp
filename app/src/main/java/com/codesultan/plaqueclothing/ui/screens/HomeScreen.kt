package com.codesultan.plaqueclothing.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController, productViewModel: ProductViewModel = hiltViewModel(),
) {

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text(text = "Plaque Clothing", fontWeight = FontWeight.Bold) })
    }) {
        ProductListContent(
            modifier = Modifier.padding(it), navController = navController, productViewModel = productViewModel
        )

    }
}

@Composable
fun ProductListContent(
    modifier: Modifier = Modifier, productViewModel: ProductViewModel, navController: NavController
) {

    val value = productViewModel.productsState.collectAsState().value

    var errorMessage: String = ""


    val retry = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(retry.value) {
        productViewModel.fetchProducts()
    }


    Spacer(modifier = Modifier.padding(top = 16.dp))
    val errorState = productViewModel.productError.collectAsState().value
    val loadingState = productViewModel.productloading.collectAsState().value

    if (errorState.state) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Column {
                Text(text = errorState.message)
                Text(text = "Retry", color = Color.Blue, modifier = Modifier.clickable {
                    retry.value = !retry.value
                })
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

    LazyVerticalGrid(
        modifier = modifier.padding(16.dp),
        columns = GridCells.Fixed(2),

        ) {

        items(value.size) { clothingItems ->

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable(onClick = {
                            navController.navigate("details" + "?id=${value[clothingItems].id}&price=${value[clothingItems].currentPrice[0].NGN[0].toString()}")
                        }), shape = RoundedCornerShape(8.dp)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(shape = RoundedCornerShape(8.dp)),
                        model = "https://api.timbu.cloud/images/${value[clothingItems].photos[0].url}",
                        contentDescription = null,
                    )

                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier.padding(start = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = value[clothingItems].name,
                            style = MaterialTheme.typography.headlineSmall,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            modifier = Modifier.size(width = 124.dp, height = 32.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = value[clothingItems].currentPrice[0].NGN[0].toString() + " NGN",
                                style = MaterialTheme.typography.bodyMedium,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                            IconButton(onClick = {
                                navController.navigate("details" + "?id=${value[clothingItems].id}&price=${value[clothingItems].currentPrice[0].NGN[0].toString()}")
                            }) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = ""
                                )
                            }
                        }

                    }

                }

                Spacer(modifier = Modifier.padding(bottom = 16.dp))
            }

        }
    }


}