package com.arlisi.apppt

import AlgoritmoPrincipal
import android.os.Bundle
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var ejecucionHiloAlgoritmo = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnEjecutar.setOnClickListener() {
            if(!this.ejecucionHiloAlgoritmo){
                //iniciarCalculoAlgoritmo()
            }
        }
    }

    /*
    private fun iniciarCalculoAlgoritmo() {
        //val algoritmoPrincipal = AlgoritmoPrincipal(this)
        val editText:EditText = findViewById(R.id.datoCompositores)
        println("VEAMOSOOSOSOSOS "  + editText.text)
        algoritmoPrincipal.definirParametrosParaAlgoritmo(
            (findViewById(R.id.datoCompositores) as EditText).getText().toString().toInt(),
            (findViewById(R.id.datoRepeticiones) as EditText).getText().toString().toInt(),
            (findViewById(R.id.datoMaxEvaluaciones) as EditText).getText().toString().toInt(),
            (findViewById(R.id.datoFCLA) as EditText).getText().toString().toDouble(),
            (findViewById(R.id.datoCFG) as EditText).getText().toString().toDouble(),
            (findViewById(R.id.datoIFG) as EditText).getText().toString().toDouble(),
            (findViewById(R.id.datoMemoria) as EditText).getText().toString().toInt(),
            (findViewById(R.id.datoPrueba) as EditText).getText().toString().toInt(),
            (findViewById(R.id.datoTemperatura) as EditText).getText().toString().toDouble(),
            (findViewById(R.id.datoPrecipitacion) as EditText).getText().toString().toDouble()
        )
        algoritmoPrincipal.start()
    }

     */
}