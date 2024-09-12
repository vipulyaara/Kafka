package org.kafka.base

interface SecretsProvider {
    val googleServerClientId: String?
    val openAiApiKey: String?
}
