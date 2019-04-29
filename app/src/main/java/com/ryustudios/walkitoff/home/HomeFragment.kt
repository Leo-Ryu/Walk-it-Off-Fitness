package com.ryustudios.walkitoff.home

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import kotlinx.android.synthetic.main.fragment_home.view.*
import android.os.SystemClock
import androidx.annotation.RequiresApi
import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
import android.app.NotificationChannel
import android.graphics.Color
import android.widget.Toast
import com.ryustudios.walkitoff.AlarmReceiver
import com.ryustudios.walkitoff.R


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HomeFragment : Fragment() {

    private val NOTIFICATION_ID = 0
    private val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    private var mNotificationManager: NotificationManager? = null

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment HomeFragment.
         */
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        if (alarmMgr != null) {
            alarmMgr?.cancel(alarmIntent)
        }

        mNotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val notifyIntent = Intent(context, AlarmReceiver::class.java)
        alarmIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        createNotificationChannel()

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        view.startButton.text = "Start"
        view.startButton.textOn = "Stop"
        view.startButton.textOff = "Start"
        view.startButton.setOnCheckedChangeListener{view, isChecked ->
            Log.d("startButton", "Clicked!")

            if (isChecked) {
                Toast.makeText(context, "Alarm Set!", Toast.LENGTH_SHORT).show()
                alarmMgr?.setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 1000,
                    5000,
                    alarmIntent)
            } else {
                alarmMgr?.cancel(alarmIntent)
                Toast.makeText(context, "Alarm Cancelled!", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    /**
     * Creates a Notification channel, for OREO and higher.
     */
    private fun createNotificationChannel() {
        // Create a notification manager object.
        mNotificationManager = context?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Stand up notification",
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Notifies every 15 minutes to stand up and walk"
            mNotificationManager?.createNotificationChannel(notificationChannel)
        }
    }
}
