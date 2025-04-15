package com.example.pupilmesh.Ui_Layer.Screens

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.pupilmesh.Common.State.State
import com.example.pupilmesh.Domain_Layer.Modal.Data
import com.example.pupilmesh.Domain_Layer.Modal.Manga
import com.example.pupilmesh.Ui_Layer.Utlis.MangaCard
import com.example.pupilmesh.Ui_Layer.Viewmodel.PupilVm
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions


@Composable
fun HomeScreen(viewModel: PupilVm, GetContent: (Data) -> Unit) {
    var selectedIndex by remember { mutableStateOf(0) }
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Anime") },
                    label = { Text("Manga") },
                    selected = selectedIndex == 0,
                    onClick = { selectedIndex = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Camera") },
                    label = { Text("Camera") },
                    selected = selectedIndex == 1,
                    onClick = { selectedIndex = 1 }
                )
            }
        }
    ) { it ->
        if (selectedIndex == 0) {
            MangaScreen(viewModel ,GetContent)
        } else {
            CameraScreen()
        }
    }
}
@Composable
fun MangaScreen(viewModel: PupilVm, GetContent: (Data) -> Unit) {
    val mangaState by viewModel.mangaState.collectAsState()
    val listState = rememberLazyListState()

    var allMangaData by remember { mutableStateOf<List<Data>>(emptyList()) }
    var currentPage by remember { mutableStateOf(1) }
    var isLoadingMore by remember { mutableStateOf(false) }
    var hasMore by remember { mutableStateOf(true) }

    LaunchedEffect(currentPage) {
        isLoadingMore = true
        viewModel.fetchManga(currentPage)
    }

    LaunchedEffect(mangaState) {
        when (mangaState) {
            is State.Success -> {
                val response = (mangaState as State.Success<Manga>).data
                val newList = response?.data ?: emptyList()
                if (newList.isEmpty()) {
                    hasMore = false
                } else {
                    allMangaData = allMangaData + newList
                }
                isLoadingMore = false
            }
            is State.Error, is State.Loading -> {
                isLoadingMore = false
            }
        }
    }

    if (allMangaData.isNotEmpty()) {
        LazyColumn(state = listState) {
            items(allMangaData) { mangaData ->
                MangaCard(mangaData,GetContent)
            }

            item {
                if (isLoadingMore) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }

        LaunchedEffect(listState) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .collect { index ->
                    if (index == allMangaData.lastIndex && !isLoadingMore && hasMore) {
                        currentPage++
                    }
                }
        }
    } else {
        when (mangaState) {
            is State.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is State.Error -> {
                val msg = (mangaState as State.Error).message ?: "Error occurred"
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = msg, color = Color.Red)
                }
            }
            else -> Unit
        }
    }
}


@OptIn(ExperimentalGetImage::class)
@Composable
fun CameraScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var permissionGranted by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
        if (!isGranted) {
            Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    val faceDetector = remember {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .build()
        FaceDetection.getClient(options)
    }

    if (permissionGranted) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy ->
                        val mediaImage = imageProxy.image
                        if (mediaImage != null) {
                            val inputImage = InputImage.fromMediaImage(
                                mediaImage,
                                imageProxy.imageInfo.rotationDegrees
                            )

                            faceDetector.process(inputImage)
                                .addOnSuccessListener { faces ->
                                    for (face in faces) {
                                        Log.d("FaceDetection", "Face bounds: ${face.boundingBox}")
                                    }
                                }
                                .addOnFailureListener {
                                    Log.e("FaceDetection", "Face detection failed", it)
                                }
                                .addOnCompleteListener {
                                    imageProxy.close()
                                }
                        } else {
                            imageProxy.close()
                        }
                    }

                    val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )

                }, ContextCompat.getMainExecutor(ctx))

                previewView
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}





