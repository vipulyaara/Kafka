package com.kafka.data.feature.ai

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.kafka.base.SecretsProvider
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Singleton
class OpenAiRepository @Inject constructor(secretsProvider: SecretsProvider) {
    private val openai = OpenAI(
        token = secretsProvider.openAiApiKey.orEmpty(),
        timeout = Timeout(socket = 60.seconds)
    )

    fun observerSummary(
        title: String,
        author: String? = null,
        language: String? = null,
    ): Flow<SummaryResponse> {
        var result = ""

        val completionRequest = ChatCompletionRequest(
            model = model,
            temperature = 0.5,
            messages = listOf(
                ChatMessage(role = ChatRole.System, content = systemMessage),
                ChatMessage(role = ChatRole.User, content = summaryPrompt(title, author, language))
            ),
        )

        return openai.chatCompletions(completionRequest)
            .onEach { result += it.choices.firstOrNull()?.delta?.content.orEmpty() }
            .map { SummaryResponse(result, it.choices.firstOrNull()?.finishReason != null) }
    }

    private val model = ModelId("gpt-4o")

    private val systemMessage = """
        You are a highly skilled literary analyst. Your task is to create detailed and comprehensive summaries of books. 
        Ensure that the summaries capture all essential plot points, character arcs, and themes while remaining concise. 
        The goal is to provide a summary that gives readers a full understanding of the book’s content without needing to read the entire text.
        The text length should be more than 2000 words.
    """.trimIndent()

    private fun summaryPrompt(title: String, author: String? = null, language: String? = null) = """
        Please write a 2,000-word summary of the book **$title** by author $author (author can be empty or wrong) in $language language.
            1. Start with an introduction that sets the stage for the story.
            2. Provide a detailed summary of each key chapter, focusing on the main events.
            3. Describe the main characters, their motivations, and their arcs throughout the story.
            4. Discuss the major themes and how they are explored in the book.
            5. Conclude with the resolution of the story and its overall impact.

        Each section should contribute to a detailed, comprehensive summary that takes about 10 minutes to read.
        Use beautiful markdown but do not show title and author on top in big heading.
    """.trimIndent()

}

data class SummaryResponse(val content: String, val finished: Boolean)
