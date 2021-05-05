package com.example.todolist

import android.content.Context
import android.content.DialogInterface
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.attributes.ItemAttributes
import com.example.todolist.attributes.PrimaryAttributes
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class ListActivity : AppCompatActivity() {

    lateinit var db: DBHandler
    lateinit var changeColorGroup: ChangeColorGroup

    private var intentID: Long = -1
    var titleVar: String = ""
    private var groupVar: Int = 0
    private var typeVar: Int = 1
    private var itemsVar: Int = 0
    private var itemsCheckedVar: Int = 0

    private var isCreated: Boolean = false
    private var inEditMode: Boolean = false

    var globalList: MutableList<ItemAttributes>? = null
    var adapter: ListAdapter? = null

    var touchHelper: ItemTouchHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        db = DBHandler(this)
        changeColorGroup = ChangeColorGroup(this)

        isCreated = intent.getBooleanExtra(INTENT_CREATED, false)
        intentID = intent.getLongExtra(INTENT_ID, -1)

        //val colourGroup: View = findViewById(R.id.listColourGroupLayout)

        rvItem.layoutManager = LinearLayoutManager(this)
        refreshList()


        if (isCreated) {
            editModeFunc()
        } else {
            val list: MutableList<PrimaryAttributes> = db.getPrimaryBasics(intentID)
            titleVar = list[0].title
            groupVar = list[0].group
            typeVar = list[0].type
            itemsVar = list[0].items
            itemsCheckedVar = list[0].itemsChecked

            changeColorGroup.colourGroupChangeFunc(groupVar)
            //colourGroupFunc(groupVar)

            listToolbarTitleET.setText(titleVar)
            listToolbarTitleTV.text = titleVar
        }

        if (typeVar == 1){
            listArchive.visibility = View.VISIBLE
            listUnarchive.visibility = View.GONE
        }
        else{
            listArchive.visibility = View.GONE
            listUnarchive.visibility = View.VISIBLE
        }

        listToolbarEdit.setOnClickListener { editModeFunc() }
        listToolbarDone.setOnClickListener { editModeFunc() }

        listDelete.setOnClickListener {
            dialogFunc("Delete List", 1)
        }

        listArchive.setOnClickListener {
            dialogFunc("Archive List", 2)
        }

        listCheckAll.setOnClickListener {
            dialogFunc("Check all items in the list", 3)
        }

        listUncheckAll.setOnClickListener {
            dialogFunc("Uncheck all items in the list", 4)
        }
        listUnarchive.setOnClickListener {
            dialogFunc("Unarchive List", 5)
        }

        listMore.setOnClickListener {
            if (listMoreLayout.visibility == View.GONE)
                listMoreLayout.visibility = View.VISIBLE
            else
                listMoreLayout.visibility = View.GONE
        }

        /*listColorGroup.setOnClickListener {
            if (listColourGroupLayout.visibility == View.GONE)
                listColourGroupLayout.visibility = View.VISIBLE
            else
                listColourGroupLayout.visibility = View.GONE
        }*/

        touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP
                or ItemTouchHelper.DOWN, 0){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val sourcePos: Int = viewHolder.adapterPosition
                val targetPos: Int = target.adapterPosition
                Collections.swap(globalList, sourcePos, targetPos)
                adapter?.notifyItemMoved(sourcePos, targetPos)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }

        })

        touchHelper?.attachToRecyclerView(rvItem)

    }

    fun colourGroupFunc(v: View){
        when(v.id){
            R.id.layoutColourGroup0 -> {
                changeColorGroup.colourGroupChangeFunc(0)
                groupVar = 0
            }
            R.id.layoutColourGroup1 -> {
                changeColorGroup.colourGroupChangeFunc(1)
                groupVar = 1
            }
            R.id.layoutColourGroup2 -> {
                changeColorGroup.colourGroupChangeFunc(2)
                groupVar = 2
            }
            R.id.layoutColourGroup3 -> {
                changeColorGroup.colourGroupChangeFunc(3)
                groupVar = 3
            }
            R.id.layoutColourGroup4 -> {
                changeColorGroup.colourGroupChangeFunc(4)
                groupVar = 4
            }
            R.id.layoutColourGroup5 -> {
                changeColorGroup.colourGroupChangeFunc(5)
                groupVar = 5
            }
            R.id.layoutColourGroup6 -> {
                changeColorGroup.colourGroupChangeFunc(6)
                groupVar = 6
            }
            R.id.layoutColourGroup7 -> {
                changeColorGroup.colourGroupChangeFunc(7)
                groupVar = 7
            }
            R.id.layoutColourGroup8 -> {
                changeColorGroup.colourGroupChangeFunc(8)
                groupVar = 8
            }

        }
        //listColourGroupLayout.visibility = View.GONE
        listMoreLayout.visibility = View.GONE
        addUpdateFunc()
    }


    private fun dialogFunc(s: String, n: Int){
        val dialog = AlertDialog.Builder(this, R.style.MyDialogTheme)
        dialog.setTitle(s)
        dialog.setPositiveButton("Yes"){ _: DialogInterface, _: Int ->
            if (n==1){
                if (!isCreated)
                {
                    typeVar = 3
                    addUpdateFunc()
                }
                    //db.deletePrimaryList(intentID)
                this.finish()
            }
            else if(n==2){
                typeVar = 2
                addUpdateFunc()
                listMoreLayout.visibility = View.GONE
                Toast.makeText(this, "List Archived", Toast.LENGTH_SHORT).show()
                listArchive.visibility = View.GONE
                listUnarchive.visibility = View.VISIBLE
            }
            else if(n==3){
                db.markItemList(intentID, true)
                listMoreLayout.visibility = View.GONE
                refreshList()
                itemsCheckedVar = itemsVar
                itemsChecked()
                addUpdateFunc()
                Toast.makeText(this, "All Checked", Toast.LENGTH_SHORT).show()

            }
            else if (n==4){
                db.markItemList(intentID, false)
                listMoreLayout.visibility = View.GONE
                refreshList()
                itemsCheckedVar = 0
                itemsChecked()
                addUpdateFunc()
                Toast.makeText(this, "All unchecked", Toast.LENGTH_SHORT).show()

            }
            else{
                typeVar = 1
                addUpdateFunc()
                listMoreLayout.visibility = View.GONE
                Toast.makeText(this, "List Unarchived", Toast.LENGTH_SHORT).show()
                listArchive.visibility = View.VISIBLE
                listUnarchive.visibility = View.GONE
            }
        }
        dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }

        dialog.show()
    }

    private fun editModeFunc() {
        if (inEditMode) {
            listToolbarTitleTV.visibility = View.VISIBLE
            listToolbarEdit.visibility = View.VISIBLE

            listToolbarTitleET.visibility = View.GONE
            listToolbarDone.visibility = View.GONE

            addUpdateFunc()
        } else {
            listToolbarTitleTV.visibility = View.GONE
            listToolbarEdit.visibility = View.GONE

            listToolbarTitleET.visibility = View.VISIBLE
            listToolbarDone.visibility = View.VISIBLE
        }
        inEditMode = !inEditMode
        refreshList()
    }

    private fun addUpdateFunc() {

        val obj = PrimaryAttributes()
        obj.group = groupVar
        obj.type = typeVar
        obj.items = itemsVar
        obj.itemsChecked = itemsCheckedVar

        titleVar = listToolbarTitleET.text.toString()
        listToolbarTitleTV.text = titleVar

        //When a new list is created and not yet saved in the database
        if (isCreated) {

            if (titleVar.trim().isNotEmpty() or (itemsVar > 0)) {
                if (titleVar.trim().isEmpty()) {
                    //val currentTime: Date = Calendar.getInstance().time

                    val sdf = SimpleDateFormat("dd/MM/yyyy  HH:mm:ss", Locale.getDefault())
                    titleVar = sdf.format(Date())
                    //titleVar = currentTime.toString()
                }

                obj.title = titleVar
                obj.group = groupVar
                obj.type = typeVar
                obj.items = itemsVar
                obj.itemsChecked = itemsCheckedVar

                intentID = db.addPrimaryList(obj)

                Toast.makeText(this, "List Created, ID:$intentID", Toast.LENGTH_SHORT).show()

                isCreated = false
            }


        }/*Updating the list*/
        else {

            if (titleVar.trim().isEmpty()) {
                //val currentTime: Date = Calendar.getInstance().time

                val sdf = SimpleDateFormat("dd/MM/yyyy  HH:mm:ss", Locale.getDefault())
                titleVar = sdf.format(Date())
                //titleVar = currentTime.toString()
            }

            /*if (titleVar.trim().isEmpty()){
                val currentTime: Date = Calendar.getInstance().time
                titleVar = currentTime.toString()
            }*/
            obj.id = intentID
            obj.title = titleVar
            obj.group = groupVar
            obj.type = typeVar
            obj.items = itemsVar
            obj.itemsChecked = itemsCheckedVar

            db.updatePrimaryList(obj)
        }

        listToolbarTitleTV.text = titleVar

    }

    private fun addItemFunc() {
        val dialog = AlertDialog.Builder(this, R.style.MyDialogTheme)
        val view = layoutInflater.inflate(R.layout.dialog_add, null)
        val dialogFieldText = view.findViewById<TextView>(R.id.dialogAddText)

        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        dialog.setView(view)
        dialog.setTitle("Add Item")
        dialog.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            if (dialogFieldText.text.isNotEmpty()) {
                val obj = ItemAttributes()
                obj.name = dialogFieldText.text.toString()
                obj.isCompleted = false
                itemsVar += 1
                addUpdateFunc()
                obj.primaryId = intentID
                db.addItem(obj)

                refreshList()

                //Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
            }
            imm.hideSoftInputFromWindow(dialogFieldText.windowToken, 0)
        }
        dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->
            /*val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(dialogFieldText.windowToken, 0)*/
            imm.hideSoftInputFromWindow(dialogFieldText.windowToken, 0)
        }
        dialog.setNeutralButton("Next") { _: DialogInterface, _: Int ->
            if (dialogFieldText.text.isNotEmpty()) {
                val obj = ItemAttributes()
                obj.name = dialogFieldText.text.toString()
                obj.isCompleted = false
                itemsVar += 1
                addUpdateFunc()
                obj.primaryId = intentID
                db.addItem(obj)

                refreshList()
                //Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
                imm.hideSoftInputFromWindow(dialogFieldText.windowToken, 0)
                addItemFunc()
            }
        }
        //window.setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show()
        dialogFieldText.requestFocus()
        /*val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)*/
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun updateItemFunc(obj: ItemAttributes) {
        val dialog = AlertDialog.Builder(this, R.style.MyDialogTheme)
        val view = layoutInflater.inflate(R.layout.dialog_add, null)
        val dialogFieldText = view.findViewById<TextView>(R.id.dialogAddText)
        dialog.setView(view)
        dialog.setTitle("Add Item")
        dialog.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            if (dialogFieldText.text.isNotEmpty()) {
                obj.name = dialogFieldText.text.toString()
                //obj.isCompleted = false
                //itemsVar += 1
                db.updateItem(obj)
                //obj.primaryId = intentID
                //db.addItem(obj)

                refreshList()

                //Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
            }

        }
        dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(dialogFieldText.windowToken, 0)
        }
        dialog.show()
        dialogFieldText.text = obj.name
        dialogFieldText.requestFocus()
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    override fun onBackPressed() {
        when {
            listMoreLayout.visibility == View.VISIBLE -> listMoreLayout.visibility = View.GONE
            inEditMode -> editModeFunc()
            else -> finish()
        }
    }

    private fun itemsChecked(){
        listToolbarBotItems.text = ("$itemsCheckedVar / $itemsVar")
    }

    private fun refreshList() {
        globalList = db.getItem(intentID)
        adapter = ListAdapter(this, globalList!!)
        rvItem.adapter = adapter
        itemsChecked()

    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    class ListAdapter(
        private val activity: ListActivity,
        private val list: MutableList<ItemAttributes>
    ) :
        RecyclerView.Adapter<ListAdapter.ViewHolder>() {
        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

            val cardTitle = v.findViewById<TextView>(R.id.cardItemTitle)
            val cardSwap = v.findViewById<ImageView>(R.id.cardItemSwap)
            //val cardMenu = v.findViewById<ImageView>(R.id.cardItemMenu)
            val cardDelete = v.findViewById<ImageView>(R.id.cardItemDelete)
            val addItemBtn = v.findViewById<LinearLayout>(R.id.addItemButton)
            var cardCheckBox = v.findViewById<CheckBox>(R.id.cardItemCheckBox)

        }

        override fun getItemViewType(position: Int): Int {
            return if (position == list.size)
                R.layout.button_add_item
            else
                R.layout.card_item
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val viewItem: View = when (viewType) {
                R.layout.card_item -> LayoutInflater.from(activity).inflate(
                    R.layout.card_item,
                    parent,
                    false
                )
                else -> LayoutInflater.from(activity).inflate(
                    R.layout.button_add_item,
                    parent,
                    false
                )
            }
            return ViewHolder(viewItem)
        }

        override fun getItemCount(): Int {
            return list.size + 1
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            fun strikeThroughFunc() {
                if (list[position].isCompleted) {
                    holder.cardTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    holder.cardCheckBox.isChecked = true
                } else {
                    holder.cardTitle.paintFlags = 0
                    holder.cardCheckBox.isChecked = false
                }

            }

            /*For button in recyclerview*/
            if (position == list.size) {
                if (activity.inEditMode) {
                    holder.addItemBtn.visibility = View.VISIBLE
                    holder.addItemBtn.setOnClickListener {
                        activity.addItemFunc()
                    }
                } else {
                    holder.addItemBtn.visibility = View.GONE
                }
            }

            /*Item card holders*/
            else {
                holder.cardTitle.text = list[position].name

                /*Painting STRIKE THROUGH if completed*/
                strikeThroughFunc()

                /*Actions to perform in EDIT Mode*/
                if (activity.inEditMode) {
                    holder.cardSwap.visibility = View.VISIBLE
                    holder.cardDelete.visibility = View.VISIBLE
                    holder.cardCheckBox.visibility = View.GONE
                    //holder.cardMenu.visibility = View.GONE

                    holder.cardTitle.setOnClickListener {
                        activity.updateItemFunc(list[position])
                    }


                    holder.cardSwap.setOnTouchListener { v, event ->
                        if (event.actionMasked == MotionEvent.ACTION_DOWN){
                            activity.touchHelper?.startDrag(holder)
                        }
                        false
                    }

                }
                /*Actions to perform in VIEW Mode*/
                else {
                    holder.cardSwap.visibility = View.GONE
                    holder.cardDelete.visibility = View.GONE
                    holder.cardCheckBox.visibility = View.VISIBLE
                    //holder.cardMenu.visibility = View.VISIBLE

                    holder.cardTitle.setOnClickListener {
                        list[position].isCompleted = !list[position].isCompleted
                        activity.db.updateItem(list[position])

                        if (list[position].isCompleted) activity.itemsCheckedVar += 1 else activity.itemsCheckedVar -= 1

                        strikeThroughFunc()
                        activity.itemsChecked()
                    }
                    holder.cardCheckBox.setOnClickListener {
                        list[position].isCompleted = !list[position].isCompleted
                        activity.db.updateItem(list[position])

                        if (list[position].isCompleted) activity.itemsCheckedVar += 1 else activity.itemsCheckedVar -= 1

                        strikeThroughFunc()
                        activity.itemsChecked()
                    }
                }

                holder.cardDelete.setOnClickListener {
                    activity.db.deleteItem(list[position].id)
                    activity.itemsVar -= 1
                    activity.refreshList()
                }


            }
        }
    }
}