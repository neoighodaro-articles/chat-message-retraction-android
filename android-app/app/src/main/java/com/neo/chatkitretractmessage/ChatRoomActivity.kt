package com.neo.chatkitretractmessage

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.neo.chatkitretractmessage.ChatkitApp.currentUser
import com.pusher.chatkit.messages.multipart.NewPart
import com.pusher.chatkit.rooms.RoomListeners
import kotlinx.android.synthetic.main.activity_chat_room.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatRoomActivity : AppCompatActivity(), ChatRoomAdapter.ChatRoomListener {

    private lateinit var textViewMessage: EditText
    private val chatRoomAdapter = ChatRoomAdapter(this)
    private val api = ApiClient.client.create(ApiInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        textViewMessage = findViewById(R.id.editTextMessage)
        setupRecyclerView()
        subscribeToRoom()
        setupButtonListener()
    }


    override fun onDeleteMessage(messageId: Int) {
        api!!.retractMessage(RetractedMessage(messageId)).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("TAG", t.message)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful){
                    Log.e("TAG", response.message())
                }
            }

        })
    }


    private fun setupRecyclerView() {
        with(recyclerViewMessages){
            layoutManager = LinearLayoutManager(this@ChatRoomActivity)
            adapter = chatRoomAdapter
            addItemDecoration(DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL))
        }
    }


    private fun subscribeToRoom() {
        currentUser.subscribeToRoomMultipart(
            roomId = currentUser.rooms[0].id,
            listeners = RoomListeners(
                onMultipartMessage = {
                    runOnUiThread {
                        chatRoomAdapter.addMessage(it)
                    }
                }
            ),
            messageLimit = 20, // Optional
            callback = { subscription ->
                // Called when the subscription has started.
                // You should terminate the subscription with subscription.unsubscribe()
                // when it is no longer needed
            }
        )

    }


    private fun setupButtonListener() {
        sendButton.setOnClickListener {
            if (textViewMessage.text.isNotEmpty()) {
                currentUser.sendMultipartMessage(
                    roomId = currentUser.rooms[0].id,
                    parts = listOf(
                        NewPart.Inline(textViewMessage.text.toString(), "text/plain")),
                    callback = { result -> // Result<Int, Error>
                        // The Int is the new message ID
                    }
                )

                textViewMessage.setText("")
                hideKeyboard()
            }
        }
    }


    private fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus

        if (view == null) {
            view = View(this)
        }

        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
