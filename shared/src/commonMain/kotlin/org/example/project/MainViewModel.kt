package org.example.project

import androidx.lifecycle.ViewModel
import api.AuthorizationInterceptor
import api.ItemResponse
import api.NewsApiClient
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.http.LoggingInterceptor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.project.github.SearchTopReposQuery

class MainViewModel(
    private val newsApiClient: NewsApiClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState(ShowContent.NONE,emptyList(), topRepos = emptyList()))

    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    suspend fun getTopStories() {
        val topStories = newsApiClient.getTopStories()

        println("Top Stories are ${topStories.map {it.title}}")

        _uiState.update {
            it.copy(
                showContent = ShowContent.HN,
                topStories = topStories
            )
        }
    }

    suspend fun  getTopRepositories(){
        val apolloClient = ApolloClient.Builder()
            .serverUrl("https://api.github.com/graphql")
            .httpInterceptors(listOf(AuthorizationInterceptor(), LoggingInterceptor()))
            .build()
        val topRepos = apolloClient.query(SearchTopReposQuery()).execute().dataAssertNoErrors.search
        _uiState.update {
            it.copy(
                showContent = ShowContent.GITHUB,
                topRepos = topRepos.reposFilterNotNull() ?: emptyList()
            )
        }
    }
}

data class  UiState(val showContent : ShowContent,
                    val topStories: List<ItemResponse.Story>,
                    val topRepos: List<SearchTopReposQuery.Repo>)


enum class ShowContent {
    NONE, HN, GITHUB
}