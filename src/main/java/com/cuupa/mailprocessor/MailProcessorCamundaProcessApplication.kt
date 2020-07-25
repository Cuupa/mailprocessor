package com.cuupa.mailprocessor

import org.apache.juli.logging.LogFactory
import org.camunda.bpm.application.PostDeploy
import org.camunda.bpm.application.ProcessApplication
import org.camunda.bpm.spring.boot.starter.SpringBootProcessApplication

@ProcessApplication
class MailProcessorCamundaProcessApplication : SpringBootProcessApplication() {
    @PostDeploy
    fun started() {
        printBanner()
    }

    private fun printBanner() {
        var banner = "\n"
        banner += """|  __ \                           |  ____|           (_)           
| |__) | __ ___   ___ ___  ___ ___| |__   _ __   __ _ _ _ __   ___ 
|  ___/ '__/ _ \ / __/ _ \/ __/ __|  __| | '_ \ / _` | | '_ \ / _ \
| |   | | | (_) | (_|  __/\__ \__ \ |____| | | | (_| | | | | |  __/
|_|   |_|  \___/ \___\___||___/___/______|_| |_|\__, |_|_| |_|\___|
                                                 __/ |             
                                                |___/              
     _             _           _ 
    | |           | |         | |
 ___| |_ __ _ _ __| |_ ___  __| |
/ __| __/ _` | '__| __/ _ \/ _` |
\__ \ || (_| | |  | ||  __/ (_| |
|___/\__\__,_|_|   \__\___|\__,_|"""
        LOG.debug(banner)
    }

    companion object {
        private val LOG = LogFactory.getLog(MailProcessorCamundaProcessApplication::class.java)
    }
}