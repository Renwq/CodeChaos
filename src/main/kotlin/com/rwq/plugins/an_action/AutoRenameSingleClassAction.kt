package com.rwq.plugins.an_action

import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.source.PsiClassImpl
import com.rwq.plugins.an_action.AutoRenameAllClassAction.Companion.renameThisFiles
import com.rwq.plugins.utils.NotificationUtils
import com.rwq.plugins.utils.RenameOption

/**
 * author： rwq
 * date: 2021/7/8 13:04
 * desc:
 */
class AutoRenameSingleClassAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val data = e.getData(PlatformDataKeys.VIRTUAL_FILE)
        if (data != null) {
            val renameOption = RenameOption(
                isRenameClassName = true,
                isRenameProperty = true,
                isRenameMethod = true,
                isRenameLocalVariable = true,
                isAddInChaosCode = true
            )
            renameThisFiles(data, e, renameOption)
        } else {
            NotificationUtils.notifyAndHideMsg("操作错误", "请先选中类文件后，执行此次操作", NotificationType.ERROR)
        }
    }
}