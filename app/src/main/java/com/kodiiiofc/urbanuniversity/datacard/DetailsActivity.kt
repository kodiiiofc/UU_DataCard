package com.kodiiiofc.urbanuniversity.datacard

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class DetailsActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var imageIV: ImageView
    private lateinit var fullNameTV: TextView
    private lateinit var birthdayTV: TextView
    private lateinit var eventsTV: TextView

    var person: Person? = null

    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        person = intent.getSerializableExtra("person") as Person

        imageIV = findViewById(R.id.iv_image)
        fullNameTV = findViewById(R.id.tv_full_name)
        birthdayTV = findViewById(R.id.tv_birthday)
        eventsTV = findViewById(R.id.tv_events)

        fullNameTV.text = "${person?.firstName} ${person?.lastName}"
        birthdayTV.text = "${person?.birthday} г.р."
        imageIV.setImageURI(Uri.parse(person?.image))

        val dateFormat = DateTimeFormatter.ofPattern("dd.M.yyyy")
        val birthday = LocalDate.parse(person?.birthday, dateFormat)

        eventsTV.text = getEventString("День рождения \uD83C\uDF89", birthday)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getEventString(event: String, date: LocalDate): String {

        val now = LocalDate.now()
        val thisYearBirthday = LocalDate.of(now.year, date.month, date.dayOfMonth)
        val nextYearBirthday = LocalDate.of(now.year + 1, date.month, date.dayOfMonth)
        val monthsDiff: Int
        val daysDiff: Int
        if (now.isBefore(thisYearBirthday)) {
            monthsDiff = Period.between(now, thisYearBirthday).months
            daysDiff = Period.between(now, thisYearBirthday).days
        } else {
            monthsDiff = Period.between(now, nextYearBirthday).months
            daysDiff = Period.between(now, nextYearBirthday).days
        }

        val monthsString = when (monthsDiff) {
            0 -> ""
            1 -> "${monthsDiff} месяц"
            in 2..4 -> "${monthsDiff} месяца"
            else -> "${monthsDiff} месяцев"
        }

        val daysString = when {
            daysDiff == 0 -> ""
            daysDiff % 10 == 1 && daysDiff != 11 -> "${daysDiff} день."
            daysDiff % 10 in 2..4 && daysDiff !in 11..14 -> "${daysDiff} дня."
            else -> "${daysDiff} дней."
        }

        val andString = if (monthsDiff > 0 && daysDiff > 0) " и " else ""

        return if (monthsDiff == 0 && daysDiff == 0) "$event сегодня!"
        else if (monthsDiff == 0 && daysDiff == 1) "$event завтра."
        else "$event через $monthsString$andString$daysString"
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