package com.example.todolist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.todolist.attributes.ItemAttributes
import com.example.todolist.attributes.PrimaryAttributes

class DBHandler(private val context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val createPrimaryTable = "CREATE TABLE $TABLE_PRIMARY (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_TITLE VARCHAR, " +
                "$COL_GROUP INTEGER, " +
                "$COL_TYPE INTEGER, "+
                "$COL_ITEMS INTEGER, " +
                "$COL_ITEMS_CHECKED INTEGER, " +
                "$COL_CREATED_AT datetime DEFAULT CURRENT_TIMESTAMP, " +
                "$COL_MODIFIED_ON datetime DEFAULT CURRENT_TIMESTAMP);"

        val createItemTable = "CREATE TABLE $TABLE_ITEM (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_ITEM_NAME VARCHAR, " +
                "$COL_PRIMARY_ID INTEGER, " +
                "$COL_IS_COMPLETED INTEGER, " +
                "$COL_CREATED_AT datetime DEFAULT CURRENT_TIMESTAMP, " +
                "$COL_MODIFIED_ON datetime DEFAULT CURRENT_TIMESTAMP);"

        db.execSQL(createPrimaryTable)
        db.execSQL(createItemTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    /*Primary List*/
    fun addPrimaryList(obj: PrimaryAttributes): Long {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_TITLE, obj.title)
        cv.put(COL_GROUP, obj.group)
        cv.put(COL_TYPE, obj.type)
        cv.put(COL_ITEMS, obj.items)
        cv.put(COL_ITEMS_CHECKED, obj.itemsChecked)

        return db.insert(TABLE_PRIMARY, null, cv)
    }

    fun getPrimaryList(n: Int, t: Int): MutableList<PrimaryAttributes>{
        val result: MutableList<PrimaryAttributes> = ArrayList()
        val db = readableDatabase
        var queryResult = db.rawQuery("SELECT * FROM $TABLE_PRIMARY WHERE $COL_TYPE = $t", null)

        when(n){
            1-> queryResult = db.rawQuery("SELECT * FROM $TABLE_PRIMARY WHERE $COL_TYPE = $t", null)
            2-> queryResult = db.rawQuery("SELECT * FROM $TABLE_PRIMARY WHERE $COL_TYPE=$t ORDER BY $COL_TITLE ASC", null)
            3-> queryResult = db.rawQuery("SELECT * FROM $TABLE_PRIMARY WHERE $COL_TYPE = $t ORDER BY $COL_TITLE DESC", null)
            4-> queryResult = db.rawQuery("SELECT * FROM $TABLE_PRIMARY WHERE $COL_TYPE = $t ORDER BY $COL_GROUP ASC", null)
        }



        if (queryResult.moveToFirst()){
            do {
                val obj = PrimaryAttributes()
                obj.id  = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                obj.title = queryResult.getString(queryResult.getColumnIndex(COL_TITLE))
                obj.group = queryResult.getInt(queryResult.getColumnIndex(COL_GROUP))
                obj.items = queryResult.getInt(queryResult.getColumnIndex(COL_ITEMS))
                obj.itemsChecked = queryResult.getInt(queryResult.getColumnIndex(COL_ITEMS_CHECKED))
                result.add(obj)

            }while (queryResult.moveToNext())
        }
        queryResult.close()
        return result
    }

    fun updatePrimaryList(obj: PrimaryAttributes){
        val db  = writableDatabase
        val cv = ContentValues()
        cv.put(COL_TITLE, obj.title)
        cv.put(COL_GROUP, obj.group)
        cv.put(COL_ITEMS, obj.items)
        cv.put(COL_TYPE, obj.type)
        cv.put(COL_ITEMS_CHECKED, obj.itemsChecked)

        db.update(TABLE_PRIMARY, cv, "$COL_ID=?", arrayOf(obj.id.toString()))
    }

    fun deletePrimaryList(n: Long){
        val db = writableDatabase
        db.delete(TABLE_ITEM, "$COL_PRIMARY_ID=?", arrayOf(n.toString()))
        db.delete(TABLE_PRIMARY, "$COL_ID=?", arrayOf(n.toString()))
    }

    fun getPrimaryBasics(n: Long): MutableList<PrimaryAttributes>{
        val result: MutableList<PrimaryAttributes> = ArrayList()
        val db = readableDatabase

        val queryResult = db.rawQuery("SELECT * FROM $TABLE_PRIMARY WHERE $COL_ID = $n", null)

        queryResult.moveToFirst()

        val obj = PrimaryAttributes()
        obj.id  = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
        obj.title = queryResult.getString(queryResult.getColumnIndex(COL_TITLE))
        obj.group = queryResult.getInt(queryResult.getColumnIndex(COL_GROUP))
        obj.type = queryResult.getInt(queryResult.getColumnIndex(COL_TYPE))
        obj.items = queryResult.getInt(queryResult.getColumnIndex(COL_ITEMS))
        obj.itemsChecked = queryResult.getInt(queryResult.getColumnIndex(COL_ITEMS_CHECKED))
        result.add(obj)

        queryResult.close()
        return result
    }

    fun markItemList(n: Long, flag: Boolean){
        val db = writableDatabase
        val qResult = db.rawQuery("SELECT * FROM $TABLE_ITEM WHERE $COL_PRIMARY_ID = $n", null)

        if (qResult.moveToFirst()){
            do {
                val obj = ItemAttributes()
                obj.id  =qResult.getLong(qResult.getColumnIndex(COL_ID))
                obj.name = qResult.getString(qResult.getColumnIndex(COL_ITEM_NAME))
                obj.isCompleted = flag
                updateItem(obj)
            }while (qResult.moveToNext())
        }
        qResult.close()
    }

    /*Items*/
    fun addItem(obj: ItemAttributes): Long {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_ITEM_NAME, obj.name)
        cv.put(COL_PRIMARY_ID, obj.primaryId)
        cv.put(COL_IS_COMPLETED, obj.isCompleted)

        return db.insert(TABLE_ITEM, null, cv)
    }

    fun getItem(n: Long): MutableList<ItemAttributes>{
        val result: MutableList<ItemAttributes> = ArrayList()
        val db = readableDatabase

        val qResult = db.rawQuery("SELECT * FROM $TABLE_ITEM WHERE $COL_PRIMARY_ID = $n", null)

        if (qResult.moveToFirst()){
            do {
                val obj = ItemAttributes()
                obj.id  =qResult.getLong(qResult.getColumnIndex(COL_ID))
                obj.name = qResult.getString(qResult.getColumnIndex(COL_ITEM_NAME))
                obj.isCompleted = qResult.getInt(qResult.getColumnIndex(COL_IS_COMPLETED)) == 1
                result.add(obj)
            }while (qResult.moveToNext())
        }
        qResult.close()
        return result
    }

    fun updateItem(obj: ItemAttributes){
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_ITEM_NAME, obj.name)
        cv.put(COL_IS_COMPLETED, obj.isCompleted)

        db.update(TABLE_ITEM, cv, "$COL_ID=?", arrayOf(obj.id.toString()))
    }

    fun deleteItem(n: Long){
        val db = writableDatabase
        db.delete(TABLE_ITEM, "$COL_ID=?", arrayOf(n.toString()))
    }
}