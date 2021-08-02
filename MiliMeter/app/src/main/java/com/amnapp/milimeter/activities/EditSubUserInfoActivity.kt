package com.amnapp.milimeter.activities

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.amnapp.milimeter.AccountManager
import com.amnapp.milimeter.GroupMemberData
import com.amnapp.milimeter.UserData
import com.amnapp.milimeter.databinding.ActivityEditSubUserInfoBinding
import com.google.android.material.R
import java.text.SimpleDateFormat
import java.util.*

class EditSubUserInfoActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditSubUserInfoBinding
    lateinit var mLoadingDialog: AlertDialog//로딩화면임. setProgressDialog()를 실행후 mLoadingDialog.show()로 시작
    lateinit var mChildUserData: UserData
    lateinit var mChildGroupMemberData: GroupMemberData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSubUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        setProgressDialog()// 로딩다이얼로그 설치
        loadUserData()

        binding.pwEt.addTextChangedListener {
            checkEditTextError()
        }
        binding.militaryIdEt.addTextChangedListener {
            checkEditTextError()
        }
        binding.heightEt.addTextChangedListener {
            checkEditTextError()
        }
        binding.weightEt.addTextChangedListener {
            checkEditTextError()
        }

        binding.confirmCv.setOnClickListener{
            uploadEditedUserData()
        }
        binding.birthDateLl.setOnClickListener {
            showDatePickerDialog()
        }
        binding.withdrawBt.setOnClickListener {
            showTwoButtonDialogMessage("주의", "정말 탈퇴시키겠습니까?"){
                if(it != -1)
                    return@showTwoButtonDialogMessage

                binding.withdrawBt.isClickable = false
                mLoadingDialog.show()
                AccountManager().leaveGroup(mChildGroupMemberData.indexHashCode!!){
                    Toast.makeText(applicationContext, "해당 하위유저가 탈퇴되었습니다", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }

        binding.cancelIb.setOnClickListener{
            finish()
        }
        binding.backIb.setOnClickListener{
            finish()
        }

    }

    private fun uploadEditedUserData() {
        if (!checkEditTextError()) {
            showDialogMessage("필수입력란 미기입", "비어있는 필수입력란을 기입해주세요") {}
            return
        } else if (!AccountManager().checkNetworkState(this)) {
            showDialogMessage("네트워크 오류", "네트워크에 연결되어 있는 지 확인해주세요") {}
            return
        }
        mChildUserData.pw = binding.pwEt.text.toString()
        mChildUserData.birthDate =
            if (binding.birthDateTv.text.isNullOrEmpty()) null else binding.birthDateTv.text.toString()
        mChildUserData.militaryId = binding.militaryIdEt.text.toString()
        mChildUserData.height = binding.heightEt.text.toString()
        mChildUserData.weight = binding.weightEt.text.toString()
        mChildUserData.bloodType = binding.bloodTypeSp.selectedItemPosition
        mChildUserData.goalOfWeight =
            if (binding.goalOfWeightEt.text.isNullOrEmpty()) null else binding.goalOfWeightEt.text.toString()
        mChildUserData.goalOfTotalGrade = binding.goalOfTotalRankSp.selectedItemPosition
        mChildUserData.goalOfLegTuckGrade = binding.goalOfLegTuckRankSp.selectedItemPosition
        mChildUserData.goalOfShuttleRunGrade = binding.goalOfShuttleRunRankSp.selectedItemPosition
        mChildUserData.goalOfFieldTrainingGrade =
            binding.goalOfFieldTrainingRankSp.selectedItemPosition

        mLoadingDialog.show() //로딩시작
        AccountManager().uploadUserData(mChildUserData) { message ->
            if (message == AccountManager.RESULT_SUCCESS)
                showDialogMessage("수정 완료", "하위회원의 정보가 수정되었습니다")
            mLoadingDialog.dismiss()
        }
    }

    private fun loadUserData() {
        mChildUserData = intent.extras?.getSerializable(UserData.USER_CHILD) as UserData
        mChildGroupMemberData =
            intent.extras?.getSerializable(GroupMemberData.GROUP_MEMBER_CHILD) as GroupMemberData
        if (mChildUserData != null) {
            binding.nameTv.text = mChildUserData.name
            binding.idTv.text = mChildUserData.id
            binding.pwEt.setText(mChildUserData.pw)
            mChildUserData.birthDate?.let { binding.birthDateTv.setText(it.toString()) }
            mChildUserData.militaryId?.let { binding.militaryIdEt.setText(it.toString()) }
            mChildUserData.height?.let { binding.heightEt.setText(it.toString()) }
            mChildUserData.weight?.let { binding.weightEt.setText(it.toString()) }
            mChildUserData.bloodType?.let { binding.bloodTypeSp.setSelection(it) }
            mChildUserData.goalOfWeight?.let { binding.goalOfWeightEt.setText(it.toString()) }
            mChildUserData.goalOfTotalGrade?.let { binding.goalOfTotalRankSp.setSelection(it) }
            mChildUserData.goalOfLegTuckGrade?.let { binding.goalOfLegTuckRankSp.setSelection(it) }
            mChildUserData.goalOfShuttleRunGrade?.let {
                binding.goalOfShuttleRunRankSp.setSelection(
                    it
                )
            }
            mChildUserData.goalOfFieldTrainingGrade?.let {
                binding.goalOfFieldTrainingRankSp.setSelection(
                    it
                )
            }
        }
    }

    private fun checkEditTextError(): Boolean{

        var valid = true

        if (binding.pwEt.text.isNullOrEmpty()
            ||binding.pwEt.text!!.length < 5
            ||binding.pwEt.text!!.length > 20){
            binding.pwEt.setError("5~20자리 글자로 작성해주세요",
                ContextCompat.getDrawable(this, R.drawable.mtrl_ic_error))
            binding.pwTl.helperText = binding.pwEt.error
            valid = false
        }
        else{
            binding.pwEt.error = null
            binding.pwTl.helperText = null
        }

        if (binding.militaryIdEt.text.isNullOrEmpty()){
            binding.militaryIdEt.setError("필수입력란 입니다",
                ContextCompat.getDrawable(this, R.drawable.mtrl_ic_error))
            binding.militaryIdTl.helperText = binding.militaryIdEt.error
            valid = false
        }
        else{
            binding.militaryIdEt.error = null
            binding.militaryIdTl.helperText = null
        }

        if (binding.heightEt.text.isNullOrEmpty()){
            binding.heightEt.setError("필수입력란 입니다",
                ContextCompat.getDrawable(this, R.drawable.mtrl_ic_error))
            binding.heightTl.helperText = binding.heightEt.error
            valid = false
        }
        else{
            binding.heightEt.error = null
            binding.heightTl.helperText = null
        }

        if (binding.weightEt.text.isNullOrEmpty()){
            binding.weightEt.setError("필수입력란 입니다",
                ContextCompat.getDrawable(this, R.drawable.mtrl_ic_error))
            binding.weightTl.helperText = binding.weightEt.error
            valid = false
        }
        else{
            binding.weightEt.error = null
            binding.weightTl.helperText = null
        }

        return valid
    }

    fun showDialogMessage(title:String, body:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(body)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int -> }
        builder.show()
    }

    fun showDialogMessage(title:String, body:String, callBack: () -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(body)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
            callBack()
        }
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

    private fun showDatePickerDialog() {
        val callBack = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            binding.birthDateTv.text = ""+year+"."+ String.format("%02d", month)+"."+String.format("%02d", dayOfMonth)
        }
        val year = SimpleDateFormat("yyyy").format(Date()).toInt()
        val month = SimpleDateFormat("MM").format(Date()).toInt()
        val day = SimpleDateFormat("dd").format(Date()).toInt()

        if(mChildUserData.birthDate.isNullOrEmpty())
            DatePickerDialog(this, callBack,year-20,month,day).show()
        else{
            val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd").parse(mChildUserData.birthDate)
            val userYear = SimpleDateFormat("yyyy").format(simpleDateFormat).toInt()
            val userMonth = SimpleDateFormat("MM").format(simpleDateFormat).toInt()
            val userDay = SimpleDateFormat("dd").format(simpleDateFormat).toInt()
            DatePickerDialog(this, callBack,userYear,userMonth,userDay).show()
        }
    }
}