package com.example.examfinal.views.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.examfinal.domain.CountryDetailDomain



@Composable
fun CountryDetailContent(country: CountryDetailDomain) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {

            AsyncImage(
                model = country.flagUrl,
                contentDescription = "Bandera de ${country.commonName}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Fit
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = country.commonName,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    DetailRow(label = "Nombre Oficial", value = country.officialName)
                    DetailRow(label = "Capital", value = country.capital)
                    DetailRow(label = "Población", value = country.population)
                    DetailRow(label = "Región", value = country.region)
                    DetailRow(label = "Subregión", value = country.subregion)
                    DetailRow(label = "Idiomas", value = country.languages)
                    DetailRow(label = "Zona Horaria", value = country.timezones)
                    DetailRow(label = "Área", value = country.area)
                }
            }
        }
    }
}
