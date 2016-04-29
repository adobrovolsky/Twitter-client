package com.twitty.write

import com.twitty.dagger.components.UserComponent
import com.twitty.dagger.scopes.ActivityScope
import dagger.Component


@ActivityScope
@Component(dependencies = arrayOf(UserComponent::class))
interface WriteComponent {

    fun getPresenter() : WritePresenter

    fun inject(writeDialog: WriteDialog)
}