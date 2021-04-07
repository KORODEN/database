package com.koroden.app5

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.ActivityInfo
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private var items = ArrayList<Item>()
    private lateinit var con: SQLiteDatabase;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        val db = SQLiteHelper(this);
        con = db.readableDatabase
        getItems()

        // Настройка плавающей кнопки
        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, ItemActivity::class.java)
            startActivityForResult(intent, 0)
        }

        // Настройка списка
        val listView: ListView = findViewById(R.id.listItems)
        listView.adapter = ItemAdapter(this, items)

        listView.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            val intent = Intent(this, ItemActivity::class.java)
            intent.putExtra("index", i)
            intent.putExtra("item", items[i])
            startActivityForResult(intent, 0)
        }

        listView.setOnItemLongClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->

            createAlertDialog(i, view1)
            return@setOnItemLongClickListener true
        }
    }

    private fun createAlertDialog(position: Int, view: View) {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Удаление")
        builder.setMessage("Очистить элемент \"" + items.get(position).info + "\"?")
        builder.setNegativeButton("Да") { dialog, which -> deleteItem(position, view) }
        builder.setPositiveButton("Нет") { dialog, which -> dialog.cancel() }
        builder.show()
    }

    private fun deleteItem(position: Int, view: View?) {
        val deletedStudent: Item = items.get(position)
        con.delete("items", "id = ?", arrayOf(Integer.toString(deletedStudent.id)))
        items.removeAt(position)
        
        val listView: ListView = findViewById(R.id.listItems)
        (listView.adapter as ItemAdapter).notifyDataSetChanged()

        val snackBar = Snackbar.make(view!!, "Элемент очищен", Snackbar.LENGTH_LONG)
        snackBar.duration = 3000
        snackBar.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
        {
            val index: Int = data?.getIntExtra("index", -1) ?: -1
            val item: Item = data?.getParcelableExtra("item") ?: Item()

            val cv = ContentValues()
            cv.put("kind", item.kind)
            cv.put("title", item.title)
            cv.put("price", item.price)
            cv.put("weight", item.weight)
            cv.put("photo", item.photo)
            if (index != -1) {
                items[index] = item
                cv.put("id", item.id)
                con.update("items", cv, "id=?", arrayOf(item.id.toString()))
            }
            else {
                items.add(item)
                con.insert("items", null, cv)
            }

            val listView: ListView = findViewById(R.id.listItems)
            (listView.adapter as ItemAdapter).notifyDataSetChanged()
        }
    }

    private fun getItems() {
        val cursor = con.query("items", arrayOf("id", "kind", "title", "price", "weight", "photo"),
                null, null, null, null, null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val s = Item()
            s.id = cursor.getInt(0)
            s.kind = cursor.getString(1)
            s.title = cursor.getString(2)
            s.price = cursor.getDouble(3)
            s.weight = cursor.getDouble(4)
            s.photo = cursor.getString(5)
            items.add(s)
            cursor.moveToNext()
        }
        cursor.close()
    }

}