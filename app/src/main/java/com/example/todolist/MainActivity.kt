package com.example.todolist

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.attributes.ItemAttributes
import com.example.todolist.attributes.PrimaryAttributes
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.drawer_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

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

    private fun dialogFuncTitle(obj: PrimaryAttributes) {
        val dialog = AlertDialog.Builder(this, R.style.MyDialogThemeTitle)
        val view = layoutInflater.inflate(R.layout.dialog_add, null)
        val dialogFieldText = view.findViewById<TextView>(R.id.dialogAddText)
        dialog.setView(view)
        dialog.setTitle("Title")
        dialog.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            if (dialogFieldText.text.trim().isNotEmpty()) {
                obj.title = dialogFieldText.text.toString()
                db.updatePrimaryList(obj)
                refreshList(sortByVar)
            }
            else{
                val sdf = SimpleDateFormat("dd/MM/yyyy  HH:mm:ss", Locale.getDefault())
                obj.title = sdf.format(Date())
                db.updatePrimaryList(obj)
                refreshList(sortByVar)
            }

        }
        dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(dialogFieldText.windowToken, 0)
        }
        dialog.show()
        dialogFieldText.text = obj.title
        dialogFieldText.requestFocus()
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun dialogFunc(s: String, n: Int, list: PrimaryAttributes){
        val dialog = AlertDialog.Builder(this, R.style.MyDialogThemeTitle)
        dialog.setTitle(s)
        dialog.setPositiveButton("Yes"){ _: DialogInterface, _: Int ->
            val obj = PrimaryAttributes()
            obj.title = list.title
            obj.type = list.type
            obj.group = list.group
            obj.itemsChecked = list.itemsChecked
            obj.items = list.items
            obj.itemsList = list.itemsList
            obj.id = list.id
            if (n==1){      //Delete
                obj.type = 3
                db.updatePrimaryList(obj)
                Toast.makeText(this, "List Deleted", Toast.LENGTH_SHORT).show()
                //db.deletePrimaryList(list[0].id)
            }
            else if(n==2){  //Archived
                obj.type = 2
                db.updatePrimaryList(obj)
                Toast.makeText(this, "List Archived", Toast.LENGTH_SHORT).show()
            }
            else if(n==3){
                obj.itemsChecked = list.items
                db.updatePrimaryList(obj)
                db.markItemList(list.id, true)
                Toast.makeText(this, "All Checked", Toast.LENGTH_SHORT).show()
            }
            else if (n==4){
                obj.itemsChecked = 0
                db.updatePrimaryList(obj)
                db.markItemList(list.id, false)
                Toast.makeText(this, "All unchecked", Toast.LENGTH_SHORT).show()
            }
            refreshList(sortByVar)
        }
        dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }

        dialog.show()
    }

    private fun refreshList(n: Int){
        rvPrimary.adapter = MainAdapter(this, db.getPrimaryList(n, 1))
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
            //val cardType = v.findViewById<TextView>(R.id.cardPrimaryType)

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

            //holder.cardType.text = list[position].type.toString()

            holder.cardTitle.text = list[position].title
            holder.cardTitle.setOnClickListener {
                val intent = Intent(activity, ListActivity::class.java)
//                val opt: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *arrayOf(holder.cardTitle))
                val opt = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, holder.cardTitle,
                    ViewCompat.getTransitionName(holder.cardTitle).toString()
                )
                intent.putExtra(INTENT_ID, list[position].id)
                intent.putExtra(INTENT_CREATED, false)
                activity.startActivity(intent, opt.toBundle())
            }

            holder.cardMenu.setOnClickListener {
                val popup = PopupMenu(activity, holder.cardMenu)
                popup.inflate(R.menu.menu_primary_card)
                popup.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menuEditTitle -> {
                            activity.dialogFuncTitle(list[position])
                        }
                        R.id.menuDelete ->  {
                            activity.dialogFunc("Delete List", 1, list[position])
                            activity.refreshList(activity.sortByVar)
                        }
                        R.id.menuArchive ->  {
                            activity.dialogFunc("Archive List", 2, list[position])
                        }
                        R.id.menuCheckList ->  {
                            activity.dialogFunc("Check all items in List", 3, list[position])
                        }
                        R.id.menuUncheckList -> {
                            activity.dialogFunc("Uncheck all items in List", 4, list[position])
                        }

                    }
                    true
                }
                popup.show()
            }
        }


    }
}