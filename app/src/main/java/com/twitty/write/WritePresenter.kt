package com.twitty.write

import android.content.Context
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter
import com.twitty.AppPreferences
import com.twitty.Constants
import com.twitty.store.DraftsRepository
import com.twitty.store.entity.Draft
import twitter4j.AsyncTwitter
import twitter4j.AsyncTwitterFactory
import twitter4j.auth.AccessToken

class WritePresenter(val context: Context) : MvpBasePresenter<WriteView>() {

    private val twitter: AsyncTwitter
    private val preferences: AppPreferences
    private val repository: DraftsRepository

    init {
        preferences = AppPreferences.getInstance()
        twitter = AsyncTwitterFactory().instance
        repository = DraftsRepository()
        twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET)
        twitter.oAuthAccessToken = AccessToken(preferences.accessToken, preferences.accessTokenSecret)
    }

    fun postTweet(status: Draft, saved: Boolean) {
        twitter.updateStatus(status.text)
        if (saved) {
            repository.delete(status)
        }
    }

    fun saveTweet(draft: Draft) {
        if (repository.exists(draft.id)) {
            repository.update(draft)
        } else {
            repository.save(draft)
        }
    }
}