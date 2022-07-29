package com.example.marvel.data.remote

import android.util.Log
import com.example.marvel.data.remote.responses.ServerCharacter
import com.example.marvel.data.repositories.RemoteProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp
import javax.inject.Inject

class RemoteClient @Inject constructor(private val api: MarvelAPI) : RemoteProvider {

    override suspend fun getCharacters(): List<ServerCharacter> {
        return withContext(Dispatchers.IO) {
            val data = api.getCharacters(timeStamp, PUBLIC_KEY, toMD5())
            Log.d("JOSE", "$data")
            data.body()?.data?.results ?: emptyList()
        }
    }

    private val timeStamp = Timestamp(System.currentTimeMillis()).time.toString()

    private fun toMD5() : String {
        val input = "$timeStamp$PRIVATE_KEY$PUBLIC_KEY"
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray()))
            .toString(16).padStart(32, '0')
    }

    companion object {
        const val PUBLIC_KEY = "7d81013872ce36a712ff431dfcee42a7"
        const val PRIVATE_KEY = "c1a7690d21b6c2e17d48412256f7ddc6107bd2e2"
    }
}