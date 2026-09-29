package api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class NewsApiClient(private val client: HttpClient) {

    suspend fun getTopStories() : List<ItemResponse.Story> {

        val storyIds: List<Long>  = client
            .get("https://hacker-news.firebaseio.com/v0/topstories.json").body()

        return storyIds
            .take(10)
            .mapNotNull { id ->

                when(val response = getStory(id)){

                    is ItemResponse.Story -> response
                    ItemResponse.NullResponse -> null
                }

            }

    }

    private suspend fun getStory(id: Long) : ItemResponse {
        return client
            .get ("https://hacker-news.firebaseio.com/v0/item/$id.json")
            .body()
    }
}