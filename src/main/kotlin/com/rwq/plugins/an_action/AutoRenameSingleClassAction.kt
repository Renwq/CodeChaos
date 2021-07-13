package com.rwq.plugins.an_action

import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.rwq.plugins.utils.NotificationUtils

/**
 * author： rwq
 * date: 2021/7/8 13:04
 * desc:
 */
class AutoRenameSingleClassAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val data = e.getData(PlatformDataKeys.VIRTUAL_FILE)
        if (data != null) {
            val psiFile = PsiManager.getInstance(e.project!!).findFile(data)
            if (psiFile != null) {
                val originalElement = psiFile.originalElement
                val psiClass = originalElement.children
                for (psiElement in psiClass) {
                    val equals = psiElement.javaClass.simpleName.equals("PsiClassImpl")
                    if(equals){
                        val autoRenameElementAction = AutoRenameElementAction()
                        val actionEvent =
                            AnActionEvent.createFromAnAction(autoRenameElementAction, null, "", e.dataContext)
                        autoRenameElementAction.actionPerformed2(actionEvent, psiElement)
                    }
                }

            } else {
                NotificationUtils.notifyAndHideMsg("错误", "查找此文件异常", NotificationType.ERROR)
            }
        } else {
            NotificationUtils.notifyAndHideMsg("操作错误", "请先选中类文件后，执行此次操作", NotificationType.ERROR)
        }
    }
}