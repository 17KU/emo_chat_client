package com.konkuk17.messenger_example.Signup

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.konkuk17.messenger_example.Login.LoginActivity
import com.konkuk17.messenger_example.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init() {
        //retrofit 객체 만들기
        var retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.219.106:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var signupService = retrofit.create(SignupService::class.java)

        binding.apply {

            btnDupliCheck.setOnClickListener {
                //나중에 구현
                var user_id = textId.text.toString()
                signupService.requestCheck(user_id)
                    .enqueue(object : Callback<MyResponse>{
                        override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>) {

                            var signup = response.body()

                            var dialog = AlertDialog.Builder(this@SignupActivity)

                            if(signup?.code.equals("0000")){
                                dialog.setTitle("사용 가능한 아이디")
                                    .setMessage("사용 가능한 아이디입니다.")
                                    .setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                                    }
                                    .show()
                            }
                            else if(signup?.code.equals("0001")){
                                dialog.setTitle("사용 불가능한 아이디")
                                    .setMessage("동일한 아이디가 이미 존재합니다.")
                                    .setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                                    }
                                    .show()
                            }
                            else{
                                dialog.setTitle("중복체크 오류")
                                    .setMessage("code = " + signup?.code + ", msg = " + signup?.msg)
                                    .setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                                    }
                                    .show()
                            }
                        }

                        override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                            Log.d("signup debug", t.message.toString())
                            var dialog = AlertDialog.Builder(this@SignupActivity)
                                .setTitle("실패")
                                .setMessage("웹 통신에 실패했습니다.")
                                .setPositiveButton("확인") { _, _ ->
                                }
                                .show()
                        }


                    })

            }

            btnSignupFinish.setOnClickListener {

                var user_id = textId.text.toString()
                var user_pw = textPw.text.toString()
                var user_name = textName.text.toString()

                signupService.requestSingup(user_id, user_pw, user_name)
                    .enqueue(object : Callback<MyResponse> {

                        //웹통신에 성공했을때 실행되는 코드. 응답값을 받아옴.
                        override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>) {
                            var signup = response.body()

                            var dialog = AlertDialog.Builder(this@SignupActivity)

                            if(signup?.code.equals("0000")){
                                dialog.setTitle("회원가입 성공")
                                    .setMessage("회원가입에 성공했습니다.")
                                    .setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                                        var loginIntent =
                                            Intent(this@SignupActivity, LoginActivity::class.java)
                                        startActivity(loginIntent)
                                    }
                                    .show()
                            }
                            else if(signup?.code.equals("0001")){
                                dialog.setTitle("회원가입 실패")
                                    .setMessage("동일한 아이디가 존재합니다.")
                                    .setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                                    }
                                    .show()
                            }
                            else{
                                dialog.setTitle("회원가입 오류")
                                    .setMessage("code = " + signup?.code + ", msg = " + signup?.msg)
                                    .setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                                    }
                                    .show()
                            }
                        }

                        //웹통신에 실패했을때 실행되는 코드
                        override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                            Log.d("signup debug", t.message.toString())
                            var dialog = AlertDialog.Builder(this@SignupActivity)
                                .setTitle("실패")
                                .setMessage("웹 통신에 실패했습니다.")
                                .setPositiveButton("확인") { _, _ ->
                                    // 여기서 할일 정의해야함
                                }
                                .show()
                        }
                    })

            }

            btnSignupCancle.setOnClickListener {
                finish()
            }

            signupBack.setOnClickListener {
                finish()
            }
        }

    }
}