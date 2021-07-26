package com.amnapp.milimeter.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.databinding.ActivityPrivateAdministrationBinding

class PrivateAdministrationActivity :AppCompatActivity() {

    val binding by lazy { ActivityPrivateAdministrationBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 창닫기
        with(binding) {
            cancelBt.setOnClickListener{
                finish()
            }
            passwordCheckBt.setOnClickListener {
                // 비밀번호가 맞는지 확인작업 수행
            }
            passwordSaveBt.setOnClickListener {
                // 새로운 비밀번호 수정 및 저장작업 수행
            }
        }
    }

}