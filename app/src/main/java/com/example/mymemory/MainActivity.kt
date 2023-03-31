package com.example.mymemory

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mymemory.models.BoardSize
import com.example.mymemory.utils.EXTRA_BOARD_SIZE
import com.example.mymemory.utils.EXTRA_GAME_NAME
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity: AppCompatActivity(){
    companion object {
        private const val TAG = "MainActivity"
        private const val CREATE_REQUEST_CODE = 248
    }

    private lateinit var GameName: TextView
    private lateinit var playButton: Button
    private lateinit var diffButton: Button
    private lateinit var customBtn: Button
    private lateinit var playCustBtn: Button
    private val db = Firebase.firestore
    private var gameName: String? = null
    private var boardSize: BoardSize = BoardSize.EASY


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)
        
        GameName = findViewById(R.id.Welcome)
        playButton = findViewById(R.id.PlayBtn)
        diffButton = findViewById(R.id.ChooseBtn)
        customBtn = findViewById(R.id.CustomBtn)
        playCustBtn = findViewById(R.id.playCustomBtn)
        
        playButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        diffButton.setOnClickListener {
            showNewSizeDialog()
        }

        customBtn.setOnClickListener {
            showCreationDialog()
        }

        playCustBtn.setOnClickListener {
            val intent = Intent(this, gameListActivity::class.java)
            startActivity(intent)
        }
        /*
        val intent = Intent(this, CreateActivity::class.java)
        intent.putExtra(EXTRA_BOARD_SIZE,BoardSize.MEDIUM)
        startActivity(intent)
        */
    }
    private fun showNewSizeDialog(){
        val boardSizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size, null)
        val radioGroupSize = boardSizeView.findViewById<RadioGroup>(R.id.radioGroup)
        when(boardSize){
            BoardSize.EASY -> radioGroupSize.check(R.id.rbEasy)
            BoardSize.MEDIUM -> radioGroupSize.check(R.id.rbMedium)
            BoardSize.HARD -> radioGroupSize.check(R.id.rbHard)
        }
        showAlertDialog("Choose new size", boardSizeView, View.OnClickListener {
            val desiredBoardSize = when (radioGroupSize.checkedRadioButtonId){
                R.id.rbEasy -> BoardSize.EASY
                R.id.rbMedium -> BoardSize.MEDIUM
                else -> BoardSize.HARD
            }
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra(EXTRA_BOARD_SIZE,desiredBoardSize)
            startActivity(intent)
        })

    }
    private fun showAlertDialog(title: String, view: View?, positiveClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK"){_, _->
                positiveClickListener.onClick(null)
            }.show()
    }
    private fun showCreationDialog() {
        val boardSizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size, null)
        val radioGroupSize = boardSizeView.findViewById<RadioGroup>(R.id.radioGroup)
        showAlertDialog("Create your own memory board", boardSizeView, View.OnClickListener {
            val desiredBoardSize = when (radioGroupSize.checkedRadioButtonId){
                R.id.rbEasy -> BoardSize.EASY
                R.id.rbMedium -> BoardSize.MEDIUM
                else -> BoardSize.HARD
            }
            //navigate to a new activity

            var intent = Intent(this,CreateActivity::class.java)
            intent.putExtra(EXTRA_BOARD_SIZE,desiredBoardSize)
            startActivityForResult(intent, CREATE_REQUEST_CODE)
        })
    }
}