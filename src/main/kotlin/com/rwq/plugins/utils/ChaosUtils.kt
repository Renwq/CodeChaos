package com.rwq.plugins.utils

/**
 * author： rwq
 * date: 2021/7/8 14:28
 * desc: 混乱字符
 **/
class ChaosUtils {
    companion object {
        @JvmStatic
        private val cacheKeys: MutableSet<String> = HashSet()

        @JvmStatic
        public fun getNewFileName(): String {
            val mixLength = 2
            val maxLength = 20

            val resultLength: Int = (1 + Math.random() * (maxLength - mixLength + 1)).toInt()//1...19

            var sb = StringBuilder()

            val charArrayWithNum = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            val charArray = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
            for (i in 1..resultLength + 1) {//1...19 for包前不包后
                if (i == 1) {
                    val index = (Math.random() * 52).toInt()
                    sb.append(charArray[index])
                } else {
                    val index = (Math.random() * 62).toInt()
                    sb.append(charArrayWithNum[index])
                }
            }
            val key = sb.toString()
            if (cacheKeys.contains(key)) {
                getNewFileName()
            } else {
                cacheKeys.add(key)
            }
            return key
        }

        @JvmStatic
        public fun clearKeyCache() {
            cacheKeys.clear()
        }

        @JvmStatic
        fun addNotAllowedKeys(keys: MutableList<String>) {
            cacheKeys.addAll(keys)
        }
    }
}