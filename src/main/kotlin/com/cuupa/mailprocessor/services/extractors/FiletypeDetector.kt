package com.cuupa.mailprocessor.services.extractors

import com.cuupa.mailprocessor.services.Extensions.isNullOrEmpty

object FiletypeDetector {

    fun isPdf(content: ByteArray?): Boolean {
        if (content.isNullOrEmpty()) {
            return false
        }

        return content!!.size > 4
                && isPdfHeaderValid(content)
                //&& isPdfEOFValid(content)
    }

    private fun isPdfEOFValid(fileContent: ByteArray): Boolean {
        //val F = fileContent[fileContent.size -1]
        return true
    }

    // header %PDF-
    private fun isPdfHeaderValid(
        fileContent: ByteArray
    ): Boolean {
        return fileContent[0].toInt() == 0x25
                && fileContent[1].toInt() == 0x50
                && fileContent[2].toInt() == 0x44
                && fileContent[3].toInt() == 0x46
                && fileContent[4].toInt() == 0x2D
    }

    fun isTiff(content: ByteArray?): Boolean {
        if (content.isNullOrEmpty()){
            return false
        }
        return isTiffLittleEndian(content!!)
            .or(isTiffBigEndian(content))
    }

    private fun isTiffBigEndian(content: ByteArray): Boolean {
        return try {
            content[0].toInt() == 0x4D
                    && content[1].toInt() == 0x4D
                    && content[2].toInt() == 0x00
                    && content[3].toInt() == 0x2A
        } catch (iobe: IndexOutOfBoundsException) {
            false
        }
    }

    private fun isTiffLittleEndian(content: ByteArray): Boolean {
        return try {
            content[0].toInt() == 0x49
                    && content[1].toInt() == 0x49
                    && content[2].toInt() == 0x2A
                    && content[3].toInt() == 0x00
        } catch (iobe: IndexOutOfBoundsException) {
            false
        }
    }

    fun isJpeg(content: ByteArray?): Boolean {

        if(content.isNullOrEmpty()){
            return false
        }

        return try {
            content!![0].toInt() == 0xFF
                    && content[1].toInt() == 0xD8
                    && content[2].toInt() == 0xFF
                    && content[3].toInt() == 0xE0
        } catch (iobe: IndexOutOfBoundsException) {
            false
        }
    }

    fun isJpeg2000(content: ByteArray?): Boolean {

        if(content.isNullOrEmpty()){
            return false
        }

        val is1 = try {
            content!![0].toInt() == 0x00
                    && content[1].toInt() == 0x00
                    && content[2].toInt() == 0x00
                    && content[3].toInt() == 0x0C
                    && content[4].toInt() == 0x6A
                    && content[5].toInt() == 0x50
                    && content[6].toInt() == 0x20
                    && content[7].toInt() == 0x20
                    && content[8].toInt() == 0x0D
                    && content[9].toInt() == 0x0A
                    && content[10].toInt() == 0x87
                    && content[11].toInt() == 0x01
        } catch (iobe: IndexOutOfBoundsException) {
            false
        }

        val is2 = try {
            content!![0].toInt() == 0xFF
                    && content[1].toInt() == 0x4F
                    && content[2].toInt() == 0xFF
                    && content[3].toInt() == 0x51
        } catch (iobe: IndexOutOfBoundsException) {
            false
        }

        return is1.or(is2)
    }
}