package ru.divar.socprotection.util

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import ru.divar.socprotection.data.SocialResponse

class GetStatistics {
    fun get(): SocialResponse =
        runBlocking {
            GlobalScope.async {
                APIUtils.get("statistics")
            }.await()
        }
}