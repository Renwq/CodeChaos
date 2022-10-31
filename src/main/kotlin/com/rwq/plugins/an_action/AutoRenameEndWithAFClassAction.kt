package com.rwq.plugins.an_action

import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.vfs.VirtualFile
import com.rwq.plugins.utils.NotificationUtils
import com.rwq.plugins.utils.RenameOption

/**
 * author： rwq
 * date: 2022-10-31 14:31
 * desc:改变以Activity 和 Fragment 为结尾的文件
 **/
class AutoRenameEndWithAFClassAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        println("rename all class file name")
        val data = e.getData(CommonDataKeys.VIRTUAL_FILE)
        if (data == null) {
            NotificationUtils.notifyAndHideMsg("警告", "请选中一个目录！", NotificationType.WARNING)
            return
        }
        val indexSourceFile = renameThisFiles(data, e, null)
        println("indexSourceFile:$indexSourceFile")
    }

    companion object {
        @JvmStatic
        public fun renameThisFiles(java: VirtualFile, e: AnActionEvent, renameOption: RenameOption?) {
            if (java.isDirectory) {
                for (item in java.children) {
                    if (item.isDirectory) {
                        renameThisFiles(item, e, renameOption)
                    } else {
                        if (item.name.endsWith("Activity.java") || item.name.endsWith("Fragment.java")) {
                            AutoRenameAllClassAction.doHandler(item, e, renameOption)
                        }
                    }
                }
            } else {
                if (java.name.endsWith("Activity.java") || java.name.endsWith("Fragment.java")) {
                    AutoRenameAllClassAction.doHandler(java, e, renameOption)
                }
            }
        }
    }
}