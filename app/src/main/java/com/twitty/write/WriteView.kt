package com.twitty.write

import com.hannesdorfmann.mosby.mvp.MvpView

interface WriteView : MvpView {

    fun showForm() : Unit

    fun showLoading(): Unit

    fun showError(e: Exception) : Unit

    fun finishWrite() : Unit
}