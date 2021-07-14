package com.rwq.plugins.utils

/**
 * author： rwq
 * date: 2021/7/13 16:03
 * desc: 更名配置类
 **/
data class RenameOption(
    val isRenameClassName: Boolean,
    val isRenameProperty: Boolean,
    val isRenameMethod: Boolean,
    val isRenameLocalVariable: Boolean,
    val isAddInChaosCode: Boolean
)