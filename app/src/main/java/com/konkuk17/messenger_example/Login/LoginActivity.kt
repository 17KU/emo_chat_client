package com.konkuk17.messenger_example.Login

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseUser
import com.konkuk17.messenger_example.Main.MainActivity
import com.konkuk17.messenger_example.Signup.SignupActivity
import com.konkuk17.messenger_example.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    lateinit var retrofit: Retrofit
    lateinit var loginService: LoginService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init() {
        //retrofit 객체 만들기
        retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.219.106:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        loginService = retrofit.create(LoginService::class.java)


        //초기화
        pref = getSharedPreferences("autologin", Activity.MODE_PRIVATE)
        editor = pref.edit()

        binding.apply {
            btnSignup.setOnClickListener {
                var signupIntent = Intent(this@LoginActivity, SignupActivity::class.java)
                startActivity(signupIntent)
            }

            btnLogin.setOnClickListener {
                startLogin(id.text.toString(), pw.text.toString())
            }
        }


    }


    fun startLogin(id: String, pw: String) {

        var user_id = id
        var user_pw = pw

        loginService.requestLogin(user_id, user_pw).enqueue(object : Callback<Login> {
            //웹통신에 성공했을때 실행되는 코드. 응답값을 받아옴.
            override fun onResponse(call: Call<Login>, response: Response<Login>) {

                var login = response.body()

                var dialog = AlertDialog.Builder(this@LoginActivity)

                if (login?.code.equals("0000")) {

                    //Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_LONG).show()
                    editor.putString("loginId", login?.user_id)
                    editor.putString("loginName", login?.user_name)
                    editor.commit()

                    var mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
                    mainIntent.putExtra("myId", login?.user_id)
                    mainIntent.putExtra("myName", login?.user_name)
                    startActivity(mainIntent)
                    finish()

                } else if (login?.code.equals("0001")) {
                    dialog.setTitle("로그인 실패")
                    dialog.setMessage("존재하지 않는 아이디입니다.")
                    dialog.setPositiveButton("확인") { _, _ ->
                    }
                    dialog.show()
                } else if (login?.code.equals("0002")) {
                    dialog.setTitle("로그인 실패")
                    dialog.setMessage("비밀번호가 틀렸습니다.")
                    dialog.setPositiveButton("확인") { _, _ ->
                    }
                    dialog.show()
                } else {
                    dialog.setTitle("로그인 오류")
                    dialog.setMessage("code = " + login?.code + ", msg = " + login?.msg + ", user_id = " + login?.user_id + ", user_name = " + login?.user_name)
                    dialog.setPositiveButton("확인") { _, _ ->
                    }
                    dialog.show()
                }

            }

            //웹통신에 실패했을때 실행되는 코드
            override fun onFailure(call: Call<Login>, t: Throwable) {
                Log.d("debug", t.message.toString())
                var dialog = AlertDialog.Builder(this@LoginActivity)
                dialog.setTitle("실패!")
                dialog.setMessage("웹 통신에 실패했습니다.")
                dialog.setPositiveButton("확인") { _, _ ->

                }
                dialog.show()
            }
        })

    }

    override fun onStart() {
        super.onStart()

        moveMainPage()
    }

    fun moveMainPage() {

        //자동 로그인 확인
        //초기화
        pref = getSharedPreferences("autologin", Activity.MODE_PRIVATE)
        editor = pref.edit()

        var loginId: String? = pref.getString("loginId", null)
        var loginName: String? = pref.getString("loginName", null)

        if (loginId != null && loginName != null) {
            var mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
            mainIntent.putExtra("myId", loginId)
            mainIntent.putExtra("myName", loginName)
            startActivity(mainIntent)
            finish()
        }
    }
}