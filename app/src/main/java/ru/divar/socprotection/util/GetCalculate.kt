package ru.divar.socprotection.util

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import ru.divar.socprotection.data.SocialResponse

class GetCalculate(
    private val age: Int,
    private val earnings: Int,
    private val disability: Boolean,
    private val childrenAmount: Int,
    private val isEngaged: Boolean,
    private val idDistrict: Int,
    private val sex: Int
) {
    fun get(): SocialResponse =
        runBlocking {
            GlobalScope.async {
                APIUtils.get("calculate?age=$age&earnings=$earnings&disability=$disability&children_amount=$childrenAmount&is_engaged=$isEngaged&id_district=$idDistrict&sex=$sex")
            }.await()
        }
}