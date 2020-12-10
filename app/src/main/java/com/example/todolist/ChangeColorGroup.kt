package com.example.todolist

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_list.*

class ChangeColorGroup(private val activity: ListActivity) {

    fun colourGroupChangeFunc(n: Int){
        when(n){
            0 -> {
                activity.listToolbar.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroupDeep0))
                activity.rvItem.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup0))
                activity.listToolbarBot.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroupDeep0))
                activity.listMoreLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup0))
            }
            1 -> {
                activity.listToolbar.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroupDeep1))
                activity.rvItem.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup1))
                activity.listToolbarBot.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroupDeep1))
                activity.listMoreLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup1))
            }
            2 -> {
                activity.listToolbar.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroupDeep2))
                activity.rvItem.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup2))
                activity.listToolbarBot.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroupDeep2))
                activity.listMoreLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup2))
            }
            3 -> {
                activity.listToolbar.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroupDeep3))
                activity.rvItem.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup3))
                activity.listToolbarBot.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroupDeep3))
                activity.listMoreLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup3))
                            }
            4 -> {
                activity.listToolbar.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroupDeep4))
                activity.rvItem.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup4))
                activity.listToolbarBot.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroupDeep4))
                activity.listMoreLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup4))
            }
            5 -> {
                activity.listToolbar.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroupDeep5))
                activity.rvItem.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup5))
                activity.listToolbarBot.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroupDeep5))
                activity.listMoreLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup5))
            }
            6 -> {
                activity.listToolbar.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroupDeep6))
                activity.rvItem.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup6))
                activity.listToolbarBot.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroupDeep6))
                activity.listMoreLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup6))
            }
            7 -> {
                activity.listToolbar.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroupDeep7))
                activity.rvItem.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup7))
                activity.listToolbarBot.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroupDeep7))
                activity.listMoreLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup7))
            }
            8 -> {
                activity.listToolbar.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroupDeep8))
                activity.rvItem.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup8))
                activity.listToolbarBot.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroupDeep8))
                activity.listMoreLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup8))
                            }
        }
    }
}