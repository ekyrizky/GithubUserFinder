package com.ekyrizky.githubezfinder.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ekyrizky.githubezfinder.BuildConfig
import com.ekyrizky.githubezfinder.data.UserEntity
import com.ekyrizky.githubezfinder.view.HomeActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class FollowViewModel : ViewModel() {
    private val listItems = ArrayList<UserEntity>()
    private val listUsers = MutableLiveData<ArrayList<UserEntity>>()
    private val client = connectApi()

    private fun connectApi(): AsyncHttpClient {
        val apiKey = BuildConfig.API_KEY
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $apiKey")
        client.addHeader("User-Agent", "request")
        return client
    }

    fun getUser(): LiveData<ArrayList<UserEntity>> = listUsers

    fun setUserFollowers(context: Context, username: String) {
        val url = "https://api.github.com/users/${username}/followers"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                val result = String(responseBody)
                Log.d(HomeActivity.TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val userFollowers = jsonObject.getString("login")
                        getUserDetail(userFollowers, context)
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable) {
                val errorMsg = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message + " GIT"}"
                }
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun setUserFollowing(context: Context, username: String) {
        val url = "https://api.github.com/users/${username}/following"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                val result = String(responseBody)
                Log.d(HomeActivity.TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val userFollowing = jsonObject.getString("login")
                        getUserDetail(userFollowing, context)
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable) {
                val errorMsg = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message + " GIT"}"
                }

                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
            }
        })
    }



    private fun getUserDetail(username: String, context: Context) {
        val url = "https://api.github.com/users/${username}"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                val result = String(responseBody)
                Log.d(HomeActivity.TAG, result)

                try {
                    val jsonObject = JSONObject(result)
                    val userItems = UserEntity()
                    userItems.id = jsonObject.getInt("id")
                    userItems.username = jsonObject.getString("login")
                    userItems.name = jsonObject.getString("name")
                    userItems.avatar = jsonObject.getString("avatar_url")
                    userItems.bio = jsonObject.getString("bio")
                    userItems.company = jsonObject.getString("company")
                    userItems.location = jsonObject.getString("location")
                    userItems.url = jsonObject.getString("html_url")
                    userItems.email = jsonObject.getString("email")
                    userItems.repository = jsonObject.getInt("public_repos")
                    userItems.followers = jsonObject.getInt("followers")
                    userItems.following = jsonObject.getInt("following")
                    listItems.add(userItems)
                    listUsers.postValue(listItems)

                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable) {
                val errorMsg = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message + " GIT"}"
                }

                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
            }
        })
    }
}