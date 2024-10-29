@file:Suppress("EmptyMethod")

package com.headoncloud.headoncloud.geoquizreview

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel(){
    //properties for questions and answers
    private val questionList = listOf<Questions>(
        Questions(R.string.question_HaNoi, true),
        Questions(R.string.question_NgheAn, true),
        Questions(R.string.question_BacNinh, false),
        Questions(R.string.question_VinhPhuc, false)
    )

    var currentIndex: Int
        get() =  savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
        private set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    val questionListSize = questionList.size

    val currentQuestionText: Int
        get() = questionList[currentIndex].textResId

    val currentQuestionAnswer: Boolean
        get() = questionList[currentIndex].answer

    //Has question been answered?
    var questionCheckedList = MutableList(questionList.size){false}
        private set

    private var correctCount = 0.0

    val isCurrentQuestionChecked: Boolean
        get() = questionCheckedList[currentIndex]

    val numberCorrectAnswers: Double
        get() = correctCount

    //Is current question cheated?
    var isCheater: Boolean
        get() = savedStateHandle.get(IS_CHEATER_KEY) ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)

    var setOfCheaters = mutableSetOf<Int>()


    /*----------------------------------------------------------------------------------*/
    //functions to update questions
    fun moveToNextQuestion(){
//        Log.d(TAG, "updating question text", Exception())
        currentIndex = (currentIndex + 1) % questionList.size
    }

    fun moveToLastQuestion(){
        currentIndex = Math.abs(currentIndex - 1) % questionList.size
    }

    //functions to check answered questions
    fun updateQuestionChecked(){
        questionCheckedList[currentIndex] = true
    }

    //function to count number of correct answers
    fun countCorrectAnswers(){
        correctCount++
    }

    fun resetScore(){
        correctCount = 0.0
        questionCheckedList.replaceAll { false }
    }
}