package com.amnapp.milimeter

object DataUtil {
    fun getData(): List<Soldier> {
       return listOf(
           Soldier(9,82,200),
           Soldier(9,85,198),
           Soldier(11,78,190),
           Soldier(11,70,193),
           Soldier(11,73,170),
           Soldier(13,74,180)
        )
    }
        //소대 가상데이터
    fun getPlatoon(): List<Soldier>{
        return listOf(
            Soldier(9,82,200),
            Soldier(9,85,198),
            Soldier(11,78,190),
            Soldier(11,70,193),
            Soldier(11,73,170),
            Soldier(13,74,180),
            Soldier(9,82,200),
            Soldier(21,62,135),
            Soldier(18,78,290),
            Soldier(14,80,180),
            Soldier(15,76,172),
            Soldier(19,80,183),
            Soldier(13,74,180)//사용자 더미 데이터
        )
    }
}
