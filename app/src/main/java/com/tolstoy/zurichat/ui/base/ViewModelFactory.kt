package com.tolstoy.zurichat.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tolstoy.zurichat.data.repository.DmRepository
import com.tolstoy.zurichat.data.repository.ImagesRepository
import com.tolstoy.zurichat.ui.dm.DMViewModel

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 19-Sep-21
 */
class ViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DMViewModel::class.java) -> {
                DMViewModel(DmRepository.INSTANCE, ImagesRepository.INSTANCE) as T
            }
            else -> throw IllegalArgumentException("Unknown class name")
        }
    }

    companion object {
        val INSTANCE by lazy { ViewModelFactory() }
    }
}