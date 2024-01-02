package com.dicoding.habitapp.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit

class HabitAdapter(
    private val onClick: (Habit) -> Unit
) : ListAdapter<Habit, HabitAdapter.HabitViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.habit_item, parent, false)
        return HabitViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = getItem(position)
        holder.bind(habit)
    }

    inner class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.item_tv_title)
        private val ivPriority: ImageView = itemView.findViewById(R.id.item_priority_level)
        private val tvStartTime: TextView = itemView.findViewById(R.id.item_tv_start_time)
        private val tvMinutes: TextView = itemView.findViewById(R.id.item_tv_minutes)

        lateinit var getHabit: Habit

        init {
            itemView.setOnClickListener {
                onClick(getHabit)
            }
        }

        fun bind(habit: Habit) {
            getHabit = habit
            tvTitle.text = habit.title
            tvStartTime.text = habit.startTime
            tvMinutes.text = habit.minutesFocus.toString()
            ivPriority.setImageResource(getImgRes(habit.priorityLevel))
        }
    }

    fun getHabitPos(position: Int): Habit {
        return getItem(position)
    }

    private fun getImgRes(priorityLevel: String): Int {
        return when (priorityLevel) {
            "High" -> R.drawable.ic_priority_high
            "Medium" -> R.drawable.ic_priority_medium
            "Low" -> R.drawable.ic_priority_low
            else -> R.drawable.ic_priority_high
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Habit>() {
            override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
                return oldItem == newItem
            }
        }
    }
}
