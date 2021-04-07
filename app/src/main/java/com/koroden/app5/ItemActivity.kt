package com.koroden.app5

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

class ItemActivity : AppCompatActivity() {
    private var index = 0
    private lateinit var item: Item

    // Хранилище имени временного файла
    private var currentPhotoPath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        val intent = intent
        index = intent?.getIntExtra("index", -1) ?: -1
        item = intent?.getParcelableExtra("item") ?: Item()

        val editTitle = findViewById<EditText>(R.id.title)
        editTitle.setText(item.title)

        val editKind = findViewById<EditText>(R.id.kind)
        editKind.setText(item.kind)

        val editPrice = findViewById<EditText>(R.id.price)
        if(item.price != 0.0){
            editPrice.setText(item.price.toString())
        }

        val editWeight = findViewById<EditText>(R.id.weight)
        if(item.weight != 0.0){
            editWeight.setText(item.weight.toString())
        }

        if (item.photo != "") {
            val bmp = BitmapFactory.decodeFile((item.photo))
            findViewById<ImageView>(R.id.ivPhoto).setImageBitmap(bmp)
            currentPhotoPath = item.photo
        }


        // Включаем кнопку Назад
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK) {
            val bmp = BitmapFactory.decodeFile(currentPhotoPath)
            val ivPhoto = findViewById<ImageView>(R.id.ivPhoto)
            ivPhoto.setImageBitmap(bmp)
        }
        else
            currentPhotoPath = ""
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }

        if (item.itemId == R.id.action_photo) {
            // Формирование имени временного файла
            val photoFile = File.createTempFile(
                "photo",
                ".jpg",
                getExternalFilesDir(Environment.DIRECTORY_PICTURES))
            // Сохранение имени для будущего использования
            currentPhotoPath = photoFile.absolutePath

            // Получение провайдера файла, чтобы камера могла записать файл в нашу частную папку
            val photoURI = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID,
                photoFile)

            // Формирование запроса на фото
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(intent, 0)
            return true
        }

        if (item.itemId == R.id.action_save) {

            //Сохранение данных
            if(findViewById<EditText>(R.id.title).text.toString() != "" &&
                findViewById<EditText>(R.id.kind).text.toString() != "" &&
                findViewById<EditText>(R.id.price).text.toString().toDouble() > 0.0 &&
                findViewById<EditText>(R.id.weight).text.toString().toDouble() > 0.0){

                this.item.title = findViewById<EditText>(R.id.title).text.toString()
                this.item.kind = findViewById<EditText>(R.id.kind).text.toString()
                this.item.price = findViewById<EditText>(R.id.price).text.toString().toDouble()
                this.item.weight = findViewById<EditText>(R.id.weight).text.toString().toDouble()
                this.item.photo = currentPhotoPath

                val intent = Intent()
                intent.putExtra("index", index)
                intent.putExtra("item", this.item)
                setResult(Activity.RESULT_OK, intent)
            }else{
                val toast =
                    Toast.makeText(applicationContext, "Данные не были указаны", Toast.LENGTH_SHORT)
                toast.show()
            }

            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}