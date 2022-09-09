package com.skillbox.multithreading.threading

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.skillbox.multithreading.AdapterMovie
import com.skillbox.multithreading.R
import com.skillbox.multithreading.ViewModelMovie
import kotlinx.android.synthetic.main.fragment_threading.*

class ThreadingFragment : Fragment(R.layout.fragment_threading) {

    private val viewModel: ViewModelMovie by viewModels()

    private var adapterMovie: AdapterMovie? = null

    private lateinit var handler: Handler

    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backgroundThread = HandlerThread("handler thread").apply {
            start()
        }
        handler = Handler(backgroundThread.looper)
        initList()

        swipeRefresh.setOnRefreshListener {
            handler.post {
                val run = Runnable {
                    viewModel.requestMovie()
                    mainHandler.post {
                        observeViewModel()
                    }
                    mainHandler.postDelayed({
                        Toast.makeText(requireContext(), "Список обновлен!", Toast.LENGTH_SHORT).show()
                    }, 1000)
                    swipeRefresh.isRefreshing = false
                }
                handler.postDelayed(run, 2000.toLong())
            }
        }

        getList.setOnClickListener {
            handler.post {
                viewModel.requestMovie()
                mainHandler.post {
                    observeViewModel()
                }

                mainHandler.postDelayed({
                    Toast.makeText(requireContext(), "Список обновлен!", Toast.LENGTH_SHORT).show()
                }, 1000)
            }

        }
    }

    private fun initList() {
        adapterMovie = AdapterMovie()
        with(listMovie) {
            adapter = adapterMovie
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeViewModel() {
        viewModel.movies.observe(viewLifecycleOwner) {
            adapterMovie?.updateListMovies(viewModel.movies.value!!)
            adapterMovie?.notifyDataSetChanged()
        }
    }
}