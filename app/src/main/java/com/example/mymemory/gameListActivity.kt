package com.example.mymemory

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymemory.models.BoardSize
import com.example.mymemory.models.UserImageList
import com.example.mymemory.utils.ButtonAdapter
import com.example.mymemory.utils.EXTRA_BOARD_SIZE
import com.example.mymemory.utils.EXTRA_GAME_NAME
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class gameListActivity: AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var recycler: RecyclerView
    private var buttonItems: List<ButtonItem>? = null
    private val db = FirebaseFirestore.getInstance()
    private val docRef = db.collection("games")
    private var gameNames: List<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.games_list)

        docRef.get()
            .addOnSuccessListener { querySnapshot ->
                val games = mutableListOf<String>()
                for (gameName in querySnapshot.documents) {
                    games.add(gameName.id)
                }
                gameNames = games
                Log.d("get: ", "onCreate: $gameNames")
                //Log.d("gameList", "onCreate: $gameNames")

                if (!gameNames.isNullOrEmpty()) {
                    val buttons = mutableListOf<ButtonItem>()
                    textView = findViewById(R.id.gamesTextView)
                    for (name in gameNames!!) {
                        val buttonItem = ButtonItem(name)
                        buttons.add(buttonItem)
                    }
                    buttonItems = buttons
                    recycler = findViewById(R.id.RecyclerList)

                    val adapter = ButtonAdapter(buttonItems as MutableList<ButtonItem>) { item ->
                        val gameName = item.text
                        val intent = Intent(this, GameActivity::class.java)
                        db.collection("games").document(gameName).get().addOnSuccessListener{ document ->
                            val userImageList = document.toObject(UserImageList::class.java)
                            val desiredBoardSize: BoardSize = when(userImageList?.images?.size){
                                        4 -> BoardSize.EASY
                                        9 -> BoardSize.MEDIUM
                                        12 -> BoardSize.HARD
                                else -> {BoardSize.EASY}
                            }
                            intent.putExtra(EXTRA_BOARD_SIZE, desiredBoardSize)
                            intent.putExtra(EXTRA_GAME_NAME, gameName)
                            startActivity(intent)
                        }
                    }
                    recycler.adapter = adapter
                    recycler.layoutManager = LinearLayoutManager(this)
                } else {
                    AlertDialog.Builder(this)
                        .setTitle("failed loading games or no games added :(")
                        .setPositiveButton("OK") { _, _ ->
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }.show()
                }
            }
            .addOnFailureListener {
                Log.d("gameList", "onCreate: Failed getting games from Firebase")
            }

    }
}