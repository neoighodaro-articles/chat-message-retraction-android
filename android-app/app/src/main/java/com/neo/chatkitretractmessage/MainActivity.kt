package com.neo.chatkitretractmessage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.pusher.chatkit.AndroidChatkitDependencies
import com.pusher.chatkit.ChatManager
import com.pusher.chatkit.ChatkitTokenProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginButton.setOnClickListener { setupChatManager() }
    }

    private fun setupChatManager() {
        val chatManager = ChatManager(
            instanceLocator = "CHATKIT_INSTANCE_LOCATOR",
            userId = username.text.toString(),
            dependencies = AndroidChatkitDependencies(
                tokenProvider = ChatkitTokenProvider(
                    endpoint = "${ChatkitApp.BASE_URL}/token",
                    userId = username.text.toString()
                )
            )
        )


        chatManager.connect { result ->
            when (result) {
                is com.pusher.util.Result.Success -> {
                    ChatkitApp.currentUser = result.value
                    startActivity(Intent(this@MainActivity,ChatRoomActivity::class.java))
                    finish()
                }

                is com.pusher.util.Result.Failure -> {
                    Log.e("Idee",result.error.localizedMessage)
                    Log.e("Idee",result.error.stackTrace.toString())
                    Log.e("Idee",result.error.reason)
                }
            }
        }

    }

}
