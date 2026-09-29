package api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class NewsApiClient {

    private val client = HttpClient {

        install(ContentNegotiation){
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }


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

//    suspend fun getTodo(): Todo {
//        return client.get (
//            "https://jsonplaceholder.typicode.com/todos/1"
//        ).body()
//    }
}