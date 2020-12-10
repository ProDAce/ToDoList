package com.example.todolist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.attributes.PrimaryAttributes
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.drawer_layout.view.*

class MainActivity : AppCompatActivity() {
    

    //changeingfxg


    //lateinit var toggle: ActionBarDrawerToggle
    lateinit var db: DBHandler
    lateinit var drawerLayout: DrawerLayout
    var sortByVar = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appSettingsPref: SharedPreferences = getSharedPreferences("AppSettingsPref", 0)
        val sharedPrefEdit: SharedPreferences.Editor = appSettingsPref.edit()
        val isNightMode: Boolean = appSettingsPref.getBoolean("NightMode", false)
        val sortBy: Int = appSettingsPref.getInt("SortBy", 1)

        db = DBHandler(this)

        //NIGHT MODE
        if (isNightMode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        /*mainDayNight.setOnClickListener {
            if (isNightMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefEdit.putBoolean("NightMode", false)
                sharedPrefEdit.apply()
            }

            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefEdit.putBoolean("NightMode", true)
                sharedPrefEdit.apply()
            }

        }*/

        sortByFun(sortBy)


        //drawerLayout = findViewById(R.id.mainActivityDrawerLayout)
        drawerLayout = findViewById(R.id.mainActivityDrawerLayout)
        //val navView: NavigationView = findViewById(R.id.navView)


        rvPrimary.layoutManager = LinearLayoutManager(this)
        refreshList(sortBy)

        mainNavToggle.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }


        //Primary list add button
        mainAdd.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            intent.putExtra(INTENT_ID, -1)
            intent.putExtra(INTENT_CREATED, true)
            startActivity(intent)
        }



        mainCreatedAsc.setOnClickListener {
            sharedPrefEdit.putInt("SortBy", 1)
            sharedPrefEdit.apply()
            sortByVar = 1
            sortByFun(1)
        }

        mainAlphaAsc.setOnClickListener{
            sharedPrefEdit.putInt("SortBy", 2)
            sharedPrefEdit.apply()
            sortByVar = 2
            sortByFun(2)
        }

        mainAlphaDec.setOnClickListener {
            sharedPrefEdit.putInt("SortBy", 3)
            sharedPrefEdit.apply()
            sortByVar = 3
            sortByFun(3)
        }


        mainColorAsc.setOnClickListener {
            sharedPrefEdit.putInt("SortBy", 4)
            sharedPrefEdit.apply()
            sortByVar = 4
            sortByFun(4)
        }
    }

    fun navHome(view: View){
        drawerLayout.closeDrawer(GravityCompat.START)
        /*val intent: Intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)*/
    }

    fun navTrash(view: View){
        drawerLayout.closeDrawer(GravityCompat.START)
        val intent: Intent = Intent(this, TrashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun navArchive(view: View){
        drawerLayout.closeDrawer(GravityCompat.START)
        val intent: Intent = Intent(this, ArchiveActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun navSettings(view: View){
        drawerLayout.closeDrawer(GravityCompat.START)
        val intent: Intent = Intent(this, SettingsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun navHelp(view: View){
        drawerLayout.closeDrawer(GravityCompat.START)
        val intent: Intent = Intent(this, HelpActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun sortByFun(n: Int){
        mainCreatedAsc.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary))
        mainAlphaAsc.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary))
        mainAlphaDec.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary))
        mainColorAsc.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary))

        when(n){
            1-> mainCreatedAsc.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorSeparation))
            2-> mainAlphaAsc.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorSeparation))
            3-> mainAlphaDec.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorSeparation))
            4-> mainColorAsc.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorSeparation))
        }
        refreshList(n)
    }

    private fun refreshList(n: Int){
        rvPrimary.adapter = MainAdapter(this, db.getPrimaryList(n))
    }

    override fun onResume() {
        super.onResume()
        refreshList(sortByVar)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        else
            super.onBackPressed()
    }

/*    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuHome -> Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
            R.id.menuTrash -> Toast.makeText(this, "Trash", Toast.LENGTH_SHORT).show()
            R.id.menuArchive -> Toast.makeText(this, "Archive", Toast.LENGTH_SHORT).show()
        }
        return true
        //return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }*/

    class MainAdapter(private val activity: MainActivity, private val list: MutableList<PrimaryAttributes>):
        RecyclerView.Adapter<MainAdapter.ViewHolder>(){
        class ViewHolder(v: View): RecyclerView.ViewHolder(v){

            val cardTitle = v.findViewById<TextView>(R.id.cardPrimaryTitle)
            val cardMenu: ImageView = v.findViewById(R.id.cardPrimaryMenu)
            val cardLayout = v.findViewById<LinearLayout>(R.id.cardPrimaryLayout)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.card_primary_list, parent, false))
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            when(list[position].group){
                0 -> holder.cardLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup0))
                1 -> holder.cardLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup1))
                2 -> holder.cardLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup2))
                3 -> holder.cardLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup3))
                4 -> holder.cardLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup4))
                5 -> holder.cardLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup5))
                6 -> holder.cardLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup6))
                7 -> holder.cardLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup7))
                8 -> holder.cardLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorGroup8))
            }

            holder.cardTitle.text = list[position].title
            holder.cardTitle.setOnClickListener {
                val intent = Intent(activity, ListActivity::class.java)
                intent.putExtra(INTENT_ID, list[position].id)
                intent.putExtra(INTENT_CREATED, false)
                activity.startActivity(intent)
            }

            holder.cardMenu.setOnClickListener {
                val popup = PopupMenu(activity, holder.cardMenu)
                popup.inflate(R.menu.menu_primary_card)
                popup.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menuEditTitle -> {

                        }
                        R.id.menuDelete ->  {
                            activity.db.deletePrimaryList(list[position].id)
                        }
                        R.id.menuArchive ->  {

                        }
                        R.id.menuCheckList ->  {
                            activity.db.markItemList(list[position].id, true)
                        }
                        R.id.menuUncheckList -> {
                            activity.db.markItemList(list[position].id, false)
                        }

                    }
                    true
                }
                popup.show()
            }
        }


    }
}