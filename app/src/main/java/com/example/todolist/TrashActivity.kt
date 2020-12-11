package com.example.todolist

import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.attributes.PrimaryAttributes
import kotlinx.android.synthetic.main.activity_archive.*
import kotlinx.android.synthetic.main.activity_trash.*

class TrashActivity : AppCompatActivity() {

    lateinit var db: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trash)

        db = DBHandler(this)

        rvPrimaryDelete.layoutManager = LinearLayoutManager(this)

        refreshList()
    }

    private fun dialogFunc(s: String, n: Int, list: PrimaryAttributes){
        val dialog = AlertDialog.Builder(this, R.style.MyDialogTheme)
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
                obj.type = 1
                db.updatePrimaryList(obj)
                Toast.makeText(this, "List Retrived", Toast.LENGTH_SHORT).show()
                //db.deletePrimaryList(list[0].id)
            }
            else if(n==2){  //Archived
                obj.type = 2
                db.deletePrimaryList(list.id)
                Toast.makeText(this, "List Deleted from Trash", Toast.LENGTH_SHORT).show()
            }

            refreshList()
        }
        dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }

        dialog.show()
    }

    private fun refreshList(){
        val list: MutableList<PrimaryAttributes> = db.getPrimaryList(1, 3)
        if (list.isEmpty())
            deleteNoAvailable.visibility = View.VISIBLE
        else
            deleteNoAvailable.visibility = View.GONE
        rvPrimaryDelete.adapter = DeleteAdapter(this, list)
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    class DeleteAdapter(private val activity: TrashActivity, private val list: MutableList<PrimaryAttributes>):
        RecyclerView.Adapter<DeleteAdapter.ViewHolder>(){
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
            /*holder.cardTitle.setOnClickListener {
                val intent = Intent(activity, ListActivity::class.java)
                intent.putExtra(INTENT_ID, list[position].id)
                intent.putExtra(INTENT_CREATED, false)
                activity.startActivity(intent)
            }*/

            holder.cardMenu.setOnClickListener {
                val popup = PopupMenu(activity, holder.cardMenu)
                popup.inflate(R.menu.menu_trsh)
                popup.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menuDeletePermanently -> {
                            activity.dialogFunc("Remove Permanently",2, list[position])
                        }
                        R.id.menuRetrive ->  {
                            activity.dialogFunc("Retrieve List",1, list[position])
                        }
                    }
                    true
                }
                popup.show()
            }
        }


    }
}