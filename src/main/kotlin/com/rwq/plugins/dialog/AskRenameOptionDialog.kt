package com.rwq.plugins.dialog

import com.intellij.openapi.ui.Messages
import com.intellij.openapi.wm.ToolWindow
import java.awt.event.ActionListener
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JPanel
import java.awt.event.ItemEvent

/**
 * author： rwq
 * date: 2021/7/14 11:13
 * desc:
 */
class AskRenameOptionDialog(
    toolWindow: ToolWindow?,
    private val ok: ActionListener,
    private val cancel: ActionListener
) {
    private var hideButton: JButton? = null
    private var dialogButton: JButton? = null
    private var mRenameClassNameCB: JCheckBox? = null
    private var mRenamePropertyCB: JCheckBox? = null
    private var mRenameMethodNameCB: JCheckBox? = null
    private var mRenameMethodParCB: JCheckBox? = null
    private var mRenameLocalVariableCB: JCheckBox? = null
    private var mAddInChaosCodeCB: JCheckBox? = null
    var content: JPanel? = null
        private set

    private fun init() {
        dialogButton = JButton("执行")
        dialogButton!!.addActionListener(ok)
        hideButton = JButton("取消")
        hideButton!!.addActionListener(cancel)
        mRenameClassNameCB = JCheckBox()
        mRenameClassNameCB!!.text = "重命名类名"
        mRenamePropertyCB = JCheckBox()
        mRenamePropertyCB!!.text = "重命名属性（全局变量）"
        mRenameMethodNameCB = JCheckBox()
        mRenameMethodNameCB!!.text = "重命名方法名（目前慎用）"
        mRenameMethodParCB = JCheckBox()
        mRenameMethodParCB!!.text = "重命名方法参数列表"
        mRenameLocalVariableCB = JCheckBox()
        mRenameLocalVariableCB!!.text = "重命名方法本地变量"
        mAddInChaosCodeCB = JCheckBox()
        mAddInChaosCodeCB!!.text = "方法中加入无用代码"
        mAddInChaosCodeCB!!.addItemListener { itemEvent: ItemEvent ->
            val stateChange = itemEvent.stateChange
            if (stateChange == 1) {
                Messages.showMessageDialog("功能开发中", "提示", Messages.getInformationIcon())
                mAddInChaosCodeCB!!.isSelected = false
            }
        }
        content = JPanel()
        content!!.add(dialogButton)
        content!!.add(hideButton)
        content!!.add(mRenameClassNameCB)
        content!!.add(mRenamePropertyCB)
        content!!.add(mRenameMethodNameCB)
        content!!.add(mRenameMethodParCB)
        content!!.add(mRenameLocalVariableCB)
        content!!.add(mAddInChaosCodeCB)
    }

    init {
        init()
    }
}