package com.rwq.plugins.dialog

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.ValidationInfo
import com.rwq.plugins.utils.GsonUtils
import com.rwq.plugins.utils.LocalDataUtils
import com.rwq.plugins.utils.RenameOption
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.ItemEvent
import javax.swing.*

/**
 * author： rwq
 * date: 2021/7/14 11:14
 * desc:
 */
class RenameOptionDialogWrapper : DialogWrapper(true) {
    private var exitAction: DialogWrapperExitAction? = null
    private var mRenameClassNameCB: JCheckBox? = null
    private var mRenamePropertyCB: JCheckBox? = null
    private var mRenameMethodNameCB: JCheckBox? = null
    private var mRenameMethodParCB: JCheckBox? = null
    private var mRenameLocalVariableCB: JCheckBox? = null
    private var mAddInChaosCodeCB: JCheckBox? = null
    private var okAction: CustomOKAction? = null

    /**
     * 创建视图
     * @return
     */
    override fun createCenterPanel(): JComponent? {
        val data = LocalDataUtils.getData(LocalDataUtils.KEY_RENAME_OPTION)
        var lastRenameOption: RenameOption? = null
        if (data == null) {
            lastRenameOption = RenameOption(
                isRenameClassName = true,
                isRenameProperty = false,
                isRenameMethodPar = true,
                isRenameMethodName = false,
                isRenameLocalVariable = true,
                isAddInChaosCode = false
            )
        } else {
            lastRenameOption = GsonUtils.parseData(data, RenameOption::class.java)
        }


        val panel = JPanel()
        panel.layout = GridLayout(6, 1)
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
        panel.add(mRenameClassNameCB!!)
        panel.add(mRenamePropertyCB!!)
        panel.add(mRenameMethodNameCB!!)
        panel.add(mRenameMethodParCB!!)
        panel.add(mRenameLocalVariableCB!!)
        panel.add(mAddInChaosCodeCB!!)
        return panel
    }

    /**
     * 校验数据
     * @return 通过必须返回null
     */
    override fun doValidate(): ValidationInfo? {
        return null
    }

    /**
     * 覆盖默认的ok/cancel按钮
     * @return
     */
    override fun createActions(): Array<Action> {
        exitAction = DialogWrapperExitAction("取消", CANCEL_EXIT_CODE)
        // 设置默认的焦点按钮
        okAction = CustomOKAction()
        okAction!!.putValue(DEFAULT_ACTION, true)
        return arrayOf(okAction!!, exitAction!!)
    }

    /**
     * 自定义 ok Action
     */
    protected open inner class CustomOKAction() : DialogWrapperAction("好的") {
        override fun doAction(e: ActionEvent) {
            // 点击ok的时候进行数据校验
            val validationInfo = doValidate()
            if (validationInfo != null) {
                Messages.showMessageDialog(validationInfo.message, "校验不通过", Messages.getInformationIcon())
            } else {
                close(CANCEL_EXIT_CODE)
            }
        }
    }

    init {
        init()
        title = "配置"
    }
}