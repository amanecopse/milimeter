package com.amnapp.milimeter.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivityAdminPageBinding
import com.amnapp.milimeter.databinding.ActivityEditSubUserInfoBinding
import com.amnapp.milimeter.databinding.ActivityHomeBinding
import com.amnapp.milimeter.viewModels.AdminPageViewModel
import com.amnapp.milimeter.viewModels.EditSubUserInfoViewModel

class EditSubUserInfoActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditSubUserInfoBinding
    lateinit var mDialog: AlertDialog//로딩화면임. setProgressDialog()를 실행후 mDialog.show()로 시작
    private val viewModel: EditSubUserInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_sub_user_info)


    }
}