package com.cuupa.mailprocessor.configuration

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.camunda.bpm.engine.test.Deployment
import org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat
import org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.processEngine
import org.camunda.bpm.engine.test.mock.Mocks
import org.camunda.bpm.engine.variable.Variables
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ProcessEngineExtension::class)
open class MailprocessorTest {

    private val runtimeService = processEngine().runtimeService

    @BeforeAll
    fun setup() {
        Mocks.register("loggerDelegate", object : JavaDelegate {
            override fun execute(p0: DelegateExecution?) {
            }
        })
    }

    @Test
    @Deployment(resources = ["mailprocessor.bpmn", "defaultActivity.bpmn"])
    fun testHappyPath() {
        Variables.createVariables().putValue("ERROR", false)
        val instance = runtimeService.startProcessInstanceByKey("mailprocessor")
        assertThat(instance).hasPassed("Event_successfull")
    }
}