package com.gitlab.hopebaron.rest.request

import io.ktor.client.response.HttpResponse

class RequestException internal constructor(val response: HttpResponse) : Exception(
        "Request(method=${response.call.request.method.value}, url=${response.call.request.url}, body=${response.call.request}) returned ${response.status}"
)