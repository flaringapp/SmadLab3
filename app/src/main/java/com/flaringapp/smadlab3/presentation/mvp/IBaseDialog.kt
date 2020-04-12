package com.flaringapp.smadlab3.presentation.mvp

interface IBaseDialog: IBaseView {
    val dialogTag: String?

    fun close()
}