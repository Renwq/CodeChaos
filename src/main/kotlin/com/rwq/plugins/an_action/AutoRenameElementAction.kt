package com.rwq.plugins.an_action

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.codeInsight.lookup.Lookup
import com.intellij.codeInsight.lookup.LookupManager
import com.intellij.codeInsight.lookup.impl.LookupImpl
import com.intellij.ide.IdeEventQueue
import com.intellij.internal.statistic.eventLog.FeatureUsageData
import com.intellij.internal.statistic.service.fus.collectors.FUCounterUsageLogger
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.application.ApplicationBundle
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.command.UndoConfirmationPolicy
import com.intellij.openapi.editor.actionSystem.DocCommandGroupId
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.util.ThrowableComputable
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.refactoring.InplaceRefactoringContinuation
import com.intellij.refactoring.RefactoringActionHandler
import com.intellij.refactoring.RefactoringBundle
import com.intellij.refactoring.actions.RenameElementAction
import com.intellij.refactoring.rename.Renamer
import com.intellij.refactoring.rename.RenamerFactory
import com.intellij.refactoring.rename.inplace.InplaceRefactoring
import com.intellij.refactoring.util.CommonRefactoringUtil
import com.intellij.ui.components.JBLabel
import com.intellij.util.SlowOperations
import com.intellij.util.ui.JBEmptyBorder
import com.intellij.util.ui.UIUtil
import com.rwq.plugins.handler.AutoPsiElementRenameHandler
import java.awt.Component
import java.util.stream.Collectors
import javax.swing.JList
import javax.swing.ListCellRenderer

/**
 * author： rwq
 * date: 2021/7/7 15:15
 * desc:
 **/
class AutoRenameElementAction : RenameElementAction() {

    fun getHandler(dataContext: DataContext): RefactoringActionHandler? {
        return AutoPsiElementRenameHandler()
    }

     object RenamerRenderer : ListCellRenderer<Renamer> {

        private val myComponent = JBLabel()

        override fun getListCellRendererComponent(list: JList<out Renamer>,
                                                  value: Renamer,
                                                  index: Int,
                                                  isSelected: Boolean,
                                                  cellHasFocus: Boolean): Component {
            myComponent.text = value.presentableText
            myComponent.background = UIUtil.getListBackground(isSelected, cellHasFocus)
            myComponent.foreground = UIUtil.getListForeground(isSelected, cellHasFocus)
            myComponent.border = JBEmptyBorder(UIUtil.getListCellPadding())
            return myComponent
        }
    }


    //212 版本rename方法
    fun actionPerformed3(e: AnActionEvent, psiElement: PsiElement) {
        val dataContext = e.dataContext
        val project = dataContext.getData(CommonDataKeys.PROJECT)
            ?: return
        val editor = dataContext.getData(CommonDataKeys.EDITOR)
        if (editor != null && InplaceRefactoringContinuation.tryResumeInplaceContinuation(
                project, editor,
                RenameElementAction::class.java
            )
        ) {
            return
        }

        if (!PsiDocumentManager.getInstance(project).commitAllDocumentsUnderProgress()) {
            return
        }

        val renamers: List<Renamer> = SlowOperations.allowSlowOperations(ThrowableComputable<List<Renamer>, Exception> {
            val flatMap = RenamerFactory.EP_NAME.extensions().flatMap { factory ->
                factory.createRenamers(dataContext).stream()
            }
            return@ThrowableComputable flatMap.collect(Collectors.toList())
        })
        if (renamers.isEmpty()) {
            val message = RefactoringBundle.getCannotRefactorMessage(
                RefactoringBundle.message("error.wrong.caret.position.symbol.to.refactor")
            )
            CommonRefactoringUtil.showErrorHint(
                project,
                e.getData(CommonDataKeys.EDITOR),
                message,
                RefactoringBundle.getCannotRefactorMessage(null),
                null
            )
        } else if (renamers.size == 1) {
            getHandler(dataContext)?.invoke(project, arrayOf(psiElement), dataContext)
//            renamers[0].performRename()
        } else {
            JBPopupFactory.getInstance()
                .createPopupChooserBuilder(renamers)
                .setTitle(RefactoringBundle.message("what.would.you.like.to.do"))
                .setRenderer(RenamerRenderer)
                .setItemChosenCallback { obj: Renamer -> obj.performRename() }
                .createPopup()
                .showInBestPositionFor(dataContext)
        }
    }

    fun actionPerformed2(e: AnActionEvent, psiElement: PsiElement) {
        actionPerformed3(e,psiElement)
       /* val dataContext = e.dataContext
        val project = e.project ?: return
        val eventCount = IdeEventQueue.getInstance().eventCount
        if (!PsiDocumentManager.getInstance(project).commitAllDocumentsUnderProgress()) {
            return
        }
        IdeEventQueue.getInstance().eventCount = eventCount
        val editor = dataContext.getData(CommonDataKeys.EDITOR)
        val elements = arrayOf(psiElement)
        val handler: RefactoringActionHandler? = try {
            getHandler(dataContext)
        } catch (ignored: ProcessCanceledException) {
            return
        }
        if (handler == null) {
            val message =
                RefactoringBundle.getCannotRefactorMessage(RefactoringBundle.message("error.wrong.caret.position.symbol.to.refactor"))
            CommonRefactoringUtil.showErrorHint(
                project,
                editor,
                message,
                RefactoringBundle.getCannotRefactorMessage(null),
                null
            )
            return
        }
        val activeInplaceRenamer = InplaceRefactoring.getActiveInplaceRenamer(editor)
        if (!InplaceRefactoring.canStartAnotherRefactoring(
                editor!!,
                handler,
                *elements
            ) && activeInplaceRenamer != null
        ) {
            InplaceRefactoring.unableToStartWarning(project, editor)
            return
        }
        if (activeInplaceRenamer == null) {
            val lookup = LookupManager.getActiveLookup(editor)
            if (lookup is LookupImpl) {
                val command = Runnable { lookup.finishLookup(Lookup.NORMAL_SELECT_CHAR) }
                val doc = editor!!.document
                val group = DocCommandGroupId.noneGroupId(doc)
                CommandProcessor.getInstance().executeCommand(
                    editor.project,
                    command,
                    ApplicationBundle.message("title.code.completion"),
                    group,
                    UndoConfirmationPolicy.DEFAULT,
                    doc
                )
            }
        }
        IdeEventQueue.getInstance().eventCount = eventCount
        val file = if (editor != null) PsiDocumentManager.getInstance(project).getPsiFile(editor.document) else null
        val language = file?.language ?: (if (elements.isNotEmpty()) elements[0].language else null)!!
        val data = FeatureUsageData()
            .addData("handler", handler.javaClass.name)
            .addLanguage(language)
        if (elements.isNotEmpty()) {
            data.addData("element", elements[0].javaClass.name)
        }
        FUCounterUsageLogger.getInstance().logEvent(project, "refactoring", "handler.invoked", data)
        if (editor != null) {
            if (file == null) return
            DaemonCodeAnalyzer.getInstance(project).autoImportReferenceAtCursor(editor, file)
            handler.invoke(project, editor, file, dataContext)
        } else {
            handler.invoke(project, elements, dataContext)
        }*/
    }


}