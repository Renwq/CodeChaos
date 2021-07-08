package com.rwq.plugins.handler

import com.intellij.featureStatistics.FeatureUsageTracker
import com.intellij.ide.scratch.ScratchUtil
import com.intellij.lang.LangBundle
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil
import com.intellij.psi.util.PsiUtilCore
import com.intellij.refactoring.RefactoringBundle
import com.intellij.refactoring.actions.BaseRefactoringAction
import com.intellij.refactoring.rename.PsiElementRenameHandler
import com.intellij.refactoring.rename.RenamePsiElementProcessor
import com.intellij.refactoring.util.CommonRefactoringUtil.RefactoringErrorHintException
import com.rwq.plugins.utils.ChaosUtils

/**
 * author： rwq
 * date: 2021/7/7 14:48
 * desc:
 */
class AutoPsiElementRenameHandler : PsiElementRenameHandler() {
    override fun invoke(project: Project, editor: Editor?, file: PsiFile?, dataContext: DataContext) {
        var element = getElement(dataContext)
        if (element == null) {
            element = BaseRefactoringAction.getElementAtCaret(editor!!, file)
        }

        if (ApplicationManager.getApplication().isUnitTestMode) {
            val newName = DEFAULT_NAME.getData(dataContext)
            if (newName != null) {
                rename(element!!, project, element, editor, newName)
                return
            }
        }

        editor!!.scrollingModel.scrollToCaret(ScrollType.MAKE_VISIBLE)
        val nameSuggestionContext = InjectedLanguageUtil.findElementAtNoCommit(file!!, editor!!.caretModel.offset)
        invoke(element!!, project, nameSuggestionContext, editor, shouldCheckInProject())
    }

    override fun invoke(project: Project, elements: Array<out PsiElement>, dataContext: DataContext?) {
        var element = if (elements.size == 1) elements[0] else null
        if (element == null) element = getElement(dataContext!!)
        val editor = CommonDataKeys.EDITOR.getData(dataContext!!)
        if (ApplicationManager.getApplication().isUnitTestMode) {
            val newName = DEFAULT_NAME.getData(dataContext)
            rename(element!!, project, element, editor, newName)
        } else {
            invoke(element!!, project, element, editor, shouldCheckInProject())
        }
    }

    companion object {


        fun rename(
            element: PsiElement,
            project: Project,
            nameSuggestionContext: PsiElement?,
            editor: Editor?,
            defaultName: String?
        ) {
            val processor = RenamePsiElementProcessor.forElement(element)
            rename(element, project, nameSuggestionContext, editor, defaultName, processor)
        }


        private fun rename(
            element: PsiElement,
            project: Project,
            nameSuggestionContext: PsiElement?,
            editor: Editor?,
            defaultName: String?,
            processor: RenamePsiElementProcessor
        ) {
            var defaultName = defaultName
            val substituted = processor.substituteElementToRename(element, editor)
            if (substituted == null || !canRename(project, editor, substituted)) return
            val dialog = processor.createRenameDialog(
                project,
                substituted,
                nameSuggestionContext,
                editor
            )
            dialog.title = "Auto Rename"
            PsiUtilCore.ensureValid(substituted)
            dialog.setPreviewResults(false)
            dialog.performRename(ChaosUtils.getNewFileName())
        }


        operator fun invoke(
            element: PsiElement,
            project: Project,
            nameSuggestionContext: PsiElement?,
            editor: Editor?,
            checkInProject: Boolean
        ) {
            if (!canRename(project, editor, element)) {
                return
            }
            val contextFile = PsiUtilCore.getVirtualFile(nameSuggestionContext)
            if (checkInProject && nameSuggestionContext != null &&
                nameSuggestionContext.isPhysical &&
                (contextFile == null || !ScratchUtil.isScratch(contextFile)) &&
                !PsiManager.getInstance(project).isInProject(nameSuggestionContext)
            ) {
                val message = LangBundle.message("dialog.message.selected.element.used.from.non.project.files")
                if (ApplicationManager.getApplication().isUnitTestMode) throw RefactoringErrorHintException(message)
                if (Messages.showYesNoDialog(
                        project, message,
                        RefactoringBundle.getCannotRefactorMessage(null), Messages.getWarningIcon()
                    ) != Messages.YES
                ) {
                    return
                }
            }
            FeatureUsageTracker.getInstance().triggerFeatureUsed("refactoring.rename")
            rename(element, project, nameSuggestionContext, editor)
        }


        private fun rename(element: PsiElement, project: Project, nameSuggestionContext: PsiElement?, editor: Editor?) {
            rename(element, project, nameSuggestionContext, editor, null)
        }
    }





}