package com.dicoding.habitapp.utils

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.sqlite.db.SimpleSQLiteQuery
import com.dicoding.habitapp.data.Habit

object SortUtils {

    fun buildPagedList(dataSourceFactory: DataSource.Factory<Int, Habit>): LiveData<PagedList<Habit>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(10)
            .setPageSize(20)
            .build()
        return LivePagedListBuilder(dataSourceFactory, config).build()
    }
}