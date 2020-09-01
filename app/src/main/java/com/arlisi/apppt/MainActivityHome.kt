package com.arlisi.apppt

import android.content.res.AssetManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivityHome : AppCompatActivity() {
    lateinit var toolbar: ActionBar
    private var temperaturaCheckForButton = false
    private var precipitacionCheckForButton = false
    lateinit var assetsLocal: AssetManager
    var resultadoHome = ArrayList<Int>()

    var calculadoraFragment: FragmentCalculadora? = null

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    toolbar.title = "Calculadora"
                    if (calculadoraFragment == null) {
                        calculadoraFragment = FragmentCalculadora.newInstance("1", "2")
                    }
                    openFragment(calculadoraFragment!!)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.page_2 -> {
                    toolbar.title = "Historial"
                    val historialFragment = Fragment_Historial.newInstance("1", "2")
                    openFragment(historialFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.page_3 -> {
                    toolbar.title = "Ayuda"
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_home)
        toolbar = supportActionBar!!
        toolbar.title = ""
        val bottom_navigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottom_navigation.selectedItemId = R.id.page_1
    }

    public fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contenedor, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}