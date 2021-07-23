package com.amnapp.milimeter

import com.github.mikephil.charting.charts.LineChart
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChartManager {
    interface UICallBack{
        fun whatToDo()
    }

    fun makeLineChart(lineChart: LineChart, userData: UserData, date: String){

    }

    fun updateTrainingRecord(userData: UserData, date: String, record: HashMap<String, String>, callBack: UICallBack){
        userData.indexHashCode?.let { indexHashCode ->
            Firebase.firestore.collection("users")
                .document(indexHashCode)
                .collection("training records")
                .document(date)
                .set(record, SetOptions.merge())
                .addOnSuccessListener {
                    callBack.whatToDo()
                }
        }
    }
}