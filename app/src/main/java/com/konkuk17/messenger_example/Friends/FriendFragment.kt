package com.konkuk17.messenger_example.Friends

import android.content.Context
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

    val myIdViewModel: IdViewModel by activityViewModels<IdViewModel>()

    lateinit var binding: FragmentFriendBinding

    lateinit var mContext: Context

    lateinit var friendService: FriendService

    lateinit var friendAdapter: FriendRecycleViewAdapter

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
        Log.d("initTest","init이 됐는지")

        //retrofit 연결
        var retrofit = Retrofit.Builder()
            .baseUrl("http://203.252.166.72:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //friendService API 연결
        friendService = retrofit.create(FriendService::class.java)

        binding.apply{

            var user_id = myIdViewModel.myId.value.toString()

            //recycler view에서 쓸 어뎁터
            friendAdapter = FriendRecycleViewAdapter(this@FriendFragment.requireContext(), friendlist){ friendRecycleViewData ->

                var favorite_add = friendRecycleViewData.id

                Toast.makeText(this@FriendFragment.requireContext(),favorite_add,Toast.LENGTH_LONG).show()

                //친구 즐겨찾기 추가
                //어댑터에 익명 리스너 붙이기 위해 여기에 코드 작성
                friendService.AddFavorite(user_id,favorite_add).enqueue(object :
                    Callback<AddFriendOutput>{
                    override fun onResponse(
                        call: Call<AddFriendOutput>,
                        response: Response<AddFriendOutput>
                    ) {
                        var favorite = response.body()

                        if(favorite?.code.equals("0002")){

                        }

                    }

                    override fun onFailure(call: Call<AddFriendOutput>, t: Throwable) {

                    }

                }
                )
            }

            friendRecycleView.adapter = friendAdapter

            val linearLayoutManager = LinearLayoutManager(this@FriendFragment.requireContext())
            friendRecycleView.layoutManager = linearLayoutManager
            friendRecycleView.setHasFixedSize(true)

            //친구추가버튼
            addFriendBtn.setOnClickListener{


                myIdViewModel.setFriendList(friendlist)
                val intent = Intent(this@FriendFragment.requireContext(), AddFriendActivity::class.java)
                intent.putExtra("myUid",myIdViewModel.myId.value)
                startActivityForResult(intent,100)
            }


            //친구 검색
            findFriendBtn.setOnClickListener{

                myIdViewModel.setFriendList(friendlist)
                val intent2 = Intent(this@FriendFragment.requireContext(), SearchFriendActivity::class.java)
                intent2.putExtra("myUid",myIdViewModel.myId.value)
                //intent2.putExtra("fList",myIdViewModel.friendList.value)
                intent2.putExtra("fList", friendlist)
                startActivityForResult(intent2,101)
            }


            //친구 목록 갱신
            FriendListUpdate(friendService,friendAdapter)
        }
    }


    // Activity Result 가 있는 경우 실행되는 콜백함수
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            100->{
                FriendListUpdate(friendService,friendAdapter)
            }
            101->{
                FriendListUpdate(friendService,friendAdapter)
            }

        }

    }



    //친구 목록 갱신
    fun FriendListUpdate(friendService: FriendService, friendAdapter:FriendRecycleViewAdapter){
        //IdViewModel에서 사용자 id값 가져오기
        var user_id = myIdViewModel.myId.value.toString()

        //API 호출
        friendService.ShowFriend(user_id).enqueue(object :
            Callback<List<ShowFriendOutput>> {
            override fun onResponse(
                call: Call<List<ShowFriendOutput>>,
                response: Response<List<ShowFriendOutput>>
            ) {

                //리스트 비우기
                friendlist.clear()

                val friendData : List<ShowFriendOutput>? = response.body()

                //목록 갱신
                if(friendData != null){
                    for(friend in friendData){
                        friendlist.apply{
                            add(FriendRecycleViewData(friend.uf_friend_name.toString(),friend.uf_friend_id.toString(),friend.uf_favorite_state.toString()))
                            friendAdapter.notifyDataSetChanged()
                        }
                        Log.d("friendList LOG", "setFriendList 완료")
                        myIdViewModel.setFriendList(friendlist)
                    }
                }

            }


            override fun onFailure(call: Call<List<ShowFriendOutput>>, t: Throwable) {
                Log.d("friendTest","실패했음")
            }

        })
    }


}