package com.cuupa.mailprocessor.services.dmn

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.camunda.bpm.engine.RepositoryService
import org.springframework.scheduling.annotation.Scheduled
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import javax.annotation.PostConstruct

class DmnDeployService(
    private val repositoryService: RepositoryService,
    private val path: String?
) {

    private var lastDeployed = 0L

    @Scheduled(fixedDelay = `5minutes`)
    @PostConstruct
    fun cron() {
        log.error("Deploying dmns ...")
        try {
            loadAndDeploy()
        } catch(e: Exception){
            log.error("Failed to deploy DMNs at $path", e)
        }
    }

    private fun loadAndDeploy() {
        if (path != null) {
            val dmns = Files.list(Paths.get(path)).collect(Collectors.toList())
                .map { it.toFile() }
                .filter { it.name.endsWith(".dmn") }
                .filter { it.lastModified() > lastDeployed }

            dmns.forEach {
                repositoryService.createDeployment()
                    .addInputStream(it.name, FileInputStream(it))
                    .name(it.name)
                    .deploy()
                log.info("Successfully deployed '${it.name}'")
            }
            lastDeployed = System.currentTimeMillis()
        }
    }

    companion object{
        private val log: Log = LogFactory.getLog(DmnDeployService::class.java)
        private const val `5minutes` = 300000L
    }
}
