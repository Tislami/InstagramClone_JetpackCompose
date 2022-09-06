package com.zeroone.instagramclone_jetpackcompose.presentation.screen.appbar

import android.widget.NumberPicker
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zeroone.instagramclone_jetpackcompose.R
import com.zeroone.instagramclone_jetpackcompose.presentation.ui.appcomponents.AppSearchTextField
import com.zeroone.instagramclone_jetpackcompose.presentation.ui.appcomponents.AppText

@Composable
fun DiscoveryPostTopBar(backOnClick: () -> Unit) {
    TopAppBar(
        elevation = 1.dp,
        backgroundColor = MaterialTheme.colors.surface,
        navigationIcon = {
            IconButton(
                onClick = backOnClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        },
        title = {
            AppText(text = stringResource(id = R.string.explore))
        },
    )
}