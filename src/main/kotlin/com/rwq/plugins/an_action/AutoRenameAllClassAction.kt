package com.rwq.plugins.an_action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager


/**
 * author： rwq
 * date: 2021/7/7 11:21
 * desc:
 */
class AutoRenameAllClassAction : AnAction() {


    override fun actionPerformed(e: AnActionEvent) {
        println("rename all class file name")
        val data = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val indexSourceFile = indexSourceFile(data,e)
        println("indexSourceFile:$indexSourceFile")

    }

    private fun indexSourceFile(moduleFile: VirtualFile?, e: AnActionEvent): Boolean {
        return if (moduleFile != null) {
            val src = findSourceFile(moduleFile, "src")
            if (src != null) {
                val main = findSourceFile(src, "main")
                if (main != null) {
                    val java = findSourceFile(main, "java")
                    if (java != null) {
                        renameThisFiles(java, e)
                        return true
                    }
                }
            }
            false
        } else {
            false
        }
    }

    private fun renameThisFiles(java: VirtualFile, e: AnActionEvent) {
        for (item in java.children) {
            if (item.isDirectory) {
                renameThisFiles(item, e)
            } else {
                if (item.name.endsWith(".java") || item.name.endsWith(".kt")) {
                    println("currentRenameFile:" + item.name)
                    val action = AutoRenameElementAction()
                    val event = AnActionEvent.createFromAnAction(action, null, "", e.dataContext)
                    val itemPsi = PsiManager.getInstance(e.project!!).findFile(item)
                    val originalElement = itemPsi!!.originalElement
                    if (originalElement != null) {
                        val children = originalElement.children
                        for (psiElement in children) {
                            val equals = psiElement.javaClass.simpleName.equals("PsiClassImpl")
                            if(equals){
                                action.actionPerformed2(event, psiElement)
                            }
                        }

                    }


                    /* val createRename =
                         RefactoringFactory.getInstance(e.project).createRename(originalElement, "newName");*/

                }
            }
        }

    }

    private fun findSourceFile(children: VirtualFile?, fileName: String): VirtualFile? {
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
}