package com.rwq.plugins.an_action

import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.rwq.plugins.an_action.AutoRenameAllClassAction.Companion.renameThisFiles
import com.rwq.plugins.dialog.RenameOptionDialogWrapper
import com.rwq.plugins.utils.NotificationUtils
import com.rwq.plugins.utils.RenameOption

/**
 * author： rwq
 * date: 2021/7/14 11:05
 * desc:
 */
class AutoRenameSCContentAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val data = e.getData(PlatformDataKeys.VIRTUAL_FILE)
        if (data != null) {
            val renameOptionDialog = RenameOptionDialogWrapper()
            renameOptionDialog.show()
            /*val renameOption = RenameOption(
                isRenameClassName = false,
                isRenameProperty = false,
                isRenameMethodPar = true,
                false,
                isRenameLocalVariable = true,
                isAddInChaosCode = false
            )
            renameThisFiles(data, e, renameOption)*/
        } else {
            NotificationUtils.notifyAndHideMsg("操作错误", "请先选中类文件后，执行此次操作", NotificationType.ERROR)
        }
    }


}