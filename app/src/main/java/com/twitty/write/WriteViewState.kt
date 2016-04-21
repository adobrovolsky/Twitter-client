package com.twitty.write

import android.os.Bundle
import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState

class WriteViewState : RestorableViewState<WriteView> {

    private val KEY_STATE: String = "com.twitty.write.current_state"
    private val STATE_SHOWING_FORM: Byte = 0
    private val STATE_SHOWING_LOADING: Byte = 1

    private var currentState = STATE_SHOWING_FORM

    override fun restoreInstanceState(`in`: Bundle?): RestorableViewState<WriteView>? {
        if (`in` == null) {
            return null;
        }
        currentState = `in`.getByte(KEY_STATE)
        return this
    }

    override fun saveInstanceState(out: Bundle) {
        out.putByte(KEY_STATE, currentState)
    }

    override fun apply(view: WriteView?, retained: Boolean) {
       when (currentState) {
           STATE_SHOWING_FORM -> view?.showForm()
           STATE_SHOWING_LOADING -> view?.showLoading()
       }
    }

    fun setStateShowForm() = { currentState = STATE_SHOWING_FORM }
    fun setStateShowLoading() = { currentState = STATE_SHOWING_LOADING }
}