package com.example.todolist

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.attributes.PrimaryAttributes
import kotlinx.android.synthetic.main.activity_archive.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class ArchiveActivity : AppCompatActivity() {

    lateinit var db: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)

        db = DBHandler(this)

        rvPrimaryArchive.layoutManager = LinearLayoutManager(this)

        refreshList()
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
                refreshList()
            }
            else{
                val sdf = SimpleDateFormat("dd/MM/yyyy  HH:mm:ss", Locale.getDefault())
                obj.title = sdf.format(Date())
                db.updatePrimaryList(obj)
                refreshList()
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
            obj.itemsList = list.itemsList
            obj.id = list.id
            if (n==1){      //Delete
                obj.type = 3
                db.updatePrimaryList(obj)
                Toast.makeText(this, "List Deleted", Toast.LENGTH_SHORT).show()
                //db.deletePrimaryList(list[0].id)
            }
            else if(n==2){  //Archived
                obj.type = 1
                db.updatePrimaryList(obj)
                Toast.makeText(this, "List Unarchived", Toast.LENGTH_SHORT).show()
            }
            else if(n==3){
                db.markItemList(list.id, true)
                Toast.makeText(this, "All Checked", Toast.LENGTH_SHORT).show()
            }
            else if (n==4){
                db.markItemList(list.id, false)
                Toast.makeText(this, "All unchecked", Toast.LENGTH_SHORT).show()
            }
            refreshList()
        }
        dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }

        dialog.show()
    }


    private fun refreshList(){
        val list: MutableList<PrimaryAttributes> = db.getPrimaryList(1, 2)
        if (list.isEmpty())
            archiveNoAvailable.visibility = View.VISIBLE
        else
            archiveNoAvailable.visibility = View.GONE
        rvPrimaryArchive.adapter = ArchiveAdapter(this, list)
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    class ArchiveAdapter(private val activity: ArchiveActivity, private val list: MutableList<PrimaryAttributes>):
        RecyclerView.Adapter<ArchiveAdapter.ViewHolder>(){
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
                intent.putExtra(INTENT_ID, list[position].id)
                intent.putExtra(INTENT_CREATED, false)
                activity.startActivity(intent)
            }

            holder.cardMenu.setOnClickListener {
                val popup = PopupMenu(activity, holder.cardMenu)
                popup.inflate(R.menu.menu_archive)
                popup.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menuArcEditTitle -> {
                            activity.dialogFuncTitle(list[position])
                        }
                        R.id.menuArcDelete ->  {
                            activity.dialogFunc("Delete List", 1, list[position])
                        }
                        R.id.menuArcArchive ->  {
                            activity.dialogFunc("Archive List", 2, list[position])
                        }
                        R.id.menuArcCheckList ->  {
                            activity.dialogFunc("Check all items in the List", 3, list[position])
                        }
                        R.id.menuArcUncheckList -> {
                            activity.dialogFunc("Uncheck all items in the List", 4, list[position])
                        }

                    }
                    true
                }
                popup.show()
            }
        }


    }
}