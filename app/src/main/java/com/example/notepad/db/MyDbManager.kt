package com.example.myfirstapp.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.notepad.db.ListItem
import com.example.notepad.db.MyDbNameClass
//для добавления в БД, считывания БД
class MyDbManager(val context : Context) {

    val MyDbHelper = MyDbHelper(context)
    var db : SQLiteDatabase? = null

    fun openDb(){
        db = MyDbHelper.writableDatabase
    }

    fun insertToDb(title:String,content:String,uri:String){
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE,title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT,content)
            put(MyDbNameClass.COLUMN_NAME_IMAGE_URI,uri)
        }
        db?.insert(MyDbNameClass.TABLE_NAME,null,values)
    }

    @SuppressLint("Range")
    fun readDbDate(searchText :String):ArrayList<ListItem>{
        val dataList = ArrayList<ListItem>()
        val selection= "${MyDbNameClass.COLUMN_NAME_TITLE} like ?"
        val cursor = db?.query(MyDbNameClass.TABLE_NAME,null,selection, arrayOf("%$searchText%"),
            null,null,null)


            while (cursor?.moveToNext()!!){
                val dataTextTitle = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_TITLE))
                val dataTextContent = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_CONTENT))
                val dataTextUri = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_IMAGE_URI))
                val dataIndex = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
                val item = ListItem()
                item.title= dataTextTitle.toString()
                item.desc = dataTextContent.toString()
                item.uri = dataTextUri.toString()
                item.id = dataIndex
                dataList.add(item)
            }

        cursor.close()
        return dataList
    }

    fun closeDb()
    {
        MyDbHelper?.close()
    }

    fun removeItemFromDb(id:String){
        val selection = BaseColumns._ID + "=$id"
        db?.delete(MyDbNameClass.TABLE_NAME,selection,null)
    }

}