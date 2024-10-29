package com.headoncloud.headoncloud.geoquizreview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.headoncloud.headoncloud.geoquizreview.databinding.ActivityCheatBinding

private const val EXTRA_ANSWER_IS_TRUE = "com.headoncloud.headoncloud.geoquizreview.answer_is_true"
private const val EXTRA_CURRENT_INDEX = "com.headoncloud.headoncloud.geoquizreview.current_index"
const val EXTRA_ANSWER_SHOWN = "com.headoncloud.headoncloud.geoquizreview.answer_shown"

class CheatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheatBinding

    private val cheatViewModel: CheatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //lấy giá trị được gửi đến từ activity khác
        cheatViewModel.currentAnswer = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        //Hiện thị dữ liệu
        if(cheatViewModel.currentQuestionIsCheated){
            displayResult()
        }

        binding.btnShowAnswer.setOnClickListener {
            displayResult()

            //đánh dấu câu hỏi đã bị cheat
            cheatViewModel.noteCheater()
            //mặc định giá trị trả về là true
            setAnswerShownResult(true)
        }

        //Trả kết quả về activity gọi nó (có thể false nếu Cheat chưa từng được thực hiện)
        setAnswerShownResult(cheatViewModel.currentQuestionIsCheated)
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean){
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    private fun displayResult(){
        val answerText = when{
            cheatViewModel.currentAnswer -> R.string.true_button
            else -> R.string.false_button
        }
        binding.tvAnswer.setText(answerText)
    }

    companion object{
        fun newIntent(packageContext: Context, answerIsTrue: Boolean) : Intent{
            return Intent(packageContext, CheatActivity::class.java).apply{
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }

    }
}