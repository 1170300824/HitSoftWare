package com.hit.software.test

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : Activity(), WebResponse {

    private var mainScope: CoroutineScope? = null
    var mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == 0) {
                Toast.makeText(applicationContext, msg.obj.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainScope = MainScope()
        setStartButton()
    }

    private fun setStartButton() {
        button_start.setOnClickListener {
            Log.d("Info", "请求问题及答案")
            val This = this
            mainScope!!.launch { WebRequest.get(This, "http://192.168.43.132:8080/")}
        }
    }

    /**
     * 回调方法
     */

    override fun requestSucceeded(content: String) {
        val result = ProcessResponse.getQA(content)
        if (result != null) {
            val QA = result.first
            val correctAnswers = result.second
            mainScope!!.cancel()
            val bundle = Bundle()
            bundle.putSerializable("qa", QA)
            bundle.putSerializable("ca", correctAnswers)
            val intent = Intent(this, QuestionActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        else {
            Log.d("Warning", "响应出错: $content")
            val mMessage = Message()
            mMessage.what = 0
            mMessage.obj = "获取题目失败"
            mHandler.sendMessage(mMessage)
        }

    }

    override fun requestFailed() {
        val mMessage = Message()
        mMessage.what = 0
        mMessage.obj = "获取题目失败"
        mHandler.sendMessage(mMessage)
    }
}