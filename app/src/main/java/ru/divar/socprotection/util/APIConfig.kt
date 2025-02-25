package ru.divar.socprotection.util

class APIConfig {
    companion object {
        private const val connectionType = "https"
        private const val domain = "soc-protect-udmurt.arpakit.com"
        const val url = "$connectionType://$domain/"
    }
}