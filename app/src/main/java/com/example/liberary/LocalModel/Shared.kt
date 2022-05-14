package com.example.liberary.LocalModel

import android.content.Context
import android.content.SharedPreferences

import androidx.appcompat.app.AppCompatActivity
import com.example.liberary.DTO.Prefs

import com.example.liberary.constants.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Shared(var context: AppCompatActivity) {
    private val sharedPreference=this.context.getSharedPreferences(Constants.sharedName, Context.MODE_PRIVATE)
    private val editor = sharedPreference.edit()

    fun saveLanguage(language:String){
        editor.putString(Constants.language,language)
        editor.commit()
    }
    fun saveDarkMode(isDark:Boolean){
        editor.putBoolean(Constants.darkMode,isDark)
        editor.commit()
    }
    fun getPrefrences(): Flow<Prefs> {
        return flow {
            emit(Prefs(sharedPreference.getString(Constants.language,Constants.noData)!!,
                sharedPreference.getBoolean(Constants.darkMode, false)
            ))
        }
    }
    fun removeAllShared(){
        editor.clear()
        editor.commit()
    }
}