package com.example.liberary.ViewModel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.liberary.Course
import com.example.liberary.DTO.Prefs
import com.example.liberary.LocalModel.LocalModel
import com.example.liberary.LocalModel.Shared
import kotlinx.coroutines.flow.*


class ViewModel:ViewModel() {
    private lateinit var shared:Shared

    fun initContext(context:AppCompatActivity){
        LocalModel.context = context
        shared = Shared(context)
    }

    suspend fun addToFavorite(course: Course): Flow<Boolean> {
        return  flow{
            emit(LocalModel.write(course).single())
        }
    }
    suspend fun getData():Flow<ArrayList<Course>> {
        return flow{
            emit(LocalModel.getAll().single().filter { !it.isRecent } as ArrayList<Course>)
        }
    }
    fun clearAllData(){
            removeAllShared()
            LocalModel.clearAll()
        }
    suspend fun getPreferences():Flow<Prefs>{
        return flow {
            emit(shared.getPrefrences().single())
        }
    }
     fun saveToPreferences(language:String){
        shared.saveLanguage(language)
    }
    fun saveDarkModeState(isDarkMode:Boolean){
        shared.saveDarkMode(isDarkMode)
    }
    private fun removeAllShared(){
        shared.removeAllShared()
    }
    suspend fun getOPendRecent():Flow<ArrayList<Course>> {
        return flow {
            val recent = LocalModel.getAll().single().filter { it.isRecent }
            if (recent.isNotEmpty()){
                emit(arrayListOf<Course>(recent.last()))
            }else{
                emit(arrayListOf<Course>())
            }
        }
    }
    fun removeRecent(){
        LocalModel.removeRecent()
    }
    fun removeAllRecentExceptLastItem(){
        //LocalModel.removeAllRecentExceptLastItem()
    }
}