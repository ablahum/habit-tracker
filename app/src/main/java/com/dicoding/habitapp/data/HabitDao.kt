package com.dicoding.habitapp.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.sqlite.db.SupportSQLiteQuery
import com.dicoding.habitapp.utils.HabitSortType
import com.dicoding.habitapp.utils.SortUtils

//TODO 2 : Define data access object (DAO)
@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY time(startTime) ASC")
    fun getHabitsByStartTime(): DataSource.Factory<Int, Habit>

    @Query("SELECT * FROM habits ORDER BY minutesFocus ASC")
    fun getHabitsByMinutesFocus(): DataSource.Factory<Int, Habit>

    @Query("SELECT * FROM habits ORDER BY title ASC")
    fun getHabitsByTitle(): DataSource.Factory<Int, Habit>

    @Query("SELECT * FROM habits WHERE id = :habitId")
    fun getHabitById(habitId: Int): LiveData<Habit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHabit(habit: Habit): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg habits: Habit)

    @Query("DELETE FROM habits WHERE id = :habitId")
    fun deleteHabit(habitId: Int)

    @Query("SELECT * FROM habits WHERE priorityLevel = :level ORDER BY RANDOM() LIMIT 1")
    fun getRandomHabitByPriorityLevel(level: String): LiveData<Habit>
}
