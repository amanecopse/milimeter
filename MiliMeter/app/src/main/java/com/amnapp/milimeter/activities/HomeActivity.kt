package com.amnapp.milimeter.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.DrawableCompat.clearColorFilter
import com.amnapp.milimeter.*
import com.amnapp.milimeter.databinding.ActivityHomeBinding
import java.io.IOException
import java.util.*
import kotlin.system.exitProcess

class HomeActivity : CustomThemeActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var mLoadingDialog: AlertDialog//로딩화면임. setProgressDialog()를 실행후 mLoadingDialog.show()로 시작

    //프로필사진 설정
    private val GALLERY = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadTheme()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 특별한 테마창 아이콘
        if (mTheme == PreferenceManager.THEME_SPECIAL_FIRST || mTheme == PreferenceManager.THEME_SPECIAL_SECOND || mTheme == PreferenceManager.THEME_SPECIAL_THIRD) {
            binding.homeBt.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.home_special_icon,0,0)
            binding.bodyBt.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.body_special_icon,0,0)
            binding.resultBt.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.result_special_icon,0,0)
            binding.goalBt.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.goal_special_icon,0,0)
            binding.settingBt.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.setting_special_icon,0,0)
        }

        initUI()
        autoLogin()
        loadProfile()
        loadDebug()
    }


    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if( resultCode == Activity.RESULT_OK){
            if( requestCode ==  GALLERY){
                var ImnageData: Uri? = data?.data
                Toast.makeText(this,ImnageData.toString(), Toast.LENGTH_SHORT ).show()
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, ImnageData)
                    val pm = PreferenceManager()
                    pm.setProfileImage(this, bitmap)
                    binding.profileCiv.setImageBitmap(bitmap)
                }
                catch (e:Exception){
                    e.printStackTrace()

                }
            }

        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_A -> {//A누르면 디버그 패스워드 창 표시
                binding.debugLl.visibility = View.VISIBLE
                return true
            }
        }
        return false
    }

    private fun loadDebug(){//디버그 창에 진입하기 위해 패스워드를 입력하는 과정
        binding.debugBt.setOnClickListener {//디버그 창으로 가는 코드
            val intent = Intent(this, DebugActivity::class.java)
            startActivity(intent)
        }

        binding.debugEt.setOnKeyListener { v, keyCode, event ->
            if(keyCode==KeyEvent.KEYCODE_ENTER){
                if(binding.debugEt.text.toString() != "debug")
                    return@setOnKeyListener true
                binding.debugBt.visibility = View.VISIBLE
            }
            return@setOnKeyListener true
        }
    }

    fun Context.assetsToBitmap(fileName:String): Bitmap?{
        return try {
            val stream = assets.open(fileName)
            BitmapFactory.decodeStream(stream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun initUI() {
        setProgressDialog()

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

        binding.loginBt.setOnClickListener {//설정 창으로 가는 코드
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.profilechangeBt.setOnClickListener{//프로필 이미지 변경 버튼
            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent,GALLERY)
        }

        //DdayBt default
//        binding.dDayBt.setText("전역일 설정")

        //Dday 날짜설정
        binding.dDayBt.setOnClickListener {
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
                       binding.dDayBt.text=("D-day")}
                   else if (dday<0){
                       val builder = AlertDialog.Builder(this@HomeActivity)
                       builder.setTitle("잘못된 날짜 선택입니다")
                       builder.setMessage("오늘 이후의 날짜를 선택해주세요")
                       builder.setPositiveButton("네",null)
                       builder.show()
                   }
                   else{
                       binding.dDayBt.text=("D-${dday}")}

                   PreferenceManager().setDDay(applicationContext, dday)// 이미지 캐시  저장

                }

            }, year, month, date)

            Dday.show()
        }
    }

    private fun loadProfile(){
        val userData = UserData.getInstance()

        if(userData.login){//로그인 되어있으면 회원정보랑 그룹코드 로드
            binding.nameTv.text = userData.name
            binding.militaryIdTv.text = userData.militaryId
            binding.groupCodeTv.text = AccountManager.mGroupCode
        }
        else{// 안되어있으면 비움
            binding.nameTv.text = ""
            binding.militaryIdTv.text = ""
            binding.groupCodeTv.text = ""
        }

        val pm = PreferenceManager()
        val bitmap = pm.getProfileImage(this)// PM에서 저장된 이미지를 가져와서 프로필사진으로 로드
        if(bitmap != null){
            binding.profileCiv.setImageBitmap(bitmap)
        }
        val dDay = pm.getDDay(this)//PM에서 디데이 로드
        if(dDay != null){
            var dDayMessage =
                if (dDay==0) "D-day"
                else "D-${dDay}"
            binding.dDayBt.setText(dDayMessage)
        }
    }

    private fun autoLogin() {
        mLoadingDialog.show() // 로딩화면 실행
        AccountManager().autoLogin(this){resultMessage->
            if(resultMessage == AccountManager.RESULT_SUCCESS){
                Toast.makeText(this, "로그인", Toast.LENGTH_LONG).show()
                mLoadingDialog.dismiss()//로딩해제
                loadProfile()
            }
            else{
                mLoadingDialog.dismiss()//로딩해제
            }
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

    fun showTwoButtonDialogMessage(title: String, body: String, callBack: (Int) -> Unit) {//다이얼로그 메시지를 띄우는 함수
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(body)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int -> callBack(i)}
        builder.setNegativeButton("취소") { dialogInterface: DialogInterface, i: Int -> callBack(i)}
        builder.show()
    }

    override fun onBackPressed() {
        showTwoButtonDialogMessage("알림", "Mili Meter를 종료하시겠습니까?"){
            when(it){
                -1 -> {
                    finishAffinity()
                    exitProcess(0)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        loadProfile()
    }

    override fun attachBaseContext(newBase: Context?) {
        LocaleWrapper.setLocale(newBase?.let { PreferenceManager().getLanguageData(it).toString() })
        super.attachBaseContext(newBase?.let { LocaleWrapper.wrap(it) })
    }
}



