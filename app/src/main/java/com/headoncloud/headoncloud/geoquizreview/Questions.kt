package com.headoncloud.headoncloud.geoquizreview

import androidx.annotation.StringRes

data class Questions(
    @StringRes val textResId: Int,
    val answer: Boolean
)


