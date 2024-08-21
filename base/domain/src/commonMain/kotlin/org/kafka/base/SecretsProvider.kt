package org.kafka.base

interface SecretsProvider {
    val googleServerClientId: String?
    val pipelessAuthToken: String?
    val openAiApiKey: String?
}
