package com.example.lab09

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
fun ScreenReceta(navController: NavHostController, servicio: RecipeApiService, id: Int) {
    var receta by remember { mutableStateOf<RecipeModel?>(null) }
    var hayError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            receta = servicio.getRecipeById(id)
        } catch (e: Exception) {
            hayError = true
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF3E0))
    ) {
        val r = receta
        when {
            hayError -> Text("No se pudo cargar la receta", Modifier.padding(16.dp))
            r == null -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFFE65100)
            )
            else -> Column(Modifier.verticalScroll(rememberScrollState())) {
                AsyncImage(
                    model = r.image,
                    contentDescription = r.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )

                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(r.name, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                    Text("${r.cuisine} • ${r.difficulty} • ${r.mealType.joinToString(", ")}", color = Color.Gray)

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        DatoReceta("Preparación", "${r.prepTimeMinutes} min")
                        DatoReceta("Cocción", "${r.cookTimeMinutes} min")
                        DatoReceta("Porciones", "${r.servings}")
                        DatoReceta("Calorías", "${r.caloriesPerServing}")
                    }

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Ingredientes", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            r.ingredients.forEach { Text("• $it") }
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Instrucciones", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            r.instructions.forEachIndexed { i, paso ->
                                Text("${i + 1}. $paso", Modifier.padding(bottom = 6.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DatoReceta(titulo: String, valor: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(valor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(titulo, color = Color.Gray, fontSize = 12.sp)
    }
}