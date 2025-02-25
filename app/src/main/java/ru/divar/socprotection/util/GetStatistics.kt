package ru.divar.socprotection.util

import ru.divar.socprotection.data.SocialResponse

class GetStatistics {
    fun get(): SocialResponse =
        APIUtils.get("statistics")
}