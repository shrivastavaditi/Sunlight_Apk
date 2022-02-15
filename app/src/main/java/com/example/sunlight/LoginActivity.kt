package com.example.sunlight

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.Sunlight.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*


const val RC_SIGN_IN = 123

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient
//    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {


        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        login_btn.setOnClickListener {
            when {
                TextUtils.isEmpty(login_email.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please Enter Your Email", Toast.LENGTH_SHORT).show()

                }

                TextUtils.isEmpty(login_password.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show()

                }

                !Patterns.EMAIL_ADDRESS.matcher(login_email.text.toString()).matches() -> {
                    Toast.makeText(
                        applicationContext, "Invalid email address",
                        Toast.LENGTH_SHORT
                    ).show()
                }


                else -> {
                    val email: String = login_email.text.toString().trim() { it <= ' ' }
                    val password: String = login_password.text.toString().trim() { it <= ' ' }
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                Toast.makeText(
                                    this,
                                    "You Logged in Successfully.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                var intent = Intent(this, choose_mood::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra(
                                    "UserId",
                                    FirebaseAuth.getInstance().currentUser!!.uid
                                )
                                intent.putExtra("email", email)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this, task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }


            }
        }


        tv_newuser.setOnClickListener {
            val intent = Intent(this, SignUp_Fragement::class.java)
            startActivity(intent)
        }

        tv_forgotpass.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
         mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        sign_in_button.visibility = View.VISIBLE
        firebaseAuth = FirebaseAuth.getInstance()

        sign_in_button.setSize(SignInButton.SIZE_STANDARD)
        sign_in_button.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
            Toast.makeText(this, "Logging In", Toast.LENGTH_SHORT).show()
        }

        val acct = GoogleSignIn.getLastSignedInAccount(this)

        if (acct != null) {
            sign_in_button.visibility = View.VISIBLE
            val personName = acct.displayName
            val personGivenName = acct.givenName
            val personFamilyName = acct.familyName
            val personEmai = acct.email
            val personId = acct.id
            val personPhoto: Uri? = acct.photoUrl
        }


    }

    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
//            }
            val account = completedTask.getResult(ApiException::class.java)

            sign_in_button.visibility = View.VISIBLE
            // Signed in successfully, show authenticated UI.
            val intent = Intent(this, choose_mood::class.java)
            intent.putExtra(choose_mood.NAME, account.displayName)
            var email = account.email.toString()
            intent.putExtra("email", email)
            startActivity(intent)
            finish()


        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            sign_in_button.visibility = View.VISIBLE

        }
    }

}




