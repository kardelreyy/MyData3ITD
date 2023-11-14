package com.example.mydata3itd

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context : Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){ //conn string
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "EmployeeDatabase" //make sure walang space sa name
        private val TABLE_CONTACTS = "EmployeeTable"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY"
                + KEY_NAME + "TEXT,"
                + KEY_EMAIL + " TEXT)") //dito papasok ung query
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS" + TABLE_CONTACTS)
        onCreate(db)
    }

    //INSTRUCTION SET NG SAVE
    fun addEmployee(emp : EmpModelClass):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId) //galing tong mga violet dun sa EmpModelClass.kt
        contentValues.put(KEY_NAME, emp.username)
        contentValues.put(KEY_EMAIL, emp.userEmail)

        //command for us to insert these values papunta kay db
        val success = db.insert(TABLE_CONTACTS,null,contentValues)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun viewEmployee(): List<EmpModelClass>{
        val empList : ArrayList<EmpModelClass> = ArrayList<EmpModelClass>()
        //query to retrieve data from db assigned to a var
        val selectQuery = "SELECT * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor : Cursor? = null


        //try and catch to avoid out of bounds in db
        try {
            cursor = db.rawQuery(selectQuery, null)
        }
        catch(e: SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var userId : Int
        var userName : String
        var userEmail : String
        if(cursor.moveToFirst()){
            do{ //will get the data
                userId = cursor.getInt(cursor.getColumnIndex("id")) //pag namula right click tas suppressrange
                userName = cursor.getString(cursor.getColumnIndex("name"))
                userEmail = cursor.getString(cursor.getColumnIndex("email"))
                val emp = EmpModelClass(userId = userId, username = userName, userEmail = userEmail) //yung may = string dapat ung mga blur, ung mga white ung mga bagong declare
                empList.add(emp)
            }while(cursor.moveToNext())
        }
        return empList
    }

    fun updateEmployee(emp: EmpModelClass): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId) //galing tong mga violet dun sa EmpModelClass.kt
        contentValues.put(KEY_NAME, emp.username)
        contentValues.put(KEY_EMAIL, emp.userEmail)

        val success = db.update(TABLE_CONTACTS, contentValues, "id="+emp.userId, null)
        db.close()
        return success
    }

    fun deleteEmployee(emp: EmpModelClass): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId)

        val success = db.delete(TABLE_CONTACTS, "id="+emp.userId, null)
        db.close()
        return success
    }
}