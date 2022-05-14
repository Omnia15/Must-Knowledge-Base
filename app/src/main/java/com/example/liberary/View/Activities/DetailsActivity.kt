package com.example.liberary.View.Activities



import android.content.Intent
import android.os.Bundle
import android.webkit.URLUtil
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.liberary.Adapters.PrerequisiteAdapter
import com.example.liberary.Course
import com.example.liberary.LocalModel.LocalModel
import com.example.liberary.R
import com.example.liberary.ViewModel.ViewModel
import com.example.liberary.constants.Constants
import kotlinx.coroutines.launch


class DetailsActivity : AppCompatActivity() {
    private var viewModel = ViewModel()
    private lateinit var course: Course
    private lateinit var courseName: TextView
    private lateinit var courseID: TextView
    private lateinit var courseIntroduction: TextView
    private lateinit var favorite: ImageButton
    private lateinit var recentCourse: Course
    private lateinit var refrences: Button
    private lateinit var level:TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PrerequisiteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        findViewByid()
        course = intent.getSerializableExtra(Constants.details) as Course
        //val aray =
        adapter = PrerequisiteAdapter(course.preRequest.split(","))
        recyclerView.adapter = adapter
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        adapter.notifyDataSetChanged()


        bindDataToView()

        refrences.setOnClickListener {

            val refrence = course.refreces


            if (URLUtil.isValidUrl(refrence)) {
                val pdfIntent = Intent(this@DetailsActivity,PDFActivity::class.java)
                pdfIntent.putExtra(Constants.reference,refrence)
                startActivity(pdfIntent)


                Toast.makeText(this,"start loading",Toast.LENGTH_LONG).show()
            } else {
                showAlert(
                    resources.getString(R.string.Error),
                    resources.getString(R.string.notValidURL)
                )
            }
        }

        LocalModel.context = this

        recentCourse = course.clone()
        course.isRecent = false
        recentCourse.isRecent = true
        lifecycleScope.launch {
            viewModel.addToFavorite(recentCourse).collect {
            }
        }
        favorite.setOnClickListener {
            addCourse(course)
        }
    }

    private fun findViewByid() {
        courseName = findViewById(R.id.courseDetailsNameValue)
        courseID = findViewById(R.id.courseDetailsIDValue)
        //coursePrerequest = findViewById(R.id.prerequisite)
        courseIntroduction = findViewById(R.id.introductionValue)
        favorite = findViewById(R.id.addToFavorite)
        refrences = findViewById(R.id.refrncebtn)
        level = findViewById(R.id.levelvalue)
        recyclerView = findViewById(R.id.prerequisites_RV)

    }

    private fun bindDataToView() {
        courseName.text = course.courseName
        courseID.text = course.courseCode
        courseIntroduction.text = course.courseDescription
        //coursePrerequest.text = course.preRequest
        level.text = course.level
    }

    private fun addCourse(course: Course) {
        lifecycleScope.launch {
            viewModel.addToFavorite(course).collect {
                if (it) {
                    showAlert(
                        resources.getString(R.string.Done),
                        resources.getString(R.string.isnotBefore)
                    )
                } else {
                    showAlert(
                        resources.getString(R.string.Error),
                        resources.getString(R.string.isBefore)
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel.removeRecent()
    }

    private fun showAlert(header: String, body: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(header)
        builder.setMessage(body)
        builder.show()
    }
}