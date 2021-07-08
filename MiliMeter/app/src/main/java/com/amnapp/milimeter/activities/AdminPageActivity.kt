package com.amnapp.milimeter.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivityAdminPageBinding
import com.amnapp.milimeter.recyclerViews.AdminPageRecyclerAdapter
import com.amnapp.milimeter.viewModels.AdminPageViewModel

class AdminPageActivity : AppCompatActivity() {

    lateinit var binding: ActivityAdminPageBinding
    private val viewModel: AdminPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_page)

        initUI()
    }

    private fun initUI() {
        binding.pathEt.setSelection(binding.pathEt.length())//항상 마지막 경로로 커서가 오도록 설정
        binding.subUserListRv.layoutManager = LinearLayoutManager(this)// 리사이클러뷰 리스트에 레이아웃 매니저를 설정

        viewModel.parentName.observe(this, Observer { // 현재부모가 디렉토리 이동으로 바뀌면 그 이름으로 UI갱신
            binding.parentNameTv.text = it
        })
        viewModel.pathList.observe(this, Observer {
            var path = ""
            for (i in it){
                path += "/"+i.userName
            }
            binding.pathEt.setText(path)
        })
        viewModel.subUserList.observe(this, Observer {
            val adminPageRecyclerAdapter = AdminPageRecyclerAdapter(it)
            binding.subUserListRv.adapter = adminPageRecyclerAdapter
            Log.d("asdAct", it.toString())
        })

        binding.cancelLl.setOnClickListener {
            finish()
        }
    }
}