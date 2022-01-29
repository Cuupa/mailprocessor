package com.cuupa.mailprocessor.services.semantic

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import java.lang.reflect.Type
import java.util.*

/**
 * Calls the extern semantic serivce to classify the text
 */
class ExternSemanticService(private val restTemplate: RestTemplate, private val semanticUrl: String) {

    private val gson = Gson()

    fun getSemanticResult(plainText: String?): List<SemanticResult> {
        return try {
            val responseEntity = restTemplate.postForEntity(semanticUrl, plainText, String::class.java)
            return when {
                responseEntity.statusCode.is2xxSuccessful -> gson.fromJson(responseEntity.body, listType)
                else -> listOf()
            }
        } catch (exception: ResourceAccessException) {
            listOf(SemanticResult("OTHER", "UNKNOWN"))
        }
    }

    companion object{
        val listType: Type = object : TypeToken<ArrayList<SemanticResult?>?>() {}.type
    }
}