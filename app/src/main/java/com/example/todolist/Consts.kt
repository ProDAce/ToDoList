package com.example.todolist

//Database
const val DB_NAME = "DB_PocketTools"
const val DB_VERSION = 1

const val COL_ID = "Id"
const val COL_CREATED_AT = "CreatedAt"
const val COL_MODIFIED_ON = "ModifiedOn"

//Primary Table
const val TABLE_PRIMARY = "TablePrimary"
const val COL_TITLE = "Title"
const val COL_GROUP = "InGroup"
const val COL_ITEMS="Items"
const val COL_ITEMS_CHECKED="ItemsChecked"


//Item Table
const val TABLE_ITEM = "TableItem"
const val COL_ITEM_NAME = "ItemName"
const val COL_PRIMARY_ID = "IdPrimary"
const val COL_IS_COMPLETED = "IsComplete"


//Intent extra
const val INTENT_ID = "Intent_ID"
const val INTENT_TITLE = "Intent_Name"
const val INTENT_CREATED = "Intent_Created"