package com.rwq.plugins.an_action

import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.rwq.plugins.utils.ChaosUtils
import com.rwq.plugins.utils.NotificationUtils

/**
 * author： rwq
 * date: 2021/7/8 14:44
 * desc:
 */
class ClearCacheKeysAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        ChaosUtils.clearKeyCache()
        NotificationUtils.notifyMsg("提示","清除成功！",NotificationType.INFORMATION)
    }
}