package com.hit.software.test

import org.json.JSONObject

object ProcessResponse {

    fun userLogin(res:String): String? {
        //解析JSON
        try {
            val jsonData = JSONObject(res)

            when (jsonData.optString("type", "")) {
                "user_login" -> {
                    if (jsonData.optString("data") == "true") {
                        //用户登录成功
                        return "success"
                    } else {
                        return jsonData.optString("error")
                    }
                }
                "" -> {
                    return null
                }
                else -> return null
            }
        } catch (e: Exception) {
            return null
        }

    }
}