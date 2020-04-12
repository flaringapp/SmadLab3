package com.flaringapp.smadlab3.presentation.fragments.home.impl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.flaringapp.app.utils.observeOnUI
import com.flaringapp.app.utils.onApiThread
import com.flaringapp.smadlab3.R
import com.flaringapp.smadlab3.data.confidenceIntervals.enums.SignificanceLevel
import com.flaringapp.smadlab3.presentation.fragments.home.HomeContract
import com.flaringapp.smadlab3.presentation.mvp.BaseFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.scope.currentScope
import java.lang.IllegalStateException

class HomeFragment : BaseFragment<HomeContract.PresenterContract>(), HomeContract.ViewContract {

    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override val presenter: HomeContract.PresenterContract by currentScope.inject()

    override fun onInitPresenter() {
        presenter.view = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViews() {
        inputNumbers.doAfterTextChanged {
            numbersInputSubject.onNext(it.toString())
        }

        groupSignificanceInterval.setOnCheckedChangeListener { _, checkedId ->
            val level = when (checkedId) {
                R.id.buttonSignificanceInterval1 -> SignificanceLevel.L05
                R.id.buttonSignificanceInterval2 -> SignificanceLevel.L01
                else -> throw IllegalStateException("Stub")
            }
            significanceLevelChangeSubject.onNext(level)
        }

        button1.setOnClickListener { presenter.onButton1Clicked() }
        button2.setOnClickListener { presenter.onButton2Clicked() }
        button3.setOnClickListener { presenter.onButton3Clicked() }
    }

    private val numbersInputSubject = PublishSubject.create<String>()
    override val numbersInputObservable: Observable<String> = numbersInputSubject
        .onApiThread()
        .observeOnUI()

    private val significanceLevelChangeSubject = PublishSubject.create<SignificanceLevel>()
    override val significanceLevelInputObservable: Observable<SignificanceLevel> =
        significanceLevelChangeSubject
            .onApiThread()
            .observeOnUI()

    override fun initInput(input: String) {
        inputNumbers.setText(input)
    }

    override fun setNumbersError(error: Int?) {
        layoutNumbersInput.error = error?.let { getString(it) }
    }

    override fun setResult(result: String) {
        textResult.text = result
    }
}