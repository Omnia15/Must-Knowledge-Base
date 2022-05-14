package com.example.liberary.View.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.liberary.Course
import com.example.liberary.Adapters.CoursesAdapter
import com.example.liberary.R
import com.example.liberary.View.Activities.DetailsActivity
import com.example.liberary.constants.Constants
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {
    private var filterdCourses:ArrayList<Course> = ArrayList()

    private lateinit var adapter: CoursesAdapter
    private lateinit var searchBar:SearchView
    private lateinit var courses:ArrayList<Course>
    private var isSearching = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        courses = arguments?.get(Constants.homeCourses) as ArrayList<Course>


        val view = inflater.inflate(R.layout.fragment_home, container, false)

        searchBar = view.findViewById(R.id.searchView)
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                isSearching = newText.isNotEmpty()

                filter(newText)
                return true
            }})
        val recyclerView:RecyclerView =  view.findViewById(R.id.recyclerviewCourses)
        adapter = CoursesAdapter(courses) {
            getPressesdItemIndex(it)
        }
        val layoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        return view
    }


    private fun filter(text:String){
        filterdCourses.clear()
        courses.map { i ->
            if (i.courseCode.lowercase().contains(text.lowercase())) {
                filterdCourses.add(i)
            }
        }
        adapter.filterCourses(filterdCourses)

    }
    private fun getPressesdItemIndex(index:Int){
        if (isSearching){
            moveToNewActivity(filterdCourses[index])
        }else{
            moveToNewActivity(courses.get(index))
        }
    }
    private fun moveToNewActivity(withCourse: Course) {
        val i = Intent(activity, DetailsActivity::class.java)
        i.putExtra(Constants.details,withCourse)
        startActivity(i)
    }
}