package com.amnapp.milimeter.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.amnapp.milimeter.PreferenceManager
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivityLoginBinding
import com.amnapp.milimeter.databinding.ActivityThemeExampleBinding

class ThemeExampleActivity : AppCompatActivity() {
    var mTheme: String? = null
    lateinit var binding: ActivityThemeExampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadTheme()
        binding = ActivityThemeExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.changeThemeBt.setOnClickListener {
            val pm = PreferenceManager()
            if(mTheme == PreferenceManager.THEME_DARK){
                pm.setThemeData(this, PreferenceManager.THEME_DEFAULT)
            }
            else{
                pm.setThemeData(this, PreferenceManager.THEME_DARK)
            }
            Toast.makeText(this, pm.getThemeData(this), Toast.LENGTH_SHORT).show()

            val intent = Intent(this, ThemeExampleActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.changeBottomMenuCb.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked){
                true->{
                    binding.menuBottomLl.visibility = View.VISIBLE
                    binding.bottomLl.visibility = View.GONE
                }
                false->{
                    binding.menuBottomLl.visibility = View.GONE
                    binding.bottomLl.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun loadTheme() {
        val pm = PreferenceManager()
        mTheme = pm.getThemeData(this)
        if(mTheme == PreferenceManager.THEME_DARK){
            setTheme(R.style.DarkAppTheme)
        }
        else{
            setTheme(R.style.AppTheme)
        }
    }
}