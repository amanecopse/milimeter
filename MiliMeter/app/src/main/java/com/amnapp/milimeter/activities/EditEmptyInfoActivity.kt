package com.amnapp.milimeter.activities

import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.amnapp.milimeter.AccountManager
import com.amnapp.milimeter.GroupMemberData
import com.amnapp.milimeter.UserData
import com.amnapp.milimeter.databinding.ActivityAdminPageBinding
import com.amnapp.milimeter.databinding.ActivityEditEmptyInfoBinding
import com.amnapp.milimeter.viewModels.AdminPageViewModel

class EditEmptyInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditEmptyInfoBinding
    lateinit var mLoadingDialog: AlertDialog//로딩화면임. setProgressDialog()를 실행후 mLoadingDialog.show()로 시작
    lateinit var mSubUserData: UserData
    lateinit var mSubGroupMemberData: GroupMemberData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditEmptyInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        setProgressDialog()

        mSubUserData = UserData.mTmpUserData!!
        mSubGroupMemberData = GroupMemberData.mTmpGroupMemberData!!// 지금 이 공석인 계정

        binding.deleteGroupMemberAccountTv.setOnClickListener {
            if(mSubGroupMemberData.childCount != 0){
                showDialogMessage("삭제불가", "하위유저를 가지고 있어 삭제할 수 없습니다"){}
            }else{
                showTwoButtonDialogMessage("주의", "정말 삭제하시겠습니까?"){
                    if(it != -1)
                        return@showTwoButtonDialogMessage

                    binding.deleteGroupMemberAccountTv.isClickable = false
                    mLoadingDialog.show()
                    AccountManager().deleteGroupMemberAccount(
                        GroupMemberData.mParentTmpGroupMemberData!!,
                        mSubGroupMemberData
                    ){
                        Toast.makeText(this, "삭제하였습니다", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }

        binding.cancelIb.setOnClickListener {
            finish()
        }
        binding.backIb.setOnClickListener {
            finish()
        }

    }

    fun setProgressDialog() {
        val llPadding = 30
        val ll = LinearLayout(this)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam
        val progressBar = ProgressBar(this)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam
        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(this)
        tvText.text = "Loading ..."
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20f
        tvText.layoutParams = llParam
        ll.addView(progressBar)
        ll.addView(tvText)
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(ll)
        mLoadingDialog = builder.create()
    }

    fun showDialogMessage(title: String, body: String, callBack: () -> Unit) {//다이얼로그 메시지를 띄우는 함수
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(body)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
            callBack()
        }
        builder.show()
    }

    fun showEditTextDialogMessage(title: String, callBack: (input: String) -> Unit) {//다이얼로그 메시지를 띄우는 함수
        val editText = EditText(this)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setView(editText)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int -> callBack(editText.text.toString())}
        builder.setNegativeButton("취소") { dialogInterface: DialogInterface, i: Int -> }
        builder.show()
    }

    fun showTwoButtonDialogMessage(title: String, body: String, callBack: (Int) -> Unit) {//다이얼로그 메시지를 띄우는 함수
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(body)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int -> callBack(i)}
        builder.setNegativeButton("취소") { dialogInterface: DialogInterface, i: Int -> callBack(i)}
        builder.show()
    }
}