package com.dicoding.habitapp.ui.countdown

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit
import com.dicoding.habitapp.databinding.ActivityAddHabitBinding
import com.dicoding.habitapp.databinding.ActivityCountDownBinding
import com.dicoding.habitapp.notification.NotificationWorker
import com.dicoding.habitapp.utils.HABIT

class CountDownActivity : AppCompatActivity() {
    private lateinit var countDownTimer: CountDownTimer
    private var isCountDownRunning = false
    private lateinit var binding: ActivityCountDownBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountDownBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Count Down"

        val habit = intent.getParcelableExtra<Habit>(HABIT)

        if (habit != null) {
            binding.tvCountDownTitle.text = habit.title

            ViewModelProvider(this)[CountDownViewModel::class.java]

            // TODO 13: Start and cancel One Time Request WorkManager to notify when time is up.

            binding.btnStart.setOnClickListener {
                val initialTime = habit.minutesFocus * 60 * 1000
                startCount(initialTime, habit.id, habit.title)
            }

            binding.btnStop.setOnClickListener {
                stopCount()
            }
        }
    }

    private fun showNotification(habitId: Int, habitTitle: String) {
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(NotificationWorker.createInputData(habitId, habitTitle))
            .build()
        WorkManager.getInstance(this).enqueue(workRequest)
    }

    private fun updateCount(timeInMillis: Long) {
        val minutes = (timeInMillis / 1000) / 60
        val seconds = (timeInMillis / 1000) % 60
        binding.tvCountDown.text =
            String.format("%02d:%02d", minutes, seconds)
    }

    private fun updateBtn(isRunning: Boolean) {
        binding.btnStop.isEnabled = isRunning
        binding.btnStart.isEnabled = !isRunning
    }

    private fun startCount(initialTime: Long, habitId: Int, habitTitle: String) {
        countDownTimer = object : CountDownTimer(initialTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateCount(millisUntilFinished)
            }

            override fun onFinish() {
                updateCount(0)
                updateBtn(false)
                showNotification(habitId, habitTitle)
            }
        }

        countDownTimer.start()
        isCountDownRunning = true
        updateBtn(true)
    }

    private fun stopCount() {
        countDownTimer.cancel()
        isCountDownRunning = false
        updateBtn(false)
    }
}
