package com.sartimau.openpayfit.data.service

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sartimau.openpayfit.domain.entity.Location
import com.sartimau.openpayfit.domain.service.FirebaseService
import com.sartimau.openpayfit.domain.utils.CoroutineResult
import com.sartimau.openpayfit.domain.utils.EMPTY_STRING
import com.sartimau.openpayfit.domain.utils.ONE_INT
import com.sartimau.openpayfit.domain.utils.OPEN_PAY_FIT_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

enum class PostDelayedResponseEnum {
    POST_DELAYED_SUCCESS,
    POST_DELAYED_ERROR
}

class FirebaseServiceImpl @Inject constructor() : FirebaseService {
    override fun saveLocation(location: Location) {
        val db = Firebase.firestore

        // Format current time
        val date = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd - HH:mm:ss", Locale.getDefault())
        val formattedDate = dateFormat.format(date)

        db.collection(LOCATION_COLLECTION)
            .document(formattedDate)
            .set(location)
            .addOnSuccessListener { documentReference ->
                Log.d(OPEN_PAY_FIT_TAG, "Location saved success, $documentReference")
            }
            .addOnFailureListener { e ->
                Log.d(OPEN_PAY_FIT_TAG, "Location saved error, $e")
            }
    }

    override suspend fun getLocations(): CoroutineResult<HashMap<String, Location>> {
        var status: PostDelayedResponseEnum = PostDelayedResponseEnum.POST_DELAYED_ERROR
        val postDelayedResponse: HashMap<String, Location> = hashMapOf()
        var postDelayedResponseError: String = EMPTY_STRING
        val done = CountDownLatch(ONE_INT)

        val db = Firebase.firestore

        db.collection(LOCATION_COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(OPEN_PAY_FIT_TAG, "${document.id} => ${document.data}")

                    postDelayedResponse[document.id] = Location(
                        latitude = document.data[LATITUDE_PARAM] as Double,
                        longitude = document.data[LONGITUDE_PARAM] as Double
                    )
                }

                status = PostDelayedResponseEnum.POST_DELAYED_SUCCESS
                done.countDown()
            }
            .addOnFailureListener { exception ->
                Log.w(OPEN_PAY_FIT_TAG, "Error getting documents.", exception)

                postDelayedResponseError = exception.message.orEmpty()
                status = PostDelayedResponseEnum.POST_DELAYED_ERROR
                done.countDown()
            }

        withContext(Dispatchers.IO) { done.await() } // it will wait till the response is received from firebase.

        return when (status) {
            PostDelayedResponseEnum.POST_DELAYED_SUCCESS -> CoroutineResult.Success(postDelayedResponse)
            PostDelayedResponseEnum.POST_DELAYED_ERROR -> CoroutineResult.Failure(Exception(postDelayedResponseError))
        }
    }

    override suspend fun saveImages(selectedImages: List<ByteArray>) {
        val storage = Firebase.storage
        val storageRef = storage.reference

        selectedImages.forEach { image ->
            val imagesRef = storageRef.child("images/selectedImage-${UUID.randomUUID()}.jpg")

            val uploadTask = imagesRef.putBytes(image)
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            }
        }
    }

    companion object {
        private const val LOCATION_COLLECTION = "location"

        private const val LONGITUDE_PARAM = "longitude"
        private const val LATITUDE_PARAM = "latitude"
    }
}
