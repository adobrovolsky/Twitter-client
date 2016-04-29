package com.twitty

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.FragmentActivity
import com.twitty.drafts.DraftsActivity
import com.twitty.login.LoginActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class IntentStarter {

    val appContext = MainApplication.getContext()

    fun showLoginActivity(context: Context, arg: String) {
       val intent = Intent(context, LoginActivity::class.java)
       intent.putExtra(LoginActivity.EXTRA_VERIFIER, arg)
        context.startActivity(intent)
    }

    fun showAuthentication(context: Context) {
       val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
    }

    fun openCamera(context: FragmentActivity) {
        val takePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                createFileName())
        takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file))
        context.startActivityForResult(takePhoto, MainActivity.REQUEST_IMAGE_CAPTURE)
    }

    fun openImagePicker(context: FragmentActivity) {
        val pickImage = Intent(Intent.ACTION_PICK)
        context.startActivityForResult(pickImage, MainActivity.REQUEST_PICK_IMAGE)
    }

    fun showDrafts(context: Context) {
        val intent = Intent(context, DraftsActivity::class.java)
        context.startActivity(intent)
    }

    fun isIntentAvailable(action: String) : Boolean {
        val packageManager = appContext.packageManager
        val resolveInfo = packageManager.queryIntentActivities(
                Intent(action), PackageManager.MATCH_DEFAULT_ONLY)
        return resolveInfo.size > 0
    }

     private fun createFileName() : String {
        val date = SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault()).format(Date())
        return "IMG_$date.jpg"
    }
}