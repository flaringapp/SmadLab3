package com.flaringapp.smadlab3.data.confidenceIntervals.enums

enum class SignificanceLevel(
    val level: Double,
    val laplace: Double,
    val student: Double,
    val pirson: Double
) {
    L05(0.05, 0.1825, 2.05, 16.2),
    L01(0.01, 0.1915, 2.77, 12.9)
}