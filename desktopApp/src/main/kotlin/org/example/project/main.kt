package org.example.project

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import api.NewsApiClient

fun main() = application {


    Window(
        onCloseRequest = ::exitApplication,
        title = "KotlinProject",
    ) {
        App()
    }
}