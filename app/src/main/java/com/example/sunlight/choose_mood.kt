package com.example.sunlight

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.Sunlight.R
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_choose_mood.*

class choose_mood : AppCompatActivity() {

    companion object {
        const val NAME = "NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_mood)
        card_happy.setOnClickListener { startActivity(Intent(this, CardHappy::class.java)) }
        card_sad.setOnClickListener { startActivity(Intent(this, CardSad::class.java)) }
        card_tired.setOnClickListener { startActivity(Intent(this, CardTired::class.java)) }
        card_anger.setOnClickListener { startActivity(Intent(this, CardAnger::class.java)) }
//        val intent = getIntent()
//        val name = intent.getStringExtra(NAME)
//        name_text.text = "HEY!!  " + name

//        FirebaseAuth.getInstance()
//        checkUser()


        val sharedPref = this?.getPreferences(Context.MODE_PRIVATE) ?: return
        val isLogin = sharedPref.getString("Email", "1")
        logout_choosemood.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            sharedPref.edit().remove("Email").apply()
            Toast.makeText(
                this,
                "You Logged Out Successfully",
                Toast.LENGTH_SHORT
            )
                .show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        if (isLogin == "1") {
            var email = intent.getStringExtra("email")
            if (email != null) {
                with(sharedPref.edit())
                {
                    putString("Email", email)
                    apply()
                }
            } else {
                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }


}

