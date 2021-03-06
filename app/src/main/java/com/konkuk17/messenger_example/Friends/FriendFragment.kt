package com.konkuk17.messenger_example.Friends

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonArray
import com.konkuk17.messenger_example.ChatRoom.MessageActivity
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

    var favoritelist = arrayListOf<FriendRecycleViewData>()

    val myIdViewModel: IdViewModel by activityViewModels<IdViewModel>()

    lateinit var binding: FragmentFriendBinding

    lateinit var mContext: Context

    lateinit var friendService: FriendService

    lateinit var friendAdapter: FriendRecycleViewAdapter

    lateinit var favoriteAdapter: FriendRecycleViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        mContext = this@FriendFragment.requireContext()
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
        Log.d("initTest","init??? ?????????")

        //retrofit ??????
        var retrofit = Retrofit.Builder()
            .baseUrl("http://220.87.45.195:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //friendService API ??????
        friendService = retrofit.create(FriendService::class.java)

        binding.apply{

            var user_id = myIdViewModel.myId.value.toString()
            var user_name = myIdViewModel.myName.value.toString()


            favoriteAdapter = FriendRecycleViewAdapter(this@FriendFragment.requireContext(), favoritelist, friendService, user_id, user_name){ friendRecycleViewData ->

                var favorite_add = friendRecycleViewData.id

                //Toast.makeText(this@FriendFragment.requireContext(),favorite_add,Toast.LENGTH_LONG).show()

                //?????? ???????????? ??????
                //???????????? ?????? ????????? ????????? ?????? ????????? ?????? ??????
                friendService.AddFavorite(user_id,favorite_add).enqueue(object :
                    Callback<AddFriendOutput>{
                    override fun onResponse(
                        call: Call<AddFriendOutput>,
                        response: Response<AddFriendOutput>
                    ) {
                        var favorite = response.body()
                        favoriteAdapter.filter_favorite()

                        if(favorite?.code.equals("0003")){
                            friendRecycleViewData.favorite = "true"
                        }
                        else if(favorite?.code.equals("0002")){
                            friendRecycleViewData.favorite = "false"
                        }

                        FriendListUpdate(friendService,friendAdapter,friendlist)
                        favoriteListUpdate(friendService,favoriteAdapter,favoritelist)

                    }

                    override fun onFailure(call: Call<AddFriendOutput>, t: Throwable) {

                    }

                }
                )
            }

            favoriteRecyclerview.adapter = favoriteAdapter
            val linearLayoutManager2 = LinearLayoutManager(this@FriendFragment.requireContext())
            favoriteRecyclerview.layoutManager = linearLayoutManager2
            favoriteRecyclerview.setHasFixedSize(true)

            //FriendListUpdate(friendService,favoriteAdapter,favoritelist)
            //favoriteAdapter.filter_favorite()
            favoriteListUpdate(friendService,favoriteAdapter,favoritelist)

            //recycler view?????? ??? ?????????
            friendAdapter = FriendRecycleViewAdapter(this@FriendFragment.requireContext(), friendlist, friendService, user_id, user_name){ friendRecycleViewData ->

                var favorite_add = friendRecycleViewData.id

                //Toast.makeText(this@FriendFragment.requireContext(),favorite_add,Toast.LENGTH_LONG).show()

                //?????? ???????????? ??????
                //???????????? ?????? ????????? ????????? ?????? ????????? ?????? ??????
                friendService.AddFavorite(user_id,favorite_add).enqueue(object :
                    Callback<AddFriendOutput>{
                    override fun onResponse(
                        call: Call<AddFriendOutput>,
                        response: Response<AddFriendOutput>
                    ) {
                        var favorite = response.body()
                        favoriteAdapter.filter_favorite()



                        if(favorite?.code.equals("0003")){
                            friendRecycleViewData.favorite = "true"
                        }
                        else if(favorite?.code.equals("0002")){
                            friendRecycleViewData.favorite = "false"
                        }

                        FriendListUpdate(friendService,friendAdapter,friendlist)
                        favoriteListUpdate(friendService,favoriteAdapter,favoritelist)

                    }

                    override fun onFailure(call: Call<AddFriendOutput>, t: Throwable) {

                    }

                }
                )
            }

            friendAdapter.friendListClickListener = object : FriendRecycleViewAdapter.FriendListClickListener{
                override fun onFriendListLongClick(position: Int, item: FriendRecycleViewData) {
                    Log.d("test",item.id)

                    var dialog = AlertDialog.Builder(this@FriendFragment.requireContext())
                        .setTitle("?????? ??????")
                        .setMessage(item.name+"?????? ???????????? ?????????????????????????")
                        .setPositiveButton("??????", DialogInterface.OnClickListener { dialog, which ->
                            friendService.DeleteFriend(user_id, item.id)
                                .enqueue(object : Callback<DeleteFriendOutput>{
                                    override fun onResponse(
                                        call: Call<DeleteFriendOutput>,
                                        response: Response<DeleteFriendOutput>
                                    ) {
                                        var result = response.body()
                                        if(result?.code.equals("0000")){
                                            FriendListUpdate(friendService,friendAdapter,friendlist)
                                            favoriteListUpdate(friendService,favoriteAdapter,favoritelist)
                                            favoriteAdapter.filter_favorite()
                                        }
                                        else if(result?.code.equals("0001")){
                                            Toast.makeText(this@FriendFragment.requireContext(), "????????? ??????.", Toast.LENGTH_SHORT).show()

                                        }
                                        else if(result?.code.equals("0002")){
                                            Toast.makeText(this@FriendFragment.requireContext(), "?????? ?????? ??????.", Toast.LENGTH_SHORT).show()

                                        }
                                        else if(result?.code.equals("0003")){
                                            Toast.makeText(this@FriendFragment.requireContext(), "????????? ??????.", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<DeleteFriendOutput>,
                                        t: Throwable
                                    ) {
                                        TODO("Not yet implemented")
                                    }
                                })
                        })
                        .setNegativeButton("??????", DialogInterface.OnClickListener { dialog, which ->

                        })
                    dialog.show()
                }

            }

            friendRecycleView.adapter = friendAdapter

            val linearLayoutManager = LinearLayoutManager(this@FriendFragment.requireContext())
            friendRecycleView.layoutManager = linearLayoutManager
            friendRecycleView.setHasFixedSize(true)


            //??????????????????
            addFriendBtn.setOnClickListener{


                myIdViewModel.setFriendList(friendlist)
                val intent = Intent(this@FriendFragment.requireContext(), AddFriendActivity::class.java)
                intent.putExtra("myUid",myIdViewModel.myId.value)
                startActivityForResult(intent,100)
            }


            //?????? ??????
            findFriendBtn.setOnClickListener{

                myIdViewModel.setFriendList(friendlist)
                val intent2 = Intent(this@FriendFragment.requireContext(), SearchFriendActivity::class.java)
                intent2.putExtra("myUid",myIdViewModel.myId.value)
                intent2.putExtra("myName",myIdViewModel.myName.value)
                //intent2.putExtra("fList",myIdViewModel.friendList.value)
                intent2.putExtra("fList", friendlist)
                startActivityForResult(intent2,101)
            }


            //?????? ?????? ??????
            FriendListUpdate(friendService,friendAdapter,friendlist)

        }
    }


    // Activity Result ??? ?????? ?????? ???????????? ????????????
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            100->{
                FriendListUpdate(friendService,friendAdapter,friendlist)
            }
            101->{
                FriendListUpdate(friendService,friendAdapter,friendlist)
            }

        }

    }



    //?????? ?????? ??????
    fun FriendListUpdate(friendService: FriendService, friendAdapter:FriendRecycleViewAdapter, friendlist : ArrayList<FriendRecycleViewData>){
        //IdViewModel?????? ????????? id??? ????????????
        var user_id = myIdViewModel.myId.value.toString()

        //API ??????
        friendService.ShowFriend(user_id).enqueue(object :
            Callback<List<ShowFriendOutput>> {
            override fun onResponse(
                call: Call<List<ShowFriendOutput>>,
                response: Response<List<ShowFriendOutput>>
            ) {

                //????????? ?????????
                friendlist.clear()

                val friendData : List<ShowFriendOutput>? = response.body()

                //?????? ??????
                if(friendData != null){
                    for(friend in friendData){
                        friendlist.apply{
                            add(FriendRecycleViewData(friend.uf_friend_name.toString(),friend.uf_friend_id.toString(),friend.uf_favorite_state.toString()))
                            friendlist.sortWith(compareBy({it.name}))
                            friendAdapter.notifyDataSetChanged()
                        }
                        Log.d("friendList LOG", "setFriendList ??????")
                        myIdViewModel.setFriendList(friendlist)
                    }
                }


            }


            override fun onFailure(call: Call<List<ShowFriendOutput>>, t: Throwable) {
                Log.d("friendTest","????????????")
            }

        })
    }

    fun favoriteListUpdate(friendService: FriendService, friendAdapter:FriendRecycleViewAdapter, friendlist : ArrayList<FriendRecycleViewData>){
        //IdViewModel?????? ????????? id??? ????????????
        var user_id = myIdViewModel.myId.value.toString()

        //API ??????
        friendService.ShowFriend(user_id).enqueue(object :
            Callback<List<ShowFriendOutput>> {
            override fun onResponse(
                call: Call<List<ShowFriendOutput>>,
                response: Response<List<ShowFriendOutput>>
            ) {

                //????????? ?????????
                friendlist.clear()

                val friendData : List<ShowFriendOutput>? = response.body()

                //?????? ??????
                if(friendData != null){
                    for(friend in friendData){
                        if(friend.uf_favorite_state.equals("true")){
                            friendlist.apply{
                                add(FriendRecycleViewData(friend.uf_friend_name.toString(),friend.uf_friend_id.toString(),friend.uf_favorite_state.toString()))
                                friendlist.sortWith(compareBy({it.name}))
                                friendAdapter.notifyDataSetChanged()
                            }
                            //Log.d("friendList LOG", "setFriendList ??????")
                            //myIdViewModel.setFriendList(friendlist)
                        }

                    }
                }


            }


            override fun onFailure(call: Call<List<ShowFriendOutput>>, t: Throwable) {
                Log.d("friendTest","????????????")
            }

        })
    }



}