package com.zeroone.instagramclone_jetpackcompose.presentation.ui.appcomponents

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit


@Composable
fun AppTextButton(
    text: String,
    onClick: ()-> Unit = {},
    textAlign: TextAlign? = null,
    modifier : Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight?=null,
    color: Color = MaterialTheme.colors.primary
    ) {

    TextButton(onClick = onClick) {
        Text(
            text = text,
            color = color,
            modifier = modifier,
            textAlign = textAlign,
            fontSize = fontSize,
            fontWeight = fontWeight
        )
    }
}