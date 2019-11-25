package com.example.learnplaid.pages

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.learnplaid.GlideApp
import com.example.learnplaid.R
import com.example.learnplaid.databinding.ActivityDetailBinding
import com.example.learnplaid.trans.ElasticDragDismissFrameLayout

class DetailActivity: AppCompatActivity() {

    lateinit var binding: ActivityDetailBinding

    lateinit var chromeFader: ElasticDragDismissFrameLayout.SystemChromeFader
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityDetailBinding>(this, R.layout.activity_detail)
        binding.lifecycleOwner = this

        val url = intent.getStringExtra("url") ?: ""
        GlideApp.with(this).load(url).into(binding.shot)

        binding.apply {
            bodyScroll.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                shot.offset = -scrollY
            }
//            back.setOnClickListener { setResultAndFinish() }
        }

        chromeFader = object : ElasticDragDismissFrameLayout.SystemChromeFader(this) {
            override fun onDragDismissed() {
                setResultAndFinish()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        binding.draggableFrame.addListener(chromeFader)
    }

    override fun onPause() {
        binding.draggableFrame.removeListener(chromeFader)
        super.onPause()
    }


    override fun onBackPressed() {
        setResultAndFinish()
    }

    override fun onNavigateUp(): Boolean {
        setResultAndFinish()
        return true
    }


    internal fun setResultAndFinish() {
        setResult(Activity.RESULT_OK)
        finishAfterTransition()
    }
}