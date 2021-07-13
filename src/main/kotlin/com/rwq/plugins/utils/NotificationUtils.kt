package com.rwq.plugins.utils

import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications

/**
 * author： rwq
 * date: 2021/7/8 14:03
 * desc: 弹窗工具类
 **/
public class NotificationUtils {

    companion object{
        @JvmStatic
        fun notifyAndHideMsg(title:String,content:String,notificationType:NotificationType) {
            val createIdWithTitle = NotificationGroup.createIdWithTitle("error", title)
            val notification = Notification(createIdWithTitle, null, notificationType)
            notification.setContent(content)
            notification.setTitle(title)
            Notifications.Bus.notify(notification)
        }

        fun notifyMsg(title:String,content:String,notificationType:NotificationType) {
            val createIdWithTitle = NotificationGroup.createIdWithTitle("error", title)
            val notification = Notification(createIdWithTitle, null, notificationType)
            notification.setContent(content)
            notification.setTitle(title)
            Notifications.Bus.notify(notification)
        }
    }
}