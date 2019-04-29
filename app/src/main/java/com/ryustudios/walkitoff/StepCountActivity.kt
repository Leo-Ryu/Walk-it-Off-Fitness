package com.ryustudios.walkitoff

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import at.grabner.circleprogress.CircleProgressView
import at.grabner.circleprogress.Direction
import at.grabner.circleprogress.TextMode
import com.ryustudios.walkitoff.home.MainActivity
import kotlinx.android.synthetic.main.activity_step_count.*


class StepCountActivity : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var running = false

    private val stepGoal = 5
    private var stepCount = 0

    private var mCircleView: CircleProgressView? = null
    private var mShowUnit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_count)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        running = true


        mCircleView = circleView
        mCircleView?.setOnProgressChangedListener(CircleProgressView.OnProgressChangedListener { value ->
            Log.i("Progress Changed: ", value.toString())
        })

        mCircleView?.maxValue = stepGoal.toFloat()
        mCircleView?.setValue(stepGoal.toFloat())

        //growing/rotating clockwise
        mCircleView?.setDirection(Direction.CW)

        //show unit
        mCircleView?.unit = ""
        mCircleView?.isUnitVisible = mShowUnit

        //text sizes
        mCircleView?.setTextSize(50); // text size set, auto text size off
        mCircleView?.setAutoTextSize(true); // enable auto text size, previous values are overwritten
        //if you want the calculated text sizes to be bigger/smaller you can do so via
        mCircleView?.setTextScale(0.9f);

        //custom typeface
        val font = Typeface.createFromAsset(assets, "fonts/DROID_SERIF_REGULAR.ttf")
        mCircleView?.setTextTypeface(font)
        //mCircleView?.setUnitTextTypeface(font)


        //color
        //you can use a gradient
        mCircleView?.setBarColor(resources.getColor(R.color.colorPrimary), resources.getColor(R.color.colorAccent))

        //colors of text and unit can be set via
        mCircleView?.setTextColor(Color.RED)
        mCircleView?.setTextColor(Color.BLUE)
        //or to use the same color as in the gradient
        mCircleView?.setTextColorAuto(true) //previous set values are ignored


        //in the following text modes, the text is ignored
        mCircleView?.setTextMode(TextMode.VALUE) // Shows the current value

    }

    override fun onResume() {
        super.onResume()
        running = true

        val stepsSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepsSensor == null) {
            Toast.makeText(this, "No Step Counter Sensor !", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepsSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        running = false
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(p0: SensorEvent) {
        if (running) {
            mCircleView?.setValue((stepGoal - stepCount).toFloat())
            stepCount += 1
        }
        Log.i("StepCount", stepCount.toString())

        if (stepCount == stepGoal) {
            stepCount = 0

            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onBackPressed() {

    }




}
