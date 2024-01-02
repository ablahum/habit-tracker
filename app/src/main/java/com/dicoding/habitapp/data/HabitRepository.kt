package com.dicoding.habitapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dicoding.habitapp.utils.HabitSortType
import com.dicoding.habitapp.utils.SortUtils.buildPagedList
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HabitRepository(private val habitDao: HabitDao, private val executor: ExecutorService) {
    companion object {
        @Volatile
        private var instance: HabitRepository? = null

        fun getInstance(context: Context): HabitRepository {
            return instance ?: synchronized(this) {
                if (instance == null) {
                    val database = HabitDatabase.getInstance(context)
                    instance = HabitRepository(
                        database.HabitDao(),
                        Executors.newSingleThreadExecutor()
                    )
                }

                return instance as HabitRepository
            }
        }
    }

    //TODO 4 : Use SortUtils.getSortedQuery to create sortable query and build paged list
    fun getHabits(sortType: HabitSortType): LiveData<PagedList<Habit>> {
        val dataSourceFactory = when (sortType) {
            HabitSortType.START_TIME -> habitDao.getHabitsByStartTime()
            HabitSortType.MINUTES_FOCUS -> habitDao.getHabitsByMinutesFocus()
            HabitSortType.TITLE_NAME -> habitDao.getHabitsByTitle()
        }
        return buildPagedList(dataSourceFactory)
    }

    //TODO 5 : Complete other function inside repository
    fun getHabitById(habitId: Int): LiveData<Habit> {
        return habitDao.getHabitById(habitId)
    }

    fun insertHabit(newHabit: Habit) {
        executor.execute {
            habitDao.insertHabit(newHabit)
        }
    }

    fun deleteHabit(habit: Habit) {
        executor.execute {
            habitDao.deleteHabit(habit.id)
        }
    }

    fun getRandomHabitByPriorityLevel(level: String): LiveData<Habit> {
        return habitDao.getRandomHabitByPriorityLevel(level)
    }
}
