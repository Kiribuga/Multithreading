package com.skillbox.multithreading

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_race_condition.*

class RaceConditionFragment : Fragment(R.layout.fragment_race_condition) {

    private var value: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        executeOperationWithoutSync.setOnClickListener {
            value = 0
            makeIncrementWithoutSync()
        }
        executeOperationWithSync.setOnClickListener {
            value = 0
            makeIncrementWithSync()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun makeIncrementWithoutSync(){
        var requestTime = System.currentTimeMillis()
        val threadCount = countThreads.text.toString().toInt()
        val incrementCount = countIncrement.text.toString().toInt()
        val expectedValue = value + threadCount * incrementCount

        (0 until threadCount).map {
            Thread{
                val startTime = System.currentTimeMillis()
                for (i in 0 until incrementCount) {
                    value++
                }
                requestTime = System.currentTimeMillis() - startTime
            }.apply {
                start()
            }
        }.map { it.join() }

        returnValue.text = "value=$value, expected=$expectedValue, time=$requestTime"
    }

    @SuppressLint("SetTextI18n")
    private fun makeIncrementWithSync(){
        var requestTime = System.currentTimeMillis()
        val threadCount = countThreads.text.toString().toInt()
        val incrementCount = countIncrement.text.toString().toInt()
        val expectedValue = value + threadCount * incrementCount

        (0 until threadCount).map {
            Thread{
                synchronized(this) {
                    val startTime = System.currentTimeMillis()
                    for (i in 0 until incrementCount) {
                        value++
                    }
                    requestTime = System.currentTimeMillis() - startTime
                }
            }.apply {
                start()
            }
        }.map { it.join() }

        returnValue.text = "value=$value, expected=$expectedValue, time=$requestTime"
    }
}