package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.services.files.transfer.TransferProtocolFacade
import com.cuupa.mailprocessor.userconfiguration.WorkDirectory
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.util.Assert

abstract class AbstractJavaDelegate : JavaDelegate {

    protected fun getContent(id: String?, workConfig: WorkDirectory?): ByteArray {
        Assert.notNull(id, "ID must not be null")
        Assert.notNull(workConfig, "workConfig must not be null")
        Assert.notNull(workConfig?.path, "workConfig.path must not be null")

        TransferProtocolFacade.getForPath(workConfig?.path!!)
            .init(workConfig.username, workConfig.password).use { file ->
                return file.get(workConfig.path!!, id!!).readAllBytes()
            }
    }

    protected fun writeContent(id: String?, content: ByteArray?, workConfig: WorkDirectory?) {
        Assert.notNull(id, "ID must not be null")
        Assert.notNull(workConfig, "workConfig must not be null")
        Assert.notNull(workConfig?.path, "workConfig.path must not be null")
        Assert.notNull(content, "content must not be null")

        TransferProtocolFacade.getForPath(workConfig?.path!!)
            .init(workConfig.username, workConfig.password).use { file ->
                file.createDirectory(workConfig.path!!)
                file.save(workConfig.path!!, id!!, content!!)
            }
    }
}
