package com.twitty.write


import com.hannesdorfmann.mosby.mvp.MvpBasePresenter
import com.twitty.store.DraftsRepository
import com.twitty.store.entity.Draft
import twitter4j.AsyncTwitter
import javax.inject.Inject

class WritePresenter
@Inject constructor(val twitter: AsyncTwitter, val repository: DraftsRepository) : MvpBasePresenter<WriteView>() {

    fun postTweet(status: Draft, saved: Boolean) {
        twitter.updateStatus(status.text)
        if (saved) {
            repository.delete(status)
        }
    }

    fun saveTweet(draft: Draft) {
        repository.save(draft)
    }
}