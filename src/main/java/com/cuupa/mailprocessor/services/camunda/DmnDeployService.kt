package com.cuupa.mailprocessor.services.camunda

import org.camunda.bpm.engine.RepositoryService
import org.springframework.scheduling.annotation.Scheduled
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

class DmnDeployService(
    private val repositoryService: RepositoryService,
    private val path: String?
) {

    private var lastDeployed = 0L

    init {
        loadAndDeploy()
    }

    @Scheduled(cron = "0 */5 * * * *")
    fun cron() {
        loadAndDeploy()
    }

    private fun loadAndDeploy() {
        if (path != null) {
            val files = Files.list(Paths.get(path)).collect(Collectors.toList())
            val dmns = files.map { it.toFile() }
                .filter { it.name.endsWith(".dmn") }
                .filter { it.lastModified() > lastDeployed }

            dmns.forEach {
                repositoryService.createDeployment()
                    .addInputStream(it.name, FileInputStream(it))
                    .deploy()
            }
            lastDeployed = System.currentTimeMillis()
        }
    }
}
