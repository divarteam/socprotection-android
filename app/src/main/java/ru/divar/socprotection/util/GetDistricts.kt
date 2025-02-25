package ru.divar.socprotection.util

import ru.divar.socprotection.data.SocialResponse

class GetDistricts {
    fun get(): SocialResponse =
        APIUtils.get("districts")
}