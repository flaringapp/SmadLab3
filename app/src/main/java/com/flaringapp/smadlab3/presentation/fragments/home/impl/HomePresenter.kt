package com.flaringapp.smadlab3.presentation.fragments.home.impl

import androidx.core.text.isDigitsOnly
import com.flaringapp.smadlab3.R
import com.flaringapp.smadlab3.data.confidenceIntervals.ConfidenceIntervalsCalculator
import com.flaringapp.smadlab3.data.confidenceIntervals.ConfidenceIntervalsCalculator.IConfidenceInterval
import com.flaringapp.smadlab3.data.confidenceIntervals.enums.SignificanceLevel
import com.flaringapp.smadlab3.presentation.fragments.home.HomeContract
import com.flaringapp.smadlab3.presentation.mvp.BasePresenter
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import java.text.DecimalFormat

class HomePresenter(
    private val confidenceIntervalsCalculator: ConfidenceIntervalsCalculator
) : BasePresenter<HomeContract.ViewContract>(), HomeContract.PresenterContract {

    companion object {
        private const val SPACE = " "

        private val decimalFormat: DecimalFormat = DecimalFormat("0.0000")

        private const val DEFINED_INPUT = "0.25 0.32 0.36 0.44 0.65 0.88 0.57 " +
                "0.37 0.37 0.30 0.38 0.33 0.22 0.28 " +
                "0.26 0.30 0.29 0.32 0.36 0.37 0.37 " +
                "0.14 0.25 0.31 0.36 0.65 0.65 0.70"
    }

    private var numbers: String = ""
    private var significanceLevel: SignificanceLevel = SignificanceLevel.L05

    private val inputRequests = CompositeDisposable()

    private var calculationRequest: Disposable? = null

    private var pendingNumberInputAction: ((Int) -> Unit)? = null

    override fun onStart() {
        super.onStart()

        inputRequests += view!!.numbersInputObservable
            .doOnNext { numbers = it }
            .subscribe {
                view?.setNumbersError(null)
            }

        inputRequests += view!!.significanceLevelInputObservable
            .doOnNext { significanceLevel = it }
            .subscribe()

        view?.initInput(DEFINED_INPUT)
    }

    override fun release() {
        calculationRequest?.dispose()
        inputRequests.dispose()
        super.release()
    }

    override fun onInput(input: String) {
        if (!input.isDigitsOnly()) return
        pendingNumberInputAction?.invoke(input.toInt())
    }

    override fun onButton1Clicked() {
        if (!validateInput()) return

        calculation(
            confidenceIntervalsCalculator.mathExpectationVarianceKnown(
                significanceLevel,
                *parseNumbers()
            )
        )
    }

    override fun onButton2Clicked() {
        if (!validateInput()) return

        calculation(
            confidenceIntervalsCalculator.mathExpectationVarianceUnknown(
                significanceLevel,
                *parseNumbers()
            )
        )
    }

    override fun onButton3Clicked() {
        if (!validateInput()) return

        calculation(
            confidenceIntervalsCalculator.meanSquaredDivision(
                significanceLevel,
                *parseNumbers()
            )
        )
    }

    private fun calculation(request: Single<IConfidenceInterval>) {
        calculationRequest?.dispose()
        calculationRequest = request
            .subscribe(
                { view?.setResult(it.format()) },
                { view?.handleError(it) }
            )
    }

    private fun parseNumbers() = numbers.trim().split(SPACE)
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .map { it.toDouble() }
        .toDoubleArray()


    private fun validateInput(): Boolean {
        if (numbers.trim().isEmpty()) {
            view?.setNumbersError(R.string.input_too_short)
            return false
        }

        val numbersSplit = numbers.trim().split(SPACE)
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        if (numbersSplit.find { !it.isDecimal() } != null) {
            view?.setNumbersError(R.string.input_invalid_only_decimal)
            return false
        }

        if (numbersSplit.find { it == "." } != null) {
            view?.setNumbersError(R.string.input_invalid_dot)
            return false
        }

        if (numbersSplit.isEmpty()) {
            view?.setNumbersError(R.string.input_too_short)
            return false
        }

        return true
    }

    private fun String.isDecimal(): Boolean {
        return let {
            if (startsWith('-')) {
                if (length == 1) return false
                substring(1)
            }
            else this
        }
            .find { !it.isDigit() && it != '.' } == null
    }

    private fun IConfidenceInterval.format(): String {
        return "[${decimalFormat.format(left)} ; ${decimalFormat.format(right)}]"
    }
}
