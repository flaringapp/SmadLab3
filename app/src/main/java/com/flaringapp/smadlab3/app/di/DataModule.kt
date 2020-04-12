package com.flaringapp.smadlab3.app.di

import com.flaringapp.smadlab3.data.calculator.CharacteristicsCalculator
import com.flaringapp.smadlab3.data.calculator.CharacteristicsCalculatorImpl
import com.flaringapp.smadlab3.data.confidenceIntervals.ConfidenceIntervalsCalculator
import com.flaringapp.smadlab3.data.confidenceIntervals.ConfidenceIntervalsCalculatorImpl
import com.flaringapp.smadlab3.data.intervalSplitter.IntervalSplitter
import com.flaringapp.smadlab3.data.intervalSplitter.IntervalSplitterImpl
import org.koin.dsl.module

val dataModule = module {

    single { IntervalSplitterImpl() as IntervalSplitter }

    single { CharacteristicsCalculatorImpl() as CharacteristicsCalculator }

    single { ConfidenceIntervalsCalculatorImpl(get()) as ConfidenceIntervalsCalculator }
}