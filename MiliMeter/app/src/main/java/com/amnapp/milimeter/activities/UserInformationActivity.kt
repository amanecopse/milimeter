package com.amnapp.milimeter.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.databinding.ActivityUserInformationBinding

class UserInformationActivity :AppCompatActivity() {

    val binding by lazy { ActivityUserInformationBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 창 닫기
        with(binding) {
            cancelBt.setOnClickListener{
                finish()
            }

            userSaveBt.setOnClickListener {
                // 수정한 회원 정보 저장하기
            }
        }

    }

}