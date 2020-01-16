package com.example.mediaplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var bgm:MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val checkNum = findViewById<Button>(R.id.checkButton)
        var decide_number = 0

        num1.minValue = 0
        num1.maxValue = 9
        num1.value = 0

        num2.minValue = 0
        num2.maxValue = 9
        num2.value = 0

        num3.minValue = 0
        num3.maxValue = 9
        num3.value = 0

        bgm = MediaPlayer.create(this,R.raw.free_bgm)

        checkNum.setOnClickListener {
            decide_number = num1.value*100 + num2.value*10 + num3.value

            if (decide_number == 117){
                bgm.isLooping = true
                bgm.start()
                numCheck.setText("正解！")
            }
        }
    }

    override fun onDestroy() {
        bgm.release()
        super.onDestroy()
    }
}
