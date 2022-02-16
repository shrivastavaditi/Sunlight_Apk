package com.example.sunlight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.Sunlight.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_splashs.*

class splashs : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashs)
        sunlight.alpha=0f
        sunlight.animate().setDuration(2000).alpha(1f).withEndAction{
        val intent = Intent(this,choose_mood::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        finish()
        }

    }
}
