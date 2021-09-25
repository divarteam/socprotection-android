package ru.divar.socprotection.util

class APIConfig {
    companion object {
        private const val connectionType = "http"
        private const val domain = "31.172.66.208:8080"
        const val url = "$connectionType://$domain/"
    }
}