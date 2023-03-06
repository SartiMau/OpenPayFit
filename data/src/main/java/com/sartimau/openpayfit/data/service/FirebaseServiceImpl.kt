package com.sartimau.openpayfit.data.service

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sartimau.openpayfit.domain.entity.Location
import com.sartimau.openpayfit.domain.service.FirebaseService
import com.sartimau.openpayfit.domain.utils.CoroutineResult
import com.sartimau.openpayfit.domain.utils.EMPTY_STRING
import com.sartimau.openpayfit.domain.utils.ONE_INT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

enum class PostDelayedResponseEnum {
    POST_DELAYED_SUCCESS,
    POST_DELAYED_ERROR
}

class FirebaseServiceImpl @Inject constructor() : FirebaseService {
    override fun saveLocation(location: Location) {
        val db = Firebase.firestore

        db.collection(LOCATION_COLLECTION)
            .add(location)
            .addOnSuccessListener { documentReference ->
                Log.d("OpenPayApp", "Location saved success, $documentReference")
            }
            .addOnFailureListener { e ->
                Log.d("OpenPayApp", "Location saved error, $e")
            }
    }

    override suspend fun getLocations(): CoroutineResult<List<Location>> {
        var status: PostDelayedResponseEnum = PostDelayedResponseEnum.POST_DELAYED_ERROR
        val postDelayedResponse: ArrayList<Location> = ArrayList()
        var postDelayedResponseError: String = EMPTY_STRING
        val done = CountDownLatch(ONE_INT)

        val db = Firebase.firestore

        db.collection(LOCATION_COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("OpenPayApp", "${document.id} => ${document.data}")

                    postDelayedResponse.add(
                        Location(
                            latitude = document.data[LATITUDE_PARAM] as Double,
                            longitude = document.data[LONGITUDE_PARAM] as Double
                        )
                    )
                }

                status = PostDelayedResponseEnum.POST_DELAYED_SUCCESS
                done.countDown()
            }
            .addOnFailureListener { exception ->
                Log.w("OpenPayApp", "Error getting documents.", exception)

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

    companion object {
        private const val LOCATION_COLLECTION = "location"

        private const val LONGITUDE_PARAM = "longitude"
        private const val LATITUDE_PARAM = "latitude"
    }
}
