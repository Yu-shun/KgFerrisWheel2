package com.example.mediaplayer

import android.animation.ValueAnimator
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    var num = 50

    val handler = Handler()
    var timeValue = 60
    val ms = "%02d:%02d"

    var key = 0

    lateinit var music: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        music = MediaPlayer.create(this, R.raw.free_bgm)

        limit_view.rotation = 30f

        val typeface = Typeface.createFromAsset(assets, "DSEG7.ttf")
        standardText.typeface = typeface
        foamText(num)

        val upButton = findViewById<ImageButton>(R.id.up_Button)
        upButton.setOnClickListener(this)

        val downButton = findViewById<ImageButton>(R.id.down_Button)
        downButton.setOnClickListener(this)

        val mediaButton = findViewById<ImageButton>(R.id.recButton)
        mediaButton.setOnClickListener(this)

        val keyText = findViewById<TextView>(R.id.tapText)
        keyText.setOnClickListener(this)

        timeToText(timeValue)?.let {
            timeText.text = it
            timeText.typeface = typeface
        }

        val rotationView = findViewById<View>(R.id.rotation_view)

        ferrisWheel(rotationView)

        val runnable = object : Runnable {
            override fun run() {
                timeValue--
                //.?⇒nullでなければ
                timeToText(timeValue)?.let {
                    timeText.text = it
                    timeText.typeface = typeface
                }
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
//        handler.removeCallbacks(runnable)
    }


    fun ferrisWheel(rotationView: View?){
        val layoutParams = rotationView?.layoutParams as ConstraintLayout.LayoutParams
        val startAngle = layoutParams.circleAngle
        val endAngle = startAngle + 360

//        System.out.println("startAngle****"+startAngle+"****")
//        System.out.println("endAngle****"+endAngle+"****")

        //ValueAnimatorのインスタンスを作成
        val rotationAnim = ValueAnimator.ofFloat(startAngle, endAngle)
        rotationAnim.addUpdateListener { valueAnimator ->
            //インスタンスが更新されるごとにrotationViewの角度を取得し，割り当て
            val animatedValue = valueAnimator.animatedValue as Float
            val updatelayoutParams = rotationView.layoutParams as ConstraintLayout.LayoutParams
            updatelayoutParams.circleAngle = animatedValue
            rotationView.layoutParams = updatelayoutParams

            //vavarious_crescentsも回転させることで，自然に違和感なく回転しているようにみえる
            //(丸などの上下左右対称である場合は不要)
            //various_crescents.rotation = (animatedValue % 360 - 270)

            System.out.println("*********"+updatelayoutParams.circleAngle)

            if (updatelayoutParams.circleAngle >= 480f && updatelayoutParams.circleAngle <= 539f){
                //view.visibility = View.VISIBLE
                if(timeValue == 10){
                    finishText.alpha = 1.0f
                    view.alpha = 1.0f
                    val view_animation = AnimationUtils.loadAnimation(this, R.anim.view_fadein)
                    view.startAnimation(view_animation)
                    val text_animation = AnimationUtils.loadAnimation(this, R.anim.finishtext_fadein)
                    finishText.startAnimation(text_animation)
                }else if(timeValue == 2){
                    val text_animation = AnimationUtils.loadAnimation(this, R.anim.finishtext_fadeout)
                    finishText.startAnimation(text_animation)
                    val view_animation = AnimationUtils.loadAnimation(this, R.anim.view_fadeout)
                    view.startAnimation(view_animation)
                }
                if(updatelayoutParams.circleAngle >= 500f){
                    num = 50
                    foamText(num)
                    music.release()

                    originalText()

                    if (up_Button.isEnabled == false){
                        up_Button.isEnabled = true
                        down_Button.isEnabled = true
                        recButton.isEnabled = true
                    }
                }
            }

            if (updatelayoutParams.circleAngle == endAngle){
                layoutParams.circleAngle = layoutParams.circleAngle - 360
                timeValue = 60
                ferrisWheel(rotationView)
            }
        }
        //1回転の時間(60000:60秒)
        rotationAnim.duration = 60000
        //LinearInterpolatorをセットすることでアニメーションの減速をなくす
        rotationAnim.interpolator = LinearInterpolator()
        rotationAnim.start()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.up_Button -> {
                if (num<99){
                    num += 1
                }
            }
            R.id.down_Button -> {
                if (num>0){
                    num -= 1
                }
            }
            R.id.recButton -> {
                if (num == 40){
                    music = MediaPlayer.create(this, R.raw.free_bgm)
                    music.isLooping = true
                    music.start()
                    up_Button.isEnabled = false
                    down_Button.isEnabled = false
                    recButton.isEnabled = false
                }else{
                    music.release()
                }
            }
            R.id.tapText -> {
                key++
                if (key == 5){
                    val animation = AnimationUtils.loadAnimation(this, R.anim.alpha_fadein)
                    how_to_use.startAnimation(animation)
                    nazoText1.startAnimation(animation)
                    nazoText2.startAnimation(animation)
                    nazoText3.startAnimation(animation)
                    tapText.startAnimation(animation)
                    nazoText3_1.startAnimation(animation)
                    nazoText4.startAnimation(animation)
                    nazoText5.startAnimation(animation)
                    nazoText6.startAnimation(animation)

                    how_to_use.text = "なぞ解明！"
                    nazoText1.text = "1.いいいいいいいいいいいいい"
                    nazoText2.text = "2.いいいいいいいいいいいいい"
                    nazoText3.text = "3.いいいいいいい"
                    tapText.text = "い"
                    nazoText3_1.text = "いいいいい"
                    nazoText4.text = "4.いいいいいいいいいいいいい"
                    nazoText5.text = "5.いいいいいいいいいいいいい"
                    nazoText6.text = "6.いいいいいいいいいいいいい"
                }
            }
        }
        foamText(num)
    }

//    override fun onDestroy() {
//        music.release()
//        super.onDestroy()
//    }

    fun foamText(num: Int){
        val typeface = Typeface.createFromAsset(assets, "DSEG7.ttf")
        textView.text = "%02d".format(num)
        textView.typeface = typeface
    }

    fun originalText(){
        key = 0
        how_to_use.text = "観覧車での注意事項"
        nazoText1.text = "1.あああああああああああああ"
        nazoText2.text = "2.あああああああああああああ"
        nazoText3.text = "3.あああああああ"
        tapText.text = "あ"
        nazoText3_1.text = "あああああ"
        nazoText4.text = "4.あああああああああああああ"
        nazoText5.text = "5.あああああああああああああ"
        nazoText6.text = "6.あああああああああああああ"
    }

    fun timeToText(time: Int = 0): String? {
        if (time <= 0) {
            return ms.format(0,0)
        } else {
            val m = time % 3600 / 60
            val s = time % 60
            return ms.format(m, s)
        }
    }

    override fun onStart() {
        super.onStart()
        hideSystemUI()
    }

    fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }
}
