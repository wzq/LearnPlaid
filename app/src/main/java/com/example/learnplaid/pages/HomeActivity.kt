package com.example.learnplaid.pages

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnplaid.R
import com.example.learnplaid.ViewModelFactory
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private val viewModel by viewModels<HomeViewModel> { ViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setActionBar(toolbar)

        val adapter = PlaidAdapter(this, false)
        setExitSharedElementCallback(PlaidAdapter.createSharedElementReenterCallback(this))

        val gridLayoutManager = GridLayoutManager(this@HomeActivity, 2)
        grid.layoutManager = gridLayoutManager
        grid.adapter = adapter
        grid.setHasFixedSize(true)
        grid.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                // we want the grid to scroll over the top of the toolbar but for the toolbar items
                // to be clickable when visible. To achieve this we play games with elevation. The
                // toolbar is laid out in front of the grid but when we scroll, we lower it's elevation
                // to allow the content to pass in front (and reset when scrolled to top of the grid)
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                    gridLayoutManager.findFirstVisibleItemPosition() == 0 &&
                    gridLayoutManager.findViewByPosition(0)!!.top == grid.paddingTop &&
                    toolbar.translationZ != 0f
                ) {
                    // at top, reset elevation
                    toolbar.translationZ = 0f
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING && toolbar.translationZ != -1f) {
                    // grid scrolled, lower toolbar to allow content to pass in front
                    toolbar.translationZ = -1f
                }
            }
        })

        viewModel.sources.observe(this) {
            adapter.items = it.getOrNull() ?: return@observe
        }

        lifecycleScope.launch {  }

    }


}