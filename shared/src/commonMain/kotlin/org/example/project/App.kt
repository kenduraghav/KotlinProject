package org.example.project

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import api.ItemResponse
import kotlinx.coroutines.launch
import org.example.project.github.SearchTopReposQuery
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(viewModel: MainViewModel = koinViewModel()) {

    val scope = rememberCoroutineScope()

    MaterialTheme {

        val state = viewModel.uiState.collectAsState()

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = {
                scope.launch {
                    viewModel.getTopStories()
                }
            }) {
                Text("Click me!")
            }

            Button(onClick = {
                scope.launch {
                    viewModel.getTopRepositories()
                }
            }) {
                Text("Show Top 10 Github Repos")
            }

            AnimatedContent(state.value.showContent) { showContent ->

                when (showContent) {
                    ShowContent.HN -> HNContent(state.value.topStories)
                    ShowContent.GITHUB -> GithubContent(state.value.topRepos)
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun HNContent(state: List<ItemResponse.Story>) {
    LazyColumn(Modifier.padding(horizontal = 15.dp)) {
        item {
            Text("Top 10 HN Stories: ", fontSize = 30.sp)
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))
        }

        items(state) {
            Text(it.title.orEmpty())
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun GithubContent(state: List<SearchTopReposQuery.Repo>) {
    LazyColumn(Modifier.padding(horizontal = 15.dp)) {
        item {
            Text("Top 10 Github Repos: ", fontSize = 30.sp)
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))
        }

        items(state) {
            val repo = it.repo?.onRepository
            Text("${repo?.name.orEmpty()} ${repo?.stargazerCount} stars")
            Spacer(Modifier.height(8.dp))
        }
    }
}
