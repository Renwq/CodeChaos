package com.rwq.plugins.an_action

import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.rwq.plugins.an_action.AutoRenameAllClassAction.Companion.indexSourceFile
import com.rwq.plugins.an_action.AutoRenameAllClassAction.Companion.renameThisFiles
import com.rwq.plugins.utils.NotificationUtils

/**
 * author： rwq
 * date: 2021/7/13 12:14
 * desc:
 */
class AutoRenameDirClassAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        println("rename all class file name")
        val data = e.getData(CommonDataKeys.VIRTUAL_FILE)
        if (data == null) {
            NotificationUtils.notifyAndHideMsg("警告", "请选中一个目录！", NotificationType.WARNING)
            return
        }
        val indexSourceFile = renameThisFiles(data, e)
        println("indexSourceFile:$indexSourceFile")
    }
}