package com.rwq.plugins.utils

import com.rwq.plugins.services.MyProjectService
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.charset.Charset

/**
 * author： rwq
 * date: 2021/7/15 15:02
 * desc: 本地数据工具
 **/
class LocalDataUtils {
    companion object {
        const val KEY_RENAME_OPTION: String = "KEY_RENAME_OPTION"

        fun getData(key: String): String? {
            val keyFile = File(MyProjectService.staticProject?.basePath + File.separator + "ChaosCode", key)
            if (!keyFile.exists()) {
                return null
            }
            val fileInputStream = FileInputStream(keyFile)
            val readAllBytes = fileInputStream.readBytes()
            fileInputStream.close()
            return String(readAllBytes, Charset.forName("utf-8"))
        }

        fun saveData(key: String, content: String) {
            val keyFile = File(MyProjectService.staticProject?.basePath + File.separator + "ChaosCode", key)
            if (!keyFile.exists()) {
                keyFile.createNewFile()
            }
            val fileOutputStream = FileOutputStream(keyFile)
            fileOutputStream.write(content.toByteArray(Charset.forName("utf-8")))
            fileOutputStream.close()
        }


    }
}