package com.hit.software.test

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import kotlinx.android.synthetic.main.activity_question.*

class QuestionActivity : Activity() {

    private var QA: ArrayList<List<String>>? = null
    private var correctAnswers: ArrayList<*>? = null
    private var index = 0
    private var total = 0
    private val answers = hashMapOf<Int, Int>()
    private val radioButtonMap = hashMapOf<Int, Int>(R.id.radioButton_a to 1, R.id.radioButton_b to 2, R.id.radioButton_c to 3, R.id.radioButton_d to 4)
    private val radioButtons: List<Int> = listOf(R.id.radioButton_a, R.id.radioButton_b, R.id.radioButton_c, R.id.radioButton_d)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        val bundle = intent.extras
        QA = bundle!!.getSerializable("qa") as ArrayList<List<String>>
        correctAnswers = bundle.getSerializable("ca") as ArrayList<*>
        total = QA!!.size
        button_pre.isEnabled = false
        textViewQuestionNumber.text = String.format("1/%d", total)
        setNextButton(bundle)
        setPreButton()
    }

    private fun setNextButton(bundle: Bundle) {
        button_next.setOnClickListener {
            if (radioGroup.checkedRadioButtonId != -1) {
                answers[index] = radioButtonMap[radioGroup.checkedRadioButtonId]!!
            }
            if (index == 0) {
                button_pre.isEnabled = true
            }
            else if (index == total - 2) {
                button_next.text = "提交"
            }
            index++
            if (index < total) {
                textViewQuestionNumber.text = String.format("%d/%d", index + 1, total)
                writeQuestion()
                radioGroup.clearCheck()
                if (answers[index] != null) {
                    findViewById<RadioButton>(radioButtons[answers[index]!! - 1]).isChecked = true
                }
            }
            if (index == total) {
                var correct = 0
                for ((i, c) in correctAnswers!!.withIndex()) {
                    if (answers[i] == c) {
                        correct++
                    }
                }
                bundle.putSerializable("correct", correct)
                bundle.putSerializable("answers", answers)
                bundle.putSerializable("total", total)
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setPreButton() {
        button_pre.setOnClickListener {
            if (radioGroup.checkedRadioButtonId != -1) {
                answers[index] = radioButtonMap[radioGroup.checkedRadioButtonId]!!
            }
            if (index == 1) {
                button_pre.isEnabled = false
            }
            else if (index == total - 1) {
                button_next.text = "下一题"
            }
            index--
            textViewQuestionNumber.text = String.format("%d/%d", index + 1, total)
            writeQuestion()
            radioGroup.clearCheck()
            if (answers[index] != null) {
                findViewById<RadioButton>(radioButtons[answers[index]!! - 1]).isChecked = true
            }

        }
    }

    /**
     * 通过这个方法动态的更改控件之中的信息
     */
    private fun writeQuestion() {
        // 设置控件文本
        textViewQuestion.text = QA!![index][0]
        val options = listOf("A", "B", "C", "D")
        for (i in 0..3) {
            findViewById<RadioButton>(radioButtons[i]).text = String.format("%s. %s", options[i], QA!![index][i + 1])
        }
    }

}