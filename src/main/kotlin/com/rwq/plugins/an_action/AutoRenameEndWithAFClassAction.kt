package com.rwq.plugins.an_action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.vfs.VirtualFile
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
        val indexSourceFile = indexSourceFile(data, e)
        println("indexSourceFile:$indexSourceFile")
    }

    companion object {
        @JvmStatic
        public fun indexSourceFile(moduleFile: VirtualFile?, e: AnActionEvent): Boolean {
            return if (moduleFile != null) {
                val src = findSourceFile(moduleFile, "src")
                if (src != null) {
                    val main = findSourceFile(src, "main")
                    if (main != null) {
                        val java = findSourceFile(main, "java")
                        if (java != null) {
                            renameThisFiles(java, e, null)
                            return true
                        }
                    }
                }
                false
            } else {
                false
            }
        }

        @JvmStatic
        public fun findSourceFile(children: VirtualFile?, fileName: String): VirtualFile? {
            if (children != null) {
                for (itemFile in children.children) {
                    if (itemFile.name == fileName) {
                        return itemFile
                    }
                }
                return null
            } else {
                return null
            }
        }

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