package com.dicoding.habitapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit
import com.dicoding.habitapp.databinding.ActivityCountDownBinding
import com.dicoding.habitapp.databinding.ActivityDetailHabitBinding
import com.dicoding.habitapp.ui.ViewModelFactory
import com.dicoding.habitapp.ui.countdown.CountDownActivity
import com.dicoding.habitapp.utils.HABIT
import com.dicoding.habitapp.utils.HABIT_ID
import com.google.android.material.appbar.CollapsingToolbarLayout

class DetailHabitActivity : AppCompatActivity() {
    private lateinit var selectedHabit: Habit
    private lateinit var viewModel: DetailHabitViewModel
    private lateinit var binding: ActivityDetailHabitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val habitId = intent.getIntExtra(HABIT_ID, 0)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(DetailHabitViewModel::class.java)

        viewModel.start(habitId)

        viewModel.habit.observe(this) { habit ->
            if (habit != null) {
                selectedHabit = habit
                binding.toolbarLayout.title = habit.title
                findViewById<EditText>(R.id.detail_ed_time_minutes).setText(habit.minutesFocus.toString())
                findViewById<EditText>(R.id.detail_ed_start_time).setText(habit.startTime)
                when (habit.priorityLevel) {
                    resources.getStringArray(R.array.priority_level)[0] -> {
                        binding.detailPriorityLevel.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.red
                            )
                        )
                    }

                    resources.getStringArray(R.array.priority_level)[1] -> {
                        binding.detailPriorityLevel.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.yellow
                            )
                        )
                    }

                    else -> {
                        binding.detailPriorityLevel.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.green
                            )
                        )
                    }
                }
            }
        }

        findViewById<Button>(R.id.btn_open_count_down).setOnClickListener {
            val intent = Intent(this, CountDownActivity::class.java)
            intent.putExtra(HABIT, selectedHabit)
            startActivity(intent)
        }
    }
}