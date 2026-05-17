package com.example.prathamchikisthehealthcare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        val list = listOf(
            EmergencyModel("Burn", R.mipmap.ic_launcher),
            EmergencyModel("Snake Bite", R.mipmap.ic_launcher),
            EmergencyModel("Fracture", R.mipmap.ic_launcher),
            EmergencyModel("CPR", R.mipmap.ic_launcher),
            EmergencyModel("Choking", R.mipmap.ic_launcher),
            EmergencyModel("Bleeding", R.mipmap.ic_launcher)
        )

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = EmergencyAdapter(list)
    }
}
