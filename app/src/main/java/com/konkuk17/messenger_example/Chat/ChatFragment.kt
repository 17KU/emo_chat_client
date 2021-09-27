package com.konkuk17.messenger_example.Chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.konkuk17.messenger_example.Main.IdViewModel
import com.konkuk17.messenger_example.R


class ChatFragment : Fragment() {

    val myIdViewModel: IdViewModel by activityViewModels<IdViewModel>()

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var items : ArrayList<Chatting> = arrayListOf()
        val view = inflater.inflate(R.layout.fragment_chat_list, container, false)

        items.add(Chatting(0,"홍예주","yeju"))
        items.add(Chatting(0,"김소현","sohyun"))
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyChatRecyclerViewAdapter(items)
            }
        }
        return view
    }

}