package com.dicoding.habitapp.ui.random

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit

class RandomHabitAdapter(
    private val context: Context,
    private val onClick: (Habit) -> Unit
) : RecyclerView.Adapter<RandomHabitAdapter.PagerViewHolder>() {
    private val habitList = mutableListOf<Pair<PageType, Habit>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder =
        PagerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.pager_item, parent, false)
        )

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val (key, pageData) = habitList[position]
        holder.bind(key, pageData)
    }

    override fun getItemCount(): Int = habitList.size

    enum class PageType {
        HIGH, MEDIUM, LOW
    }

    inner class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.pager_tv_title)
        private val tvStartTime: TextView = itemView.findViewById(R.id.pager_tv_start_time)
        private val ivPriorityLevel: ImageView = itemView.findViewById(R.id.item_priority_level)
        private val tvMinutes: TextView = itemView.findViewById(R.id.pager_tv_minutes)
        private val btnOpenCountdown: Button = itemView.findViewById(R.id.btn_open_count_down)

        init {
            itemView.setOnClickListener {
                val key = habitList[adapterPosition].first
                onClick(habitList[adapterPosition].second)
            }
        }

        fun bind(pageType: PageType, pageData: Habit) {
            tvTitle.text = pageData.title
            tvStartTime.text = pageData.startTime
            ivPriorityLevel.setImageResource(getImgRes(pageType))
            tvMinutes.text = pageData.minutesFocus.toString()
            btnOpenCountdown.text = itemView.context.getString(R.string.open_count_down)

            btnOpenCountdown.setOnClickListener {
                onClick(habitList[adapterPosition].second)
            }
        }

        private fun getImgRes(pageType: PageType): Int {
            return when (pageType) {
                PageType.HIGH -> R.drawable.ic_priority_high
                PageType.MEDIUM -> R.drawable.ic_priority_medium
                PageType.LOW -> R.drawable.ic_priority_low
            }
        }
    }

    fun submit(key: PageType, habit: Habit) {
        val index = habitList.indexOfFirst { it.first == key }
        if (index != -1) {
            habitList[index] = key to habit
        } else {
            habitList.add(key to habit)
        }
        habitList.sortBy { it.first.ordinal }
        notifyDataSetChanged()
    }
}


