package com.example.todolist

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.attributes.PrimaryAttributes
import kotlinx.android.synthetic.main.activity_archive.*
import kotlinx.android.synthetic.main.activity_main.*

class ArchiveActivity : AppCompatActivity() {

    lateinit var db: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)

        db = DBHandler(this)

        rvPrimaryArchive.layoutManager = LinearLayoutManager(this)

        refreshList()
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
                popup.inflate(R.menu.menu_primary_card)
                popup.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menuEditTitle -> {

                        }
                        R.id.menuDelete ->  {
                            activity.db.deletePrimaryList(list[position].id)
                            activity.refreshList()
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