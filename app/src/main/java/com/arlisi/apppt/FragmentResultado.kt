package com.arlisi.apppt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_resultado.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentResultado.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentResultado : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var resultado = ArrayList<Int>()

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
        return inflater.inflate(R.layout.fragment_resultado, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.resultado = (activity as MainActivityHome).resultadoHome

        if(resultado[0] == 1){
            textoPuroMuros.setText("Alta densidad")
        }else{
            textoPuroMuros.setText("Baja densidad")
        }
        if(resultado[1] == 1){
            textoPuroTecho.setText("Alta densidad")
        }else{
            textoPuroTecho.setText("Baja densidad")
        }

        if(resultado[2] == 1){
            textoPuroAltura.setText("Configuración Alta")
        }else{
            textoPuroAltura.setText("Configuración Baja")
        }

        if(resultado[3] == 1){
            textoPuroAcabado.setText("Color Claro")
        }else{
            textoPuroAcabado.setText("Color Oscuro")
        }

        if(resultado[4] == 1){
            textoPuroVentilacion.setText("Ventilación Unilateral")
        }else{
            textoPuroVentilacion.setText("Ventilación Cruzada")
        }

        if(resultado[5] == 1){
            textoPuroTransmitancia.setText("Transmitancia del cristal Alta")
        }else{
            textoPuroTransmitancia.setText("Transmitancia del cristal Baja")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentResultado.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentResultado().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}