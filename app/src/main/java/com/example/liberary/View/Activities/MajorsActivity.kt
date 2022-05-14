package com.example.liberary.View.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.liberary.Major
import com.example.liberary.Adapters.MajorAdapter
import com.example.liberary.R
import com.example.liberary.constants.Constants


class MajorsActivity : AppCompatActivity() {
    private lateinit var majorsArray:ArrayList<Major>
    private lateinit var adapter: MajorAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_majors)


        majorsArray = intent.getSerializableExtra(Constants.major)  as ArrayList<Major>
        val recyclerView :RecyclerView = findViewById(R.id.majorRecyclerView)
        adapter = MajorAdapter(majorsArray) { position -> onListItemClick(position) }

        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

    }
    private fun onListItemClick(position: Int) {

        val myIntent = Intent(this, HomeActivity::class.java)

        myIntent.putExtra(Constants.courses, majorsArray.get(position).courses)
        startActivity(myIntent)
    }
}