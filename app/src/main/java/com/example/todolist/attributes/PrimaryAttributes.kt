package com.example.todolist.attributes

class PrimaryAttributes{
    var id: Long = -1
    var title: String = ""
    var group: Int = 0
    var items: Int = 0
    var itemsChecked: Int = 0
    var itemsList: MutableList<ItemAttributes> = ArrayList()
}