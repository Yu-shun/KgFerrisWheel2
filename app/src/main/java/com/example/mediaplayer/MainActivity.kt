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
    var timeValue = 60 * 18
    val ms = "%02d:%02d"

    var half_key = 0

    var radioCheck = false
    var halfCheck = false

    lateinit var radioMusic: MediaPlayer
    lateinit var halfMusic: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        radioMusic = MediaPlayer.create(this, R.raw.two_93hz)
        halfMusic = MediaPlayer.create(this, R.raw.two_announcement)

        limit_view.rotation = 30f

        val typeface = Typeface.createFromAsset(assets, "DSEG7.ttf")
        standardText.typeface = typeface
        foamText(num)

        val upButton = findViewById<ImageButton>(R.id.up_Button)
        upButton.setOnClickListener(this)

        val downButton = findViewById<ImageButton>(R.id.down_Button)
        downButton.setOnClickListener(this)

        val mediaButton = findViewById<View>(R.id.recView)
        mediaButton.setOnClickListener(this)

        val keyText = findViewById<TextView>(R.id.tapText)
        keyText.setOnClickListener(this)

//        timeToText(timeValue)?.let {
//            timeText.text = it
//            timeText.typeface = typeface
//        }

        val rotationView = findViewById<View>(R.id.rotation_view)

        ferrisWheel(rotationView)

        val runnable = object : Runnable {
            override fun run() {
                timeValue--
                //.?⇒nullでなければ
//                timeToText(timeValue)?.let {
//                    timeText.text = it
//                    timeText.typeface = typeface
//                }
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

            //System.out.println("*********"+updatelayoutParams.circleAngle)

            if (timeValue == 60 * 9) {
                if (radioCheck){
                    radioMusic.stop()
                    radioMusic = MediaPlayer.create(this, R.raw.two_93hz)
                    halfMusic.start()
                    halfCheck = true
                }else if (!radioCheck){
                    halfMusic.start()
                }
            }

            if (updatelayoutParams.circleAngle > 361f && updatelayoutParams.circleAngle < 480f) {
                if(!halfMusic.isPlaying && halfCheck && half_key == 0){
                    radioMusic.start()
                    radioMusic.isLooping = true
                    half_key++
                }
            }


            if (updatelayoutParams.circleAngle > 480f && updatelayoutParams.circleAngle <= 539f){
                //view.visibility = View.VISIBLE
                if(timeValue == 3 * 60){
                    radioMusic.stop()
                    finishText.alpha = 1.0f
                    view.alpha = 1.0f
                    val view_animation = AnimationUtils.loadAnimation(this, R.anim.view_fadein)
                    view.startAnimation(view_animation)
                    val text_animation = AnimationUtils.loadAnimation(this, R.anim.finishtext_fadein)
                    finishText.startAnimation(text_animation)
                }
//                else if(timeValue == 2){
//                    val text_animation = AnimationUtils.loadAnimation(this, R.anim.finishtext_fadeout)
//                    finishText.startAnimation(text_animation)
//                    val view_animation = AnimationUtils.loadAnimation(this, R.anim.view_fadeout)
//                    view.startAnimation(view_animation)
//                }

                if(updatelayoutParams.circleAngle > 490f){
                    num = 50
                    foamText(num)
                    radioCheck = false
                    halfCheck = false
                    //originalText()
                }
            }

            if (updatelayoutParams.circleAngle == endAngle){
                val text_animation = AnimationUtils.loadAnimation(this, R.anim.finishtext_fadeout)
                finishText.startAnimation(text_animation)
                val view_animation = AnimationUtils.loadAnimation(this, R.anim.view_fadeout)
                view.startAnimation(view_animation)

                half_key = 0

                layoutParams.circleAngle = layoutParams.circleAngle - 360
                timeValue = 60 * 18
                radioMusic = MediaPlayer.create(this, R.raw.two_93hz)
                ferrisWheel(rotationView)
            }
        }
        //1回転の時間(60000:60秒)
        rotationAnim.duration = 60000 * 18
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
            R.id.recView -> {
                if (num == 93){
                    if (!radioMusic.isPlaying){
                        radioMusic.start()
                        radioMusic.isLooping = true
                    }
                    radioCheck = true
                }else{
                    if (radioMusic.isPlaying){
                        radioMusic.stop()//音楽を停止
                        radioMusic = MediaPlayer.create(this, R.raw.two_93hz)
                        radioCheck = false
                    }
                }
            }
//            R.id.tapText -> {
//                key++
//                if (key == 5){
//                    val animation = AnimationUtils.loadAnimation(this, R.anim.alpha_fadein)
//                    nazoText3.startAnimation(animation)
//                    tapText.startAnimation(animation)
//                    nazoText3_1.startAnimation(animation)
//                    nazoText4.startAnimation(animation)
//                    nazoText5.startAnimation(animation)
//
//                    nazoText3.text = ""
//                    tapText.text = ""
//                    nazoText3_1.text = ""
//                    nazoText4.text = "D1に規模3の敵がいます"
//                    nazoText5.text = ""
//                }
//            }
        }
        foamText(num)
    }

    fun foamText(num: Int){
        val typeface = Typeface.createFromAsset(assets, "DSEG7.ttf")
        textView.text = "%02d".format(num)
        textView.typeface = typeface
    }

//    fun originalText(){
//        key = 0
//        nazoText3.text = "システム上の"
//        tapText.text = "理由"
//        nazoText3_1.text = "で、"
//        nazoText4.text = "タブレットの画面以外は"
//        nazoText5.text = "さわらないでください"
//    }

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
