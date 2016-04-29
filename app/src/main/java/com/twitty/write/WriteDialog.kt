package com.twitty.write

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.hannesdorfmann.mosby.mvp.delegate.BaseMvpViewStateDelegateCallback
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState
import com.twitty.IntentStarter
import com.twitty.MainApplication
import com.twitty.R
import com.twitty.base.MvpViewStateDialogFragment
import com.twitty.store.entity.Draft
import java.util.*

class WriteDialog : MvpViewStateDialogFragment<WriteView, WritePresenter>(),
        WriteView, BaseMvpViewStateDelegateCallback<WriteView, WritePresenter> {

    private var tweetMessage : TextView? = null
    private var addPhotoButton : ImageButton? = null
    private var addPictureButton : ImageButton? = null
    private var postTweetButton : ImageButton? = null

    private val intentStarter = IntentStarter()
    private var draft: Draft? = null

    lateinit var component: WriteComponent

    companion object Companion {

        val argumentStatus = "argument_status"

        @JvmStatic fun newInstance(draft: Draft?) : WriteDialog {
            val args = Bundle().apply { putSerializable(argumentStatus, draft) }
            val dialog = WriteDialog().apply { arguments = args }
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.dialog_write, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tweetMessage = view?.findViewById(R.id.tweetMessage) as TextView
        addPhotoButton = view?.findViewById(R.id.addPhotoButton) as ImageButton
        addPictureButton = view?.findViewById(R.id.addPictureButton) as ImageButton
        postTweetButton = view?.findViewById(R.id.postTweetButton) as ImageButton

        if (arguments != null) {
            draft = arguments.getSerializable(argumentStatus) as Draft
            tweetMessage?.text = draft?.text
        }

        addPhotoButton?.setOnClickListener {
            if (intentStarter.isIntentAvailable(MediaStore.ACTION_IMAGE_CAPTURE))
                intentStarter.openCamera(activity)
            else
                Toast.makeText(activity, "Camera app not found", Toast.LENGTH_SHORT).show()
        }

        addPictureButton?.setOnClickListener {
            if (intentStarter.isIntentAvailable(Intent.ACTION_PICK))
                intentStarter.openImagePicker(activity)
        }

        postTweetButton?.setOnClickListener {
            val status = tweetMessage?.text.toString()
            var saved = true;

            if (status.isNotEmpty()) {
                return@setOnClickListener
            }
            if (draft == null) {
                saved = false
                draft = Draft(null, status, null)
            }
            presenter.postTweet(draft as Draft, saved)
        }
    }

    override fun onDestroyView() {
        saveTweet();
        super.onDestroyView()
    }

    private fun saveTweet() {
        val status = tweetMessage?.text.toString()
        if (status.isNotEmpty()) {
            if (draft == null) {
                draft = Draft(null, status, Date())
            }
            presenter.saveTweet(draft as Draft)
            Toast.makeText(activity, R.string.notification_tweet_saved, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onViewStateInstanceRestored(instanceStateRetained: Boolean) {
        // Not needed
    }

    override fun createViewState(): ViewState<WriteView> {
        return WriteViewState()
    }

    override fun onNewViewStateInstance() {
        showForm()
    }

    override fun createPresenter(): WritePresenter {
        return component.getPresenter()
    }

    override fun getMvpView(): WriteView? {
        return this
    }

    override fun showForm() {

    }

    override fun showLoading() {

    }

    override fun showError(e: Exception) {

    }

    override fun finishWrite() {

    }

    protected fun injectDependencies() {
        component = DaggerWriteComponent.builder()
                .userComponent(MainApplication.getUserComponent())
                .build()
        component.inject(this)
    }
}