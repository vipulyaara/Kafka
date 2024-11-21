package com.kafka.data.feature.ai

import com.aallam.openai.api.chat.ChatCompletionChunk
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.kafka.base.ApplicationScope
import com.kafka.base.SecretsProvider
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.seconds

@Inject
@ApplicationScope
class OpenAi(secretsProvider: SecretsProvider) {
    private val openai = OpenAI(
        token = secretsProvider.openAiApiKey.orEmpty(),
        timeout = Timeout(socket = 60.seconds)
    )

    private val model = ModelId("gpt-4o")

    data class Request(val systemMessage: String, val userMessage: String)

    fun chatCompletions(request: Request): Flow<ChatCompletionChunk> {
        val completionRequest = ChatCompletionRequest(
            model = model,
            temperature = 0.5,
            messages = listOf(
                ChatMessage(role = ChatRole.System, content = request.systemMessage),
                ChatMessage(role = ChatRole.User, content = request.userMessage)
            ),
        )

        return openai.chatCompletions(completionRequest)
    }
}
