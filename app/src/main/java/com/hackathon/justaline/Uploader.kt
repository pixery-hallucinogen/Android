package com.hackathon.justaline

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import com.hackathon.MainActivity
import com.hackathon.R
import com.hackathon.data.model.CreatePostRequest
import com.hackathon.data.model.Post
import com.hackathon.di.module.SchedulersModule
import com.hackathon.domain.auth.PostTask
import io.reactivex.disposables.CompositeDisposable
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class Uploader : KoinComponent {
    val postTask: PostTask by inject()
    val schedulersModule: SchedulersModule by inject()
    val storage = FirebaseStorage.getInstance()

    fun upload(context: Context, bitmap: Bitmap) {
        // Create a storage reference from our app
        val storageRef = storage.reference

// Create a reference to "mountains.jpg"
        val mountainsRef = storageRef.child(Date().time.toString() + ".jpg")

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            Toast.makeText(context, R.string.errorOccurred, Toast.LENGTH_LONG).show()
        }.addOnSuccessListener { a ->
            mountainsRef.downloadUrl.addOnSuccessListener {
                CompositeDisposable().add(
                        postTask.createPost(CreatePostRequest(
                                Post(
                                        latitude = 41.0797675,
                                        longitude = 29.0064777,
                                        media = it.toString(),
                                        location = "Levent, Istanbul",
                                        description = ""
                                )
                        )).subscribeOn(schedulersModule.io())
                                .observeOn(schedulersModule.ui())
                                .subscribe({ result ->
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    context.startActivity(intent)
                                }, { error ->
                                    Toast.makeText(context, R.string.errorOccurred, Toast.LENGTH_LONG).show()
                                }))
            }
        }
    }

    fun upload(context: Context, file: File) {
        // Create a storage reference from our app
        val storageRef = storage.reference

// Create a reference to "mountains.jpg"
        val mountainsRef = storageRef.child(Date().time.toString() + ".mp4")

        val uploadTask = mountainsRef.putFile(file.toUri())
        uploadTask.addOnFailureListener {
            Toast.makeText(context, R.string.errorOccurred, Toast.LENGTH_LONG).show()
        }.addOnSuccessListener { a ->
            mountainsRef.downloadUrl.addOnSuccessListener {
                Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
                CompositeDisposable().add(
                        postTask.createPost(CreatePostRequest(
                                Post(
                                        latitude = 41.0797675,
                                        longitude = 29.0064777,
                                        media = it.toString(),
                                        location = "Levent, Istanbul",
                                        description = ""
                                )
                        )).subscribeOn(schedulersModule.io())
                                .observeOn(schedulersModule.ui())
                                .subscribe({ result ->
                                    Toast.makeText(context, "Success2", Toast.LENGTH_LONG).show()
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    context.startActivity(intent)
                                }, { error ->
                                    Toast.makeText(context, R.string.errorOccurred, Toast.LENGTH_LONG).show()
                                }))
            }
        }
    }
}