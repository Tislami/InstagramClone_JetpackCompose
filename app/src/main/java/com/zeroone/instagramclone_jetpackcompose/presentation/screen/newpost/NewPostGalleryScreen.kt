package com.zeroone.instagramclone_jetpackcompose.presentation.screen.newpost

import android.content.Context
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.zeroone.instagramclone_jetpackcompose.presentation.ui.appbar.AddTopBar
import com.zeroone.instagramclone_jetpackcompose.presentation.screen.main.AppState
import com.zeroone.instagramclone_jetpackcompose.presentation.screen.navigation.Add
import com.zeroone.instagramclone_jetpackcompose.presentation.screen.navigation.Graph
import com.zeroone.instagramclone_jetpackcompose.presentation.screen.navigation.Screens
import com.zeroone.instagramclone_jetpackcompose.presentation.ui.Loading
import java.io.File
import java.io.FileInputStream

@Composable
fun AddScreen(
    appState: AppState,
    newPostViewModel: NewPostViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val navController by remember { mutableStateOf(appState.navHostController) }
    val imageFile = remember { mutableStateOf("") }
    LaunchedEffect(key1 = "New_Post") {
        newPostViewModel.eventFlow.collect { uiEvent ->
            when (uiEvent) {
                is NewPostViewModel.UIEvent.Error -> {
                    Log.d("PostApp", "AddScreen: error event ${uiEvent.message}")
                    appState.showSnackBar(uiEvent.message)
                }
                NewPostViewModel.UIEvent.PhotoAdded -> {
                    Log.d("PostApp", "AddScreen: success event ")
                    navController.navigate(Add.NewPostScreen.route)
                }
            }
        }
    }

    Scaffold(
        scaffoldState = appState.scaffoldState,
        topBar = {
            AddTopBar(
                cancelOnClick = { navController.navigate(Screens.Home.route) },
                forwardOnClick = {
                    Log.d("PostApp", "AddScreen: forward Clicked")
                    newPostViewModel.onEvent(
                        NewPostEvent.SetPhoto(FileInputStream(imageFile.value))
                    )
                }
            )
        },
        content = { GridView(context = context, imageFile = imageFile) },
    )

    Loading(isLoading = newPostViewModel.isLoading.value)

}

@Composable
fun GridView(
    context: Context,
    imageFile: MutableState<String>
) {
    val imgList by remember { mutableStateOf(getImagePath(context)) }

    Column(Modifier.fillMaxSize()) {

        Surface(
            modifier = Modifier
                .weight(.6f)
                .fillMaxSize(),
        ) {
            AsyncImage(
                model = File(imageFile.value),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }


        LazyVerticalGrid(
            columns = GridCells.Adaptive(125.dp),
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .weight(.4f)

        ) {
            items(imgList.size) {
                Surface(
                    modifier = Modifier
                        .size(125.dp)
                        .clickable {
                            imageFile.value = imgList[it]
                        }
                        .border(BorderStroke(2.dp, MaterialTheme.colors.secondaryVariant)),
                    shape = RectangleShape,
                    elevation = 0.dp,
                ) {

                    AsyncImage(
                        alignment = Alignment.Center,
                        model = File(imgList[it]),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
    }
}