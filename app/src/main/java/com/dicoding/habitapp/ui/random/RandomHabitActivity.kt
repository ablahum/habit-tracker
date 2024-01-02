package com.dicoding.habitapp.ui.random

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.habitapp.R
import com.dicoding.habitapp.ui.ViewModelFactory
import com.dicoding.habitapp.utils.HABIT
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.dicoding.habitapp.ui.countdown.CountDownActivity

class RandomHabitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_habit)

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        val adapter = RandomHabitAdapter(this) { habit ->
            val intent = Intent(this, CountDownActivity::class.java)
            intent.putExtra(HABIT, habit)
            startActivity(intent)
        }
        viewPager.adapter = adapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        val tabTitles = listOf("High", "Medium", "Low")

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = "Habit ${position + 1}: ${tabTitles[position]}"
        }.attach()

        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory).get(RandomHabitViewModel::class.java)

        viewModel.priorityLevelLow.observe(this) {
            if (it != null) {
                adapter.submit(RandomHabitAdapter.PageType.LOW, it)
            }
        }

        viewModel.priorityLevelMedium.observe(this) {
            if (it != null) {
                adapter.submit(RandomHabitAdapter.PageType.MEDIUM, it)
            }
        }

        viewModel.priorityLevelHigh.observe(this) {
            if (it != null) {
                adapter.submit(RandomHabitAdapter.PageType.HIGH, it)
            }
        }
    }
}



