package com.flaringapp.smadlab3.presentation.activities

import androidx.fragment.app.Fragment
import com.flaringapp.smadlab3.presentation.activities.navigation.AppNavigation
import com.flaringapp.smadlab3.presentation.activities.navigation.Screen
import com.flaringapp.smadlab3.presentation.mvp.IBaseActivity
import com.flaringapp.smadlab3.presentation.mvp.IBasePresenter

interface MainContract {

    interface ViewContract: IBaseActivity, AppNavigation {
        fun openScreen(fragment: Fragment)
    }

    interface PresenterContract: IBasePresenter<ViewContract> {
        fun onNavigate(screen: Screen, data: Any?)
    }

}