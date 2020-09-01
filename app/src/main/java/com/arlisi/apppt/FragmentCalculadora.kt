package com.arlisi.apppt

import AlgoritmoPrincipal
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_calculadora.*
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentCalculadora.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentCalculadora : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var ejecucionHiloAlgoritmo = false
    var resultado: ArrayList<Int>? by Delegates.observable(null) { property, oldValue, newValue ->
        //esconderPantallaFlotante()

        (activity as MainActivityHome).resultadoHome = newValue!!

        var fm = super.getActivity()?.supportFragmentManager
        fm!!.beginTransaction().replace(R.id.contenedor, FragmentResultado.newInstance("1", "2")).addToBackStack(null).commit()
    }

    lateinit var toolbar: ActionBar

    private var temperaturaCheckForButton = false
    private var precipitacionCheckForButton = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calculadora, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment calculadora.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentCalculadora().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        inicializarUI()
        textButtonIniciarAlgoritmo.isClickable = false
        if (ejecucionHiloAlgoritmo) {
            ventanaFlotante.visibility = View.VISIBLE
        }

    }

    private fun inicializarUI() {

        textoPuroTemperatura.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val texto = p0.toString()
                if (texto.isEmpty()) {
                    temperaturaCheckForButton = false
                    textField_Temperatura.error = null
                } else if (texto.toDouble() in 0.0..50.0) {
                    temperaturaCheckForButton = true
                    textField_Temperatura.error = null
                } else {
                    temperaturaCheckForButton = false
                    textField_Temperatura.error = "Rango de 0 a 50"
                }
                revisarSiHabilitarBoton()
            }
        })

        textoPuroPrecipitacion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val texto = p0.toString()
                if (texto.isEmpty()) {
                    precipitacionCheckForButton = false
                    textField_Precipitacion.error = null
                } else if (texto.toDouble() in 0.0..2000.0) {
                    precipitacionCheckForButton = true
                    textField_Precipitacion.error = null
                } else {
                    precipitacionCheckForButton = false
                    textField_Precipitacion.error = "Rango de 0 a 2000"

                }
                revisarSiHabilitarBoton()
            }
        })

        textoPuroCompositores.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val texto = p0.toString()
                if (texto.isEmpty()) {
                    textField_Compositores.error = "Campo vacío"
                } else if (texto.toDouble() in 3.0..100.0) {
                    textField_Compositores.error = null
                } else {
                    textField_Compositores.error = "Rango de 3 a 100"
                }
                revisarSiHabilitarBoton()
            }
        })

        textoPuroRepeticiones.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val texto = p0.toString()
                if (texto.isEmpty()) {
                    textField_Repeticiones.error = "Campo vacío"
                } else if (texto.toDouble() in 1.0..100.0) {
                    textField_Repeticiones.error = null
                } else {
                    textField_Repeticiones.error = "Rango de 1 a 100"
                }
                revisarSiHabilitarBoton()
            }
        })

        textoPuroMemoria.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val texto = p0.toString()
                if (texto.isEmpty()) {
                    textField_Memoria.error = "Campo vacío"
                } else if (texto.toDouble() in 3.0..10.0) {
                    textField_Memoria.error = null
                } else {
                    textField_Memoria.error = "Rango de 3 a 10"
                }
                revisarSiHabilitarBoton()
            }
        })

        textoPuroPruebas.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val texto = p0.toString()
                if (texto.isEmpty()) {
                    textField_Pruebas.error = "Campo vacío"
                } else if (texto.toDouble() in 10.0..100.0) {
                    textField_Pruebas.error = null
                } else {
                    textField_Pruebas.error = "Rango de 10 a 100"
                }
                revisarSiHabilitarBoton()
            }
        })

        textoPuroEvaluaciones.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val texto = p0.toString()
                if (texto.isEmpty()) {
                    textField_Evaluaciones.error = "Campo vacío"
                } else if (texto.toDouble() in 1.0..10000.0) {
                    textField_Evaluaciones.error = null
                } else {
                    textField_Evaluaciones.error = "Rango de 1 a 10000"
                }
                revisarSiHabilitarBoton()
            }
        })

        textoPuroIFG.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val texto = p0.toString()
                if (texto.isEmpty()) {
                    textField_IFG.error = "Campo vacío"
                } else if (texto.toDouble() in 0.001..1.0) {
                    textField_IFG.error = null
                } else {
                    textField_IFG.error = "Rango de 0.001 a 1"
                }
                revisarSiHabilitarBoton()
            }
        })

        textoPuroCFG.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val texto = p0.toString()
                if (texto.isEmpty()) {
                    textField_CFG.error = "Campo vacío"
                } else if (texto.toDouble() in 0.001..1.0) {
                    textField_CFG.error = null
                } else {
                    textField_CFG.error = "Rango de 0.001 a 1"
                }
                revisarSiHabilitarBoton()
            }
        })

        textoPuroFCLA.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val texto = p0.toString()
                if (texto.isEmpty()) {
                    textField_FCLA.error = "Campo vacío"
                } else if (texto.toDouble() in 0.001..1.0) {
                    textField_FCLA.error = null
                } else {
                    textField_FCLA.error = "Rango de 0.001 a 1"
                }
                revisarSiHabilitarBoton()
            }
        })


        textButtonIniciarAlgoritmo.setOnClickListener() {

            if (!this.ejecucionHiloAlgoritmo && textButtonIniciarAlgoritmo.isClickable) {
                iniciarCalculoAlgoritmo()
                ventanaFlotante.visibility = View.VISIBLE
            }
        }

        textButtonCancelar.setOnClickListener(){
               this.ejecucionHiloAlgoritmo = false
                esconderPantallaFlotante()
        }


    }

    fun esconderPantallaFlotante(){
        ventanaFlotante.visibility = View.GONE
    }

    private fun iniciarCalculoAlgoritmo() {
        println("Iniciando algoritmo")
        val algoritmoPrincipal = AlgoritmoPrincipal(this)


        algoritmoPrincipal.definirParametrosParaAlgoritmo(
            (requireView().findViewById<TextInputEditText>(R.id.textoPuroCompositores)).text.toString()
                .toInt(),
            (requireView().findViewById<TextInputEditText>(R.id.textoPuroRepeticiones)).text.toString()
                .toInt(),
            (requireView().findViewById<TextInputEditText>(R.id.textoPuroEvaluaciones)).text.toString()
                .toInt(),
            (requireView().findViewById<TextInputEditText>(R.id.textoPuroFCLA)).text.toString()
                .toDouble(),
            (requireView().findViewById<TextInputEditText>(R.id.textoPuroCFG)).text.toString()
                .toDouble(),
            (requireView().findViewById<TextInputEditText>(R.id.textoPuroIFG)).text.toString()
                .toDouble(),
            (requireView().findViewById<TextInputEditText>(R.id.textoPuroMemoria)).text.toString()
                .toInt(),
            (requireView().findViewById<TextInputEditText>(R.id.textoPuroPruebas)).text.toString()
                .toInt(),
            (requireView().findViewById<TextInputEditText>(R.id.textoPuroTemperatura)).text.toString()
                .toDouble(),
            (requireView().findViewById<TextInputEditText>(R.id.textoPuroPrecipitacion)).text.toString()
                .toDouble()
        )
        algoritmoPrincipal.start()

    }

    private fun revisarSiHabilitarBoton() {
        if (temperaturaCheckForButton &&
            precipitacionCheckForButton &&
            textField_Compositores.error == null &&
            textField_Repeticiones.error == null &&
            textField_Memoria.error == null &&
            textField_Pruebas.error == null &&
            textField_Evaluaciones.error == null &&
            textField_IFG.error == null &&
            textField_CFG.error == null &&
            textField_FCLA.error == null
        ) {
            textButtonIniciarAlgoritmo.setClickable(true)
            textButtonIniciarAlgoritmo.setText(R.string.botonAceptado)
            textButtonIniciarAlgoritmo.setBackgroundColor(
                ContextCompat.getColor(
                    this.requireContext(),
                    R.color.colorBotonNormal
                )
            )

        } else {
            textButtonIniciarAlgoritmo.setClickable(false)
            textButtonIniciarAlgoritmo.setText(R.string.botonEnEspera)
            textButtonIniciarAlgoritmo.setBackgroundColor(
                ContextCompat.getColor(
                    this.requireContext(),
                    R.color.colorBotonEsperandoDatos
                )
            )
        }
    }
}