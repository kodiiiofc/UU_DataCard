package com.kodiiiofc.urbanuniversity.datacard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar : Toolbar
    private lateinit var imageIV: ImageView
    private lateinit var firstNameET: EditText
    private lateinit var lastNameET: EditText
    private lateinit var birthdayDP: DatePicker
    private lateinit var saveBTN: Button

    val noImageString = "android.resource://com.kodiiiofc.example.grocerystore/drawable/noimage"
    private var imageUri: Uri? = Uri.parse(noImageString)

    private val imagePick = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        imageIV = findViewById(R.id.iv_image)
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            imageUri = data?.data
            imageIV.setImageURI(imageUri)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        imageIV = findViewById(R.id.iv_image)
        firstNameET = findViewById(R.id.et_first_name)
        lastNameET = findViewById(R.id.et_last_name)
        birthdayDP = findViewById(R.id.dp_birthday)
        saveBTN = findViewById(R.id.btn_save)

        imageIV.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            imagePick.launch(photoPickerIntent)
        }

        saveBTN.setOnClickListener {
            val birthday = "${birthdayDP.dayOfMonth}.${birthdayDP.month+1}.${birthdayDP.year}"

            val person = Person(
                firstName = firstNameET.text.toString().trim(),
                lastName = lastNameET.text.toString().trim(),
                birthday = birthday,
                image = imageUri.toString()
            )

            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("person", person)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_exit -> {
                Toast.makeText(applicationContext, "Приложение закрыто", Toast.LENGTH_SHORT).show()
                finishAffinity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}