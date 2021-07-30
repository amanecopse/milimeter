package com.amnapp.milimeter.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.amnapp.milimeter.AccountManager
import com.amnapp.milimeter.PreferenceManager
import com.amnapp.milimeter.R
import com.amnapp.milimeter.UserData
import com.amnapp.milimeter.databinding.ActivityHomeBinding
import com.amnapp.milimeter.databinding.ActivitySettingBinding
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {

    val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    lateinit var mLoadingDialog: AlertDialog//로딩화면임. setProgressDialog()를 실행후 mLoadingDialog.show()로 시작

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initUI()
        autoLogin()
    }

    private fun initUI() {
        setProgressDialog()

        binding.debugBt.setOnClickListener {//디버그 창으로 가는 코드
            val intent = Intent(this, DebugActivity::class.java)
            startActivity(intent)
        }
        // 각 아이콘 창으로 이동 -> 아이콘 버튼 클릭시 화면 전환
        binding.homeBt.setOnClickListener {
            val homeintent = Intent(this, HomeActivity::class.java)
            startActivity(homeintent)
        }

        binding.bodyBt.setOnClickListener {
            val bodyintent = Intent(this, BodyActivity::class.java)
            startActivity(bodyintent)
        }

       binding.resultBt.setOnClickListener {
            val resultintent = Intent(this, ResultActivity::class.java)
           startActivity(resultintent)
       }

        binding.goalBt.setOnClickListener {
            val goalintent = Intent(this, GoalActivity::class.java)
            startActivity(goalintent)
        }

        binding.settingBt.setOnClickListener {//설정 창으로 가는 코드
            val settingintent = Intent(this, SettingActivity::class.java)
            startActivity(settingintent)
        }

        //DdayBt default
        binding.DdayBt.setText("전역일 설정")

        //Dday 날짜설정
        binding.DdayBt.setOnClickListener {
            val today = GregorianCalendar()
            val year: Int = today.get(Calendar.YEAR)
            val month: Int = today.get(Calendar.MONTH)
            val date: Int = today.get(Calendar.DATE)

            val Tyear: Int = year*10000
            val Tmonth: Int = (month+1)*100
            val Tdate: Int = date
            val Today: Int = Tyear+Tmonth+Tdate

            val Dday = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {

               override fun onDateSet(view: DatePicker?, year: Int, month: Int, date: Int) {

                   val Syear: Int = year*10000
                   val Smonth: Int = (month+1)*100
                   val Sdate: Int = date
                   val Setday: Int = Syear+Smonth+Sdate
                   val dday: Int = Setday-Today

                   if (dday==0) {
                       binding.DdayBt.setText("D-day")}
                   else{
                       binding.DdayBt.setText("D-${dday}")}

                }

            }, year, month, date)

            Dday.show()
        }
    }

    private fun autoLogin() {
        mLoadingDialog.show() // 로딩화면 실행

        val pm = PreferenceManager()
        val loginDataArray = pm.getLoginData(this)// 로그인 데이터를 로컬저장소에서 로드함

        if(loginDataArray[0].isNullOrEmpty()// 로그인 정보 없는 경우
            ||loginDataArray[1].isNullOrEmpty()
            ||loginDataArray[2].isNullOrEmpty()){
            mLoadingDialog.dismiss()//로딩해제
            return// 로딩해제하고 빠져나옴
        }

        AccountManager().login(loginDataArray[0]!!, loginDataArray[1]!!, loginDataArray[2]!!){result ->
            if(result == AccountManager.LOGIN_SUCCESS){
                Toast.makeText(this@HomeActivity, "로그인", Toast.LENGTH_SHORT).show()
            }
            mLoadingDialog.dismiss() // 로딩해제
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
}



