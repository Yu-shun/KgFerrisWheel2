package com.example.mediaplayer

import android.animation.ValueAnimator
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var num = 50

    val handler = Handler()
    var timeValue = 60 * 18
    val ms = "%02d:%02d"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val typeface = Typeface.createFromAsset(assets, "DSEG7.ttf")
        textView.text = num.toString()
        textView.typeface = typeface
        standardText.typeface = typeface

        val upButton = findViewById<ImageButton>(R.id.up_Button)
        upButton.setOnClickListener(this)

        val downButton = findViewById<ImageButton>(R.id.down_Button)
        downButton.setOnClickListener(this)

        timeToText(timeValue)?.let {
            timeText.text = it
            timeText.typeface = typeface
        }

        reset_button.isEnabled = false

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

        val rotationView = findViewById<View>(R.id.rotation_view)
        val circleButton = findViewById<Button>(R.id.circle_Button)
        circleButton.setOnClickListener{
            //startAngleに現在の角度，endAngleにどこで終わるかを指定
            val layoutParams = rotationView.layoutParams as ConstraintLayout.LayoutParams
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

                if (updatelayoutParams.circleAngle == endAngle){
                    handler.removeCallbacks(runnable)
                    reset_button.isEnabled = true
                }
            }
            //1回転の時間(60000:60秒)
            rotationAnim.duration = 60000 * 18
            //LinearInterpolatorをセットすることでアニメーションの減速をなくす
            rotationAnim.interpolator = LinearInterpolator()
            rotationAnim.start()

            handler.post(runnable)

            circleButton.isEnabled = false
        }

        reset_button.setOnClickListener {
            handler.removeCallbacks(runnable)
            timeValue = 60 * 18
            timeToText(timeValue)?.let {
                timeText.text = it
            }

            circleButton.isEnabled = true
            reset_button.isEnabled = false
        }
    }

    override fun onClick(v: View?) {
        val typeface = Typeface.createFromAsset(assets, "DSEG7.ttf")
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
        }
        textView.text = "%02d".format(num)
        textView.typeface = typeface
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
}
