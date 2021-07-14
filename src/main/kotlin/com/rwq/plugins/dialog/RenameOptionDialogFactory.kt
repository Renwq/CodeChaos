package com.rwq.plugins.dialog

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Condition
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import java.awt.event.ActionEvent

/**
 * author： rwq
 * date: 2021/7/14 11:26
 * desc:
 */
class RenameOptionDialogFactory : ToolWindowFactory, Condition<Project?> {
    /**
     * 创建 tool window
     * @param project
     * @param toolWindow
     */
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = AskRenameOptionDialog(toolWindow, { ok: ActionEvent? -> }) { cancel: ActionEvent? -> }
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(myToolWindow.content, "自定义tool window", false)
        toolWindow.contentManager.addContent(content)
    }

    /**
     * 控制tool window是否展示
     * @param project
     * @return
     */
    override fun value(project: Project?): Boolean {
        return true
    }
}