package com.example.shruthisports.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.shruthisports.R
import com.example.shruthisports.classes.Sports

class SportsAdapter(val mCtx: Context, val layoutResId: Int, val sportsList:MutableList<Sports>)
    : ArrayAdapter<Sports>(mCtx,layoutResId,sportsList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View =layoutInflater.inflate(layoutResId,null)
        val sportsName=view.findViewById<TextView>(R.id.sportName)
        val sportsCategory=view.findViewById<TextView>(R.id.sportCategory)
        val sportItem = sportsList[position]

        sportsName.text=sportItem.sport_name.toString()
        sportsName.isSingleLine=true
        sportsCategory.text="Category: "+sportItem.sport_category
        return view;
    }

}