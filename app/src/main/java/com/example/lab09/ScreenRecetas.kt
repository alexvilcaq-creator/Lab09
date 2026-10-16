package com.example.lab09

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@Composable
fun ScreenRecetas(navController: NavHostController, servicio: RecipeApiService) {
    var recetas by remember { mutableStateOf<List<RecipeModel>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var hayError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            recetas = servicio.getRecipes().recipes
        } catch (e: Exception) {
            hayError = true
        }
        cargando = false
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF3E0))
    ) {
        Text(
            text = "Recetas del mundo",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE65100),
            modifier = Modifier.padding(16.dp)
        )

        when {
            cargando -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFE65100))
            }
            hayError -> Text(
                text = "No se pudieron cargar las recetas",
                modifier = Modifier.padding(16.dp)
            )
            else -> LazyColumn(
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(recetas) { receta ->
                    TarjetaReceta(receta) {
                        navController.navigate("recetaVer/${receta.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaReceta(receta: RecipeModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = receta.image,
                contentDescription = receta.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Column(Modifier.padding(12.dp)) {
                Text(
                    text = receta.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${receta.cuisine} • ${receta.difficulty}",
                    color = Color.Gray
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = "Calificación",
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = " ${receta.rating} (${receta.reviewCount})  •  " +
                                "${receta.prepTimeMinutes + receta.cookTimeMinutes} min"
                    )
                }
            }
        }
    }
}