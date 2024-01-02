package com.dicoding.habitapp.data

import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dicoding.habitapp.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import com.dicoding.habitapp.data.HabitDao
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import org.json.JSONException
import java.io.IOException


// TODO 3: Define room database class and prepopulate database using JSON
@Database(entities = [Habit::class], version = 1, exportSchema = false)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun HabitDao(): HabitDao

    companion object {
        @Volatile
        private var INSTANCE: HabitDatabase? = null

        @OptIn(DelicateCoroutinesApi::class)
        fun getInstance(context: Context): HabitDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HabitDatabase::class.java,
                    "task.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            GlobalScope.launch(Dispatchers.IO) {
                                fillWithStartingData(context, getInstance(context).HabitDao())
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private fun fillWithStartingData(context: Context, dao: HabitDao) {
            val task = loadJsonObject(context)

            try {
                if (task != null) {
                    val tasksArray = task.getJSONArray("habits")
                    for (i in 0 until tasksArray.length()) {
                        val item = tasksArray.getJSONObject(i)
                        dao.insertAll(
                            Habit(
                                item.getInt("id"),
                                item.getString("title"),
                                item.getLong("focusTime"),
                                item.getString("startTime"),
                                item.getString("priorityLevel")
                            )
                        )
                    }
                }
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
        }

        private fun loadJsonObject(context: Context): JSONObject? {
            val builder = StringBuilder()
            val resourceId = R.raw.habit
            val `in` = context.resources.openRawResource(resourceId)
            val reader = BufferedReader(InputStreamReader(`in`))

            var line: String?

            try {
                while (reader.readLine().also { line = it } != null) {
                    builder.append(line)
                }
                return JSONObject(builder.toString())
            } catch (exception: IOException) {
                exception.printStackTrace()
            } catch (exception: JSONException) {
                exception.printStackTrace()
            } finally {
                `in`.close()
            }

            return null
        }
    }
}

