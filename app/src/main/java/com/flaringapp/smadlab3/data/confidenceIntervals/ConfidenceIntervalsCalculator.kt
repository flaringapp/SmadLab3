package com.flaringapp.smadlab3.data.confidenceIntervals

import com.flaringapp.smadlab3.data.confidenceIntervals.enums.SignificanceLevel
import io.reactivex.Single

interface ConfidenceIntervalsCalculator {

    fun mathExpectationVarianceKnown(
        significanceLevel: SignificanceLevel,
        vararg numbers: Double
    ): Single<IConfidenceInterval>

    fun mathExpectationVarianceUnknown(
        significanceLevel: SignificanceLevel,
        vararg numbers: Double
    ): Single<IConfidenceInterval>

    fun meanSquaredDivision(
        significanceLevel: SignificanceLevel,
        vararg numbers: Double
    ): Single<IConfidenceInterval>

    interface IConfidenceInterval {
        val left: Double
        val right: Double
    }
}