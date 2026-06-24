package com.gpakorea.gad.sample

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.gad.sdk.Gad
import com.gpakorea.gad.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var mediaKey = "{INSERT_MEDIA_KEY}"
    private var userId = "{INSERT_USER_ID}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.mediaKey.setText(mediaKey)
        binding.userId.setText(userId)

        binding.buttonActivity.setOnClickListener {
            if (checkField()) {
                initializeGad()
                Gad.showAdList(this)
            }
        }

        binding.buttonFragment.setOnClickListener {
            if (checkField()) {
                initializeGad()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment, Gad.getAdListFragment(this))
                    .commitAllowingStateLoss()
            }
        }

        Gad.init(this, mediaKey, userId)

        initializeGad()
    }

    private fun checkField(): Boolean {
        mediaKey = binding.mediaKey.text.toString()
        userId = binding.userId.text.toString()

        if (TextUtils.isEmpty(mediaKey)) {
            binding.mediaKeyLayout.error = "{MEDIA KEY}를 입력해 주세요."
            return false
        } else {
            binding.mediaKeyLayout.isErrorEnabled = false
        }

        if (TextUtils.isEmpty(userId)) {
            binding.userIdLayout.error = "{USER ID}를 입력해 주세요."
            return false
        } else {
            binding.userIdLayout.isErrorEnabled = false
        }
        return true
    }

    private fun initializeGad() {
        Gad.init(this, mediaKey, userId)
//        Gad.setUserInfo("M", 22)
//        Gad.setProgressAnimation(false)
    }
}
