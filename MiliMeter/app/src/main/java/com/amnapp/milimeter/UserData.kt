package com.amnapp.milimeter

data class UserData(var id: String? = null){
    var pw: String? = null
    var childCount: Int = 0
    var confirmHashCode: String? = null
    var inviteHashCode: String? = null
    var indexHashCode: String? = null
    var isLogined: Boolean = false
    var isAdmin: Boolean = false
    //아래는 프로필 정보
    var userName: String? = null
    var userAge: Int? = null
    var militaryId: Int? = null // 입력받을 때 군번에서 '-'없이 입력받을 것
    var userHeight: Int? = null
    var userWeight: Int? = null
    var userBloodType: String? = null
    //목표 정보
    var goalOfWeight: Int? = null
    var goalOfTotalRank: String? = null
    var goalOfLegTuckRank: String? = null
    var goalOfShuttleRunRank: String? = null
    var goalOfFieldTrainingRank: String? = null

    companion object{
        private var mUserData: UserData? = null
        var mTmpUserData: UserData? = null

        fun setInstance(userData: UserData){
            mUserData = userData
        }
        fun getInstance(): UserData{
            return mUserData ?: UserData().also{
                mUserData = it
            }
        }
    }
}
