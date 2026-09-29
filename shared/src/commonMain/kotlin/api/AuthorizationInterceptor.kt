package api

import com.apollographql.apollo.api.http.HttpRequest
import com.apollographql.apollo.api.http.HttpResponse
import com.apollographql.apollo.network.http.HttpInterceptor
import com.apollographql.apollo.network.http.HttpInterceptorChain

class AuthorizationInterceptor () : HttpInterceptor {
    override suspend fun intercept(
        request: HttpRequest,
        chain: HttpInterceptorChain
    ): HttpResponse {

        val token = "ghp_token"

        return chain.proceed(request.newBuilder().addHeader("Authorization","Bearer $token").build())
    }
}