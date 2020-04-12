package com.flaringapp.smadlab3.presentation.fragments.home

import com.flaringapp.smadlab3.data.confidenceIntervals.enums.SignificanceLevel
import com.flaringapp.smadlab3.presentation.mvp.IBaseFragment
import com.flaringapp.smadlab3.presentation.mvp.IBasePresenter
import io.reactivex.Observable

interface HomeContract {

    interface ViewContract: IBaseFragment {
        val numbersInputObservable: Observable<String>
        val significanceLevelInputObservable: Observable<SignificanceLevel>

        fun initInput(input: String)

        fun setNumbersError(error: Int?)

        fun setResult(result: String)
    }

    interface PresenterContract: IBasePresenter<ViewContract> {
        fun onInput(input: String)

        fun onButton1Clicked()
        fun onButton2Clicked()
        fun onButton3Clicked()
    }

    interface IIntervalViewModel {
        val interval: String
        val average: String
        val frequency: String
    }

}