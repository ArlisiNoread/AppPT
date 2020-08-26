package com.arlisi.apppt

import AlgoritmoPrincipal
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var ejecucionHiloAlgoritmo = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        println("##########################################################################")
        println("##########################################################################")
        println("Inicio:")

        btnEjecutar.setOnClickListener() {
            if(!this.ejecucionHiloAlgoritmo){
                iniciarCalculoAlgoritmo()
            }

        }
    }

    private fun iniciarCalculoAlgoritmo() {
        val algoritmoPrincipal = AlgoritmoPrincipal(this)
        algoritmoPrincipal.definirParametrosParaAlgoritmo(
            10,
            1000,
            50,
            0.1,
            0.1,
            0.1,
            5,
            30,
            5.0,
            200.0
        )
        algoritmoPrincipal.start()
    }
}