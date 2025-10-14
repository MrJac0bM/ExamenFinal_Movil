package com.example.examfinal.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.examfinal.domain.CountryDetailDomain
import com.example.examfinal.presentation.CountryViewModel
import com.example.examfinal.views.components.CountryDetailContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDetailScreen(
    navController: NavController,
    viewModel: CountryViewModel,
    countryName: String
) {
    val countryDetail by viewModel.countryDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(countryName) {
        viewModel.fetchCountryDetail(countryName)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearDetail()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del País") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                countryDetail != null -> {
                    CountryDetailContent(country = countryDetail!!)
                }
                else -> {
                    Text(
                        text = "No se pudo cargar la información",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

