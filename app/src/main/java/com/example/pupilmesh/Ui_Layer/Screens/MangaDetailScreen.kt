package com.example.pupilmesh.Ui_Layer.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.pupilmesh.Common.State.State
import com.example.pupilmesh.Domain_Layer.Modal.Data
import com.example.pupilmesh.Ui_Layer.Viewmodel.PupilVm


@Composable
fun MangaDetailScreen(data:Data) {

            if (data != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(data.thumb),
                        contentDescription = "Thumbnail",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = data.title ?:"",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = data.sub_title ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Status: ${data.status}")
                    Text(text = "Type: ${data.type}")
                    Text(text = "Chapters: ${data.total_chapter}")
                    Text(text = "NSFW: ${if (data.nsfw) "Yes" else "No"}")

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Summary:",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(text = data.summary ?: "No summary available.")
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Something went wrong. Please try again later.")
                }
            }

}
