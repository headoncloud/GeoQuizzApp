package com.headoncloud.headoncloud.geoquizreview

import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.headoncloud.headoncloud.geoquizreview.databinding.ActivityMainBinding
import kotlin.time.times

private const val TAG = "MainActivity"
private const val INDEX = "IndexOfList"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        //Handle the result
        if(result.resultCode == RESULT_OK){
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            quizViewModel.isCheater
            if(quizViewModel.isCheater){
                quizViewModel.setOfCheaters.add(quizViewModel.currentIndex)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        val questionTextResId = quizViewModel.currentQuestionText
        binding.tvQuestionText.setText(questionTextResId)

        binding.btnTrue.setOnClickListener {
            clickable(true)
        }

        binding.btnFalse.setOnClickListener {
            clickable(false)
        }

        binding.btnNext.setOnClickListener {
            quizViewModel.moveToNextQuestion()
            updateQuestion()
        }

        binding.btnPrevious.setOnClickListener {
            quizViewModel.moveToLastQuestion()
            updateQuestion()
        }

        binding.tvQuestionText.setOnClickListener {
            quizViewModel.moveToNextQuestion()
            updateQuestion()
        }

        binding.btnCheat.setOnClickListener {
//            val intent = Intent(this, CheatActivity::class.java)
            val answerOfCurrentQuestions = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this, answerOfCurrentQuestions)
//            startActivity(intent)
            cheatLauncher.launch(intent)
        }

        blurCheatButton()
    }

    private fun updateQuestion(){
        val questionTextResId = quizViewModel.currentQuestionText
        binding.tvQuestionText.setText(questionTextResId)
    }

    private fun clickable(answer: Boolean){
        //nếu câu hỏi chưa được check -> có thể trả lời
        if(quizViewModel.isCurrentQuestionChecked == false){
            checkAnswer(answer)
        }
    }

    private fun checkAnswer(answer: Boolean){
        quizViewModel.updateQuestionChecked()   //Đánh giấu câu hỏi này đã được trả lời
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResID = when{
            quizViewModel.setOfCheaters
                .contains(quizViewModel.currentIndex) -> R.string.judgment_toast
            answer == correctAnswer -> {
                quizViewModel.countCorrectAnswers() //đếm câu trả lời đúng -> để tính điểm
                R.string.true_button
            }
            else -> R.string.false_button
        }
        Toast.makeText(
            this,
            messageResID,
            Toast.LENGTH_SHORT
        ).show()

        var totalScore: Double = quizViewModel.numberCorrectAnswers / quizViewModel.questionListSize * 100

        if( quizViewModel.currentIndex == quizViewModel.questionListSize-1
            && quizViewModel.questionCheckedList.all { true }){
            Toast.makeText(
                this,
                "Your score: $totalScore%",
                Toast.LENGTH_SHORT
            ).show()
            quizViewModel.resetScore()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun blurCheatButton() {
        val effect = RenderEffect.createBlurEffect(
            10.0f,
            10.0f,
            Shader.TileMode.CLAMP
        )
        binding.btnCheat.setRenderEffect(effect)
    }
}

//            val snackbar = Snackbar.make(
//                findViewById(R.id.main),
//                R.string.correct_toast,
//                Snackbar.LENGTH_SHORT
//            )
//            snackbar.show()