package com.kafka.base

interface SecretsProvider {
    val googleServerClientId: String?
    val openAiApiKey: String?
    val supabaseUrl: String
    val supabaseKey: String
    val mixpanelToken: String?
}
