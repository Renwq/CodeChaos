package com.rwq.plugins.an_action

import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.ui.components.DialogManager
import com.rwq.plugins.an_action.AutoRenameAllClassAction.Companion.renameThisFiles
import com.rwq.plugins.utils.NotificationUtils.Companion.notifyAndHideMsg

/**
 * author： rwq
 * date: 2021/7/14 11:05
 * desc:
 */
class AutoRenameDirSCContentAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val data = e.getData(PlatformDataKeys.VIRTUAL_FILE)
        if (data == null || !data.isDirectory) {
            notifyAndHideMsg("警告", "请选中要更改的文件夹！", NotificationType.WARNING)
        } else {
            val indexSourceFile = renameThisFiles(data, e, null)
            println("indexSourceFile:$indexSourceFile")
        }
    }
}