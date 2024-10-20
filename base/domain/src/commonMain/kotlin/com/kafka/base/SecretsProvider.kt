package com.kafka.base

interface SecretsProvider {
    val googleServerClientId: String?
    val openAiApiKey: String?

    val supabaseUrl
        get() = "https://kkeosgnragzpgsbaocjl.supabase.co"

    //todo: see if this needs to secured
    val supabaseKey
        get() = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtrZW9zZ25yYWd6cGdzYmFvY2psIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mjg3NTI5OTQsImV4cCI6MjA0NDMyODk5NH0.UzrJfoq5-0PowWWlCbZe_7jEgPlwL8dEgcvn-14dJZY"
}

enum class Service {
    Archive, Supabase
}

val appService = Service.Supabase
val appRecentItems = Service.Archive

enum class Auth {
    Firebase, Supabase
}

val appAuth = Service.Supabase
