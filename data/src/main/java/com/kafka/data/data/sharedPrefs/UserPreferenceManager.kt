package com.kafka.data.data.sharedPrefs

import android.app.Application
import android.content.Context
import com.kafka.data.data.annotations.UseInjection
import com.kafka.data.data.annotations.UseSingleton

/**
 * Created by Aditya Mehta on 26/03/18.
 */
@UseSingleton
@UseInjection
class UserPreferenceManager constructor(context: Application) :
    SharedPreferenceManager(context) {

    //named default to make it compatible with older versions of airtel tv
    private val appPreferenceName = "default"

    init {
        pref = context.getSharedPreferences(appPreferenceName, Context.MODE_PRIVATE)
    }
}

//TODO: make instance variables
const val KEY_USER_TOKEN = "user_token"
const val KEY_USER_UID = "user_uid"
const val IS_USER_LOGGED_IN = "is_user_logged_in"
