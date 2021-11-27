package com.konkuk17.messenger_example.Setting

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.konkuk17.messenger_example.Chat.ChatService
import com.konkuk17.messenger_example.ChatRoom.MessageActivity
import com.konkuk17.messenger_example.Login.LoginActivity
import com.konkuk17.messenger_example.Main.IdViewModel
import com.konkuk17.messenger_example.R
import com.konkuk17.messenger_example.Signup.MyResponse
import com.konkuk17.messenger_example.databinding.FragmentSettingBinding
import com.konkuk17.messenger_example.databinding.SettingChangeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SettingFragment : Fragment() {

    lateinit var binding: FragmentSettingBinding
    lateinit var retrofit: Retrofit
    lateinit var settingService: SettingService

    var pref: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    val myIdViewModel: IdViewModel by activityViewModels<IdViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = activity?.getSharedPreferences("autologin", Activity.MODE_PRIVATE)
        editor = pref?.edit()

        init()
    }


    fun init() {
        //레트로핏 객체 만들기
        retrofit = Retrofit.Builder()
            .baseUrl("http://220.87.45.195:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        settingService = retrofit.create(SettingService::class.java)


        //ID
        binding.settingId.text = myIdViewModel.myId.value

        //NAME
        binding.settingName.text = myIdViewModel.myName.value

        //로그아웃
        binding.settingBtnLogout.setOnClickListener {

            editor?.clear()
            editor?.commit()

            activity?.finish()
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        //이름 변경
        binding.settingBtnNameChange.setOnClickListener {
            var dialog = Dialog(this@SettingFragment.requireContext())
            dialog.setContentView(R.layout.setting_change)
            dialog.show()

            dialog.findViewById<Button>(R.id.sc_btn_ok).setOnClickListener {
                Log.d("st", "확인버튼 누름")
                var user_id = myIdViewModel.myId.value
                var user_pw = dialog.findViewById<EditText>(R.id.sc_et_pwd).text.toString()
                var user_name = dialog.findViewById<EditText>(R.id.sc_et_new).text.toString()
                settingService.requestNameChange(user_id!!, user_pw, user_name)
                    .enqueue(object : Callback<MyResponse> {
                        override fun onResponse(
                            call: Call<MyResponse>,
                            response: Response<MyResponse>
                        ) {
                            var result = response.body()
                            if (result?.code == "0000") {
                                myIdViewModel.setMyNmae(user_name)

                                editor?.putString("loginName", user_name)
                                editor?.commit()

                                Toast.makeText(this@SettingFragment.context, "이름을 변경했습니다.", Toast.LENGTH_SHORT).show()
                                binding.settingName.text = myIdViewModel.myName.value
                            } else if (result?.code == "0001") {
                                Toast.makeText(this@SettingFragment.context, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                            } else if (result?.code == "0002") {
                                Toast.makeText(this@SettingFragment.context, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show()
                            }
                            dialog.dismiss()

                        }

                        override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                            Toast.makeText(this@SettingFragment.context, "통신실패", Toast.LENGTH_SHORT).show()
                            Log.d("st", t.message.toString())
                            dialog.dismiss()
                        }

                    })
            }

            dialog.findViewById<Button>(R.id.sc_btn_cancle).setOnClickListener {
                Toast.makeText(this@SettingFragment.context, "취소", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }

        //비번 변경
        binding.settingBtnPwdChange.setOnClickListener {
            var dialog = Dialog(this@SettingFragment.requireContext())
            dialog.setContentView(R.layout.setting_change)
            dialog.show()

            dialog.findViewById<TextView>(R.id.sc_tv_new).text = "New PWD"
            dialog.findViewById<EditText>(R.id.sc_et_new).hint = "새로운 비밀번호"

            dialog.findViewById<Button>(R.id.sc_btn_ok).setOnClickListener {

                Log.d("st", "확인버튼 누름")
                var user_id = myIdViewModel.myId.value
                var user_pw = dialog.findViewById<EditText>(R.id.sc_et_pwd).text.toString()
                var user_new_pw = dialog.findViewById<EditText>(R.id.sc_et_new).text.toString()
                settingService.requestPwdChange(user_id!!, user_pw, user_new_pw)
                    .enqueue(object : Callback<MyResponse> {
                        override fun onResponse(
                            call: Call<MyResponse>,
                            response: Response<MyResponse>
                        ) {
                            var result = response.body()
                            if (result?.code == "0000") {
                                Toast.makeText(this@SettingFragment.context, "비밀번호를 변경했습니다.", Toast.LENGTH_SHORT).show()
                            } else if (result?.code == "0001") {
                                Toast.makeText(this@SettingFragment.context, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                            } else if (result?.code == "0002") {
                                Toast.makeText(this@SettingFragment.context, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show()
                            }
                            dialog.dismiss()
                        }

                        override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                            Toast.makeText(this@SettingFragment.context, "통신실패", Toast.LENGTH_SHORT).show()
                            Log.d("st", t.message.toString())
                            dialog.dismiss()
                        }

                    })
            }

            dialog.findViewById<Button>(R.id.sc_btn_cancle).setOnClickListener {
                Toast.makeText(this@SettingFragment.context, "취소", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }


    }
}