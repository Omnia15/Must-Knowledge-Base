package com.example.liberary


import io.realm.*
import java.io.Serializable
import kotlin.collections.ArrayList
data class Faculty(val FacultyName:String,val image:Int, val majors:ArrayList<Major>):Serializable

data class Major(val majorName:String,val courses: ArrayList<Course>):Serializable

open class Course(): RealmObject(),Serializable,Cloneable{
    var isRecent:Boolean = false
    var courseName:String = ""
    var courseCode:String = ""
    var courseDescription:String = ""
    var level:String = ""
    var refreces:String = ""
    var preRequest:String = ""
    constructor(courseName:String, courseCode:String, courseDescription:String, level:String, refreces:String, preRequest:String) : this() {
        this.courseName =  courseName
        this.courseCode = courseCode
        this.courseDescription = courseDescription
        this.level = level
        this.preRequest = preRequest
        this.refreces = refreces
    }
   public override fun clone(): Course {
        val course = Course()
        course.isRecent = isRecent
        course.courseCode = courseCode
        course.courseName = courseName
        course.courseDescription = courseDescription
        course.level = level
        course.preRequest = preRequest
        course.refreces = refreces
        return course
    }
    }