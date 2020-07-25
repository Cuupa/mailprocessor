package com.cuupa.mailprocessor.services.semantic

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.springframework.web.client.RestTemplate
import java.util.*

/**
 * Calls the extern semantic serivce to classify the text
 */
class ExternSemanticService(private val restTemplate: RestTemplate, private val semanticUrl: String) {

    private val gson = Gson()

    fun getSemanticResult(plainText: String?): List<SemanticResult> {
        val responseEntity = restTemplate.postForEntity(semanticUrl, plainText, String::class.java)

        return if (responseEntity.statusCode.is2xxSuccessful) {
            val listType = object : TypeToken<ArrayList<SemanticResult?>?>() {}.type
            gson.fromJson(responseEntity.body, listType)
        } else {
            ArrayList()
        }
    }

}