package com.rwq.plugins.an_action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.*
import com.intellij.psi.impl.java.stubs.JavaStubElementTypes
import com.intellij.psi.impl.source.PsiClassImpl
import com.intellij.psi.impl.source.PsiFieldImpl
import com.intellij.psi.impl.source.PsiMethodImpl
import com.rwq.plugins.utils.RenameOption
import org.jetbrains.kotlin.psi.psiUtil.getChildrenOfType


/**
 * author： rwq
 * date: 2021/7/7 11:21
 * desc:
 */
class AutoRenameAllClassAction : AnAction() {


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
                        doHandler(item, e, renameOption)
                    }
                }
            } else {
                doHandler(java, e, renameOption)
            }
        }

        private fun doHandler(item: VirtualFile, e: AnActionEvent, renameOption: RenameOption?) {
            if (item.name.endsWith(".java")) {
                println("currentRenameFile:" + item.name)
                val itemPsi = PsiManager.getInstance(e.project!!).findFile(item)
                val originalElement = itemPsi!!.originalElement
                if (originalElement != null) {
                    val children = originalElement.children
                    for (psiClass in children) {
                        val action = AutoRenameElementAction()
                        val event = AnActionEvent.createFromAnAction(action, null, "", e.dataContext)
                        if (renameOption != null) {
                            if (renameOption.isRenameProperty || renameOption.isRenameMethodPar) {
                                if (psiClass is PsiClassImpl) {
                                    //是类元素
                                    val extendsList = psiClass.getStubOrPsiChild(JavaStubElementTypes.EXTENDS_LIST)
                                    val implementsList =
                                        psiClass.getStubOrPsiChild(JavaStubElementTypes.IMPLEMENTS_LIST)
                                    for (classElement in psiClass.children) {
                                        if (renameOption.isRenameProperty && classElement is PsiFieldImpl) {
                                            //是字段元素
                                            action.actionPerformed2(event, classElement)
                                        }
                                        if (renameOption.isRenameMethodPar && classElement is PsiMethodImpl) {
                                            //是方法元素
                                            val overridableElement = classElement.isConstructor
                                            val isGetSetMethod =
                                                classElement.name.startsWith("get") or classElement.name.startsWith("set")
                                            val isDeprecated = classElement.isDeprecated
                                            if (overridableElement || isGetSetMethod ||isDeprecated) {
                                                continue
                                            }
                                            println(classElement.toString())
                                            val parameterListContent = classElement.getChildrenOfType<PsiParameterList>()
                                            for (par in parameterListContent) {
                                                val parameterList = par.getChildrenOfType<PsiParameter>()
                                                for (parOne in parameterList) {
                                                    action.actionPerformed2(event, parOne)
                                                }
                                            }
                                        }
                                        if (renameOption.isRenameLocalVariable && classElement is PsiMethodImpl) {
                                            // 是方法元素要更改 方法内的本地变量
                                            for (methodChild in classElement.children) {
                                                if (methodChild is PsiCodeBlock) {
                                                    renameLocalVariable(event, methodChild)
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (renameOption.isRenameClassName) {
                                //如果为空 只更改类名
                                if (psiClass is PsiClassImpl) {
                                    action.actionPerformed2(event, psiClass)
                                }
                            }
                        } else {
                            //如果为空 只更改类名
                            if (psiClass is PsiClassImpl) {
                                action.actionPerformed2(event, psiClass)
                            }
                        }
                    }
                }
                /* val createRename =
                     RefactoringFactory.getInstance(e.project).createRename(originalElement, "newName");*/

            }
        }

        private fun renameLocalVariable(event: AnActionEvent, element: PsiElement) {
            //方法块循环
            val declarationList = element.getChildrenOfType<PsiDeclarationStatement>()
            for (e in declarationList) {
                val localVarList = e.getChildrenOfType<PsiLocalVariable>()
                for (localVar in localVarList) {
                    val action = AutoRenameElementAction()
                    val anActionEvent = AnActionEvent.createFromAnAction(action, null, "", event.dataContext)
                    action.actionPerformed2(anActionEvent,localVar)
                }
            }
        }
    }


}