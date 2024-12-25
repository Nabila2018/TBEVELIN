package com.example.evelin

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.evelin.data.UserRepository
import com.example.evelin.di.Injection
import com.example.evelin.ui.addEvent.AddEventViewModel
import com.example.evelin.ui.editEvent.EditEventViewModel
import com.example.evelin.ui.eventDetail.EventDetailViewModel
import com.example.evelin.ui.history.EventHistoryViewModel
import com.example.evelin.ui.home.HomeViewModel
import com.example.evelin.ui.login.LoginViewModel
import com.example.evelin.ui.myEvent.MyEventViewModel
import com.example.evelin.ui.profile.ProfileViewModel
import com.example.evelin.ui.register.RegisterViewModel
import com.example.evelin.ui.registerEvent.RegisterEventViewModel
import com.example.evelin.ui.userInfo.UserInfoViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(EventDetailViewModel::class.java) -> {
                EventDetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterEventViewModel::class.java) -> {
                RegisterEventViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddEventViewModel::class.java) -> {
                AddEventViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UserInfoViewModel::class.java) -> {
                UserInfoViewModel(repository) as T
            }
            modelClass.isAssignableFrom(EventHistoryViewModel::class.java) -> {
                EventHistoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MyEventViewModel::class.java) -> {
                MyEventViewModel(repository) as T
            }
            modelClass.isAssignableFrom(EditEventViewModel::class.java) -> {
                EditEventViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}