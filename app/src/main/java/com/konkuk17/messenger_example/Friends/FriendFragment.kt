package com.konkuk17.messenger_example.Friends

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonArray
import com.konkuk17.messenger_example.Main.IdViewModel

import com.konkuk17.messenger_example.databinding.FragmentFriendBinding
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
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

    var friendlist = arrayListOf<FriendRecycleViewData>()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()


    }

    fun init(){
        Log.d("initTest","init이 됐는지")


        var retrofit = Retrofit.Builder()
            .baseUrl("http://3.36.165.136:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var friendService = retrofit.create(FriendService::class.java)

        binding.apply{
            addFriendBtn.setOnClickListener{

                var user_id = "youm"
                var add_friend_id = addFriendEtxt.text.toString()


                friendService.AddFriend(user_id,add_friend_id).enqueue(object :
                    Callback<AddFriendOutput> {
                    override fun onResponse(call: Call<AddFriendOutput>, response: Response<AddFriendOutput>) {

                        var add_friend = response.body()
                        /*
                        var dialog = AlertDialog.Builder(this@FriendFragment.requireContext())
                        dialog.setTitle("친구추가")
                        dialog.setMessage("id = "+add_friend?.add_friend_id)
                        dialog.setPositiveButton("OK"){_,_->}
                        dialog.show()*/

                        var dialog = AlertDialog.Builder(this@FriendFragment.requireContext())

                        if(add_friend?.code.equals("0000")){
                            dialog.setTitle("친구추가")
                            dialog.setMessage("id = "+add_friend?.add_friend_id)
                            dialog.setPositiveButton("OK"){_,_->}
                            dialog.show()
                        }

                        else{
                            dialog.setTitle("실패")
                            dialog.setMessage(add_friend?.code + "   " + add_friend?.msg)
                            dialog.setPositiveButton("OK"){_,_->}
                            dialog.show()
                        }
                    }

                    override fun onFailure(call: Call<AddFriendOutput>, t: Throwable) {

                    }
                })

            }

            val friendAdapter = FriendRecycleViewAdapter(this@FriendFragment.requireContext(), friendlist)

            friendRecycleView.adapter = friendAdapter

            val linearLayoutManager = LinearLayoutManager(this@FriendFragment.requireContext())
            friendRecycleView.layoutManager = linearLayoutManager
            friendRecycleView.setHasFixedSize(true)


            friendlist.apply{
                add(FriendRecycleViewData(name="asdf",photo="1"))
                friendAdapter.notifyDataSetChanged()
            }

            var user_id = "youm"


            friendService.ShowFriend(user_id).enqueue(object :
                Callback<List<JsonArray>> {
                override fun onResponse(
                    call: Call<List<JsonArray>>,
                    response: Response<List<JsonArray>>
                ) {
                    val friendData = JSONTokener(response.message()).nextValue() as JSONArray

                    for(i in 0 until friendData.length()){
                        val id = friendData.getJSONObject(i).getString("uf_friend_id")
                        Log.d("friendTest",id)
                        val img = friendData.getJSONObject(i).getString("uf_user_id_id")
                        Log.d("friendTest",img)
                        friendlist.apply{
                            add(FriendRecycleViewData(name=id,photo=img))
                            friendAdapter.notifyDataSetChanged()
                        }
                    }


                }


                override fun onFailure(call: Call<List<JsonArray>>, t: Throwable) {
                    Log.d("friendTest","실패했음")
                }

            })


        }
    }
}