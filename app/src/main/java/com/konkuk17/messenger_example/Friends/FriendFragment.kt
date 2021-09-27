package com.konkuk17.messenger_example.Friends

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.konkuk17.messenger_example.Main.IdViewModel

import com.konkuk17.messenger_example.databinding.FragmentFriendBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FriendFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var myViewModel: IdViewModel

    lateinit var binding: FragmentFriendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(IdViewModel::class.java)
        binding = FragmentFriendBinding.inflate(inflater, container, false)

        //return inflater.inflate(R.layout.fragment_friend, container, false)
        return binding.root

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FriendFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun init(){
        var retrofit = Retrofit.Builder()
            .baseUrl("http://3.36.165.136:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var friendService = retrofit.create(FriendService::class.java)
        binding.apply{
            addFriendBtn.setOnClickListener{

                var user_id = "youm"
                var add_friend_id = addFriendEtxt.editText.toString()

                friendService.AddFriend(user_id,add_friend_id).enqueue(object :
                    Callback<AddFriend> {
                    override fun onResponse(call: Call<AddFriend>, response: Response<AddFriend>) {
                        TODO("Not yet implemented")

                        var add_friend = response.body()

                        var dialog = AlertDialog.Builder(this@FriendFragment.requireContext())
                        dialog.setTitle("친구추가")
                        dialog.setMessage("id = ")

                    }

                    override fun onFailure(call: Call<AddFriend>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })

            }


        }
    }
}