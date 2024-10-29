package com.headoncloud.headoncloud.geoquizreview

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

const val CURRENT_QUESTION_IS_CHEATED = "CURRENT_QUESTION_IS_CHEATED"
private const val CURRENT_INDEX = "CURRENT_INDEX"

class CheatViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel(){
    var currentQuestionIsCheated: Boolean
        get() = savedStateHandle.get(CURRENT_QUESTION_IS_CHEATED) ?: false
        private set(value) = savedStateHandle.set(CURRENT_QUESTION_IS_CHEATED, value)

    var currentAnswer: Boolean = false

    fun noteCheater(){
        currentQuestionIsCheated = true
    }

//    fun getCurrentAnswer(){
//
//    }
}