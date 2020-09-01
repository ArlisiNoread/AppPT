import android.content.Intent
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.arlisi.algoritmo.RandomAux
import com.arlisi.apppt.FragmentCalculadora
import kotlinx.android.synthetic.main.fragment_calculadora.*
import org.ejml.data.DMatrixRMaj
import org.ejml.simple.SimpleMatrix
import kotlin.math.*
import kotlin.random.Random


class AlgoritmoPrincipal(private val main: FragmentCalculadora) : Thread() {

    private var hiloCorriendo = false
    private var configuracion: ArrayList<ArrayList<Double>> = ArrayList<ArrayList<Double>>();
    private var PPDCDMX: ArrayList<ArrayList<Double>> = ArrayList<ArrayList<Double>>();
    private var PMVCDMX: ArrayList<ArrayList<Double>> = ArrayList<ArrayList<Double>>();
    private var TRMCDMX: ArrayList<ArrayList<Double>> = ArrayList<ArrayList<Double>>();
    private var PPDChihuahua: ArrayList<ArrayList<Double>> = ArrayList<ArrayList<Double>>();
    private var PMVChihuahua: ArrayList<ArrayList<Double>> = ArrayList<ArrayList<Double>>();
    private var TRMChihuahua: ArrayList<ArrayList<Double>> = ArrayList<ArrayList<Double>>();
    private var PPDXalapa: ArrayList<ArrayList<Double>> = ArrayList<ArrayList<Double>>();
    private var PMVXalapa: ArrayList<ArrayList<Double>> = ArrayList<ArrayList<Double>>();
    private var TRMXalapa: ArrayList<ArrayList<Double>> = ArrayList<ArrayList<Double>>();
    private var PPDcancun: ArrayList<ArrayList<Double>> = ArrayList<ArrayList<Double>>();
    private var PMVcancun: ArrayList<ArrayList<Double>> = ArrayList<ArrayList<Double>>();
    private var TRMcancun: ArrayList<ArrayList<Double>> = ArrayList<ArrayList<Double>>();

    private var pa: Int = 0
    private var repeticiones: Int = 0
    private var maxEvaluaciones: Int = 0
    private var fcla: Double = 0.0
    private var cfg: Double = 0.0
    private var cfi: Double = 0.0
    private var mc: Int = 0
    private var pruebas: Int = 0
    private var temperatura: Double = 0.0
    private var precipitacion: Double = 0.0

    private var inicioCronometro: Long = 0
    private var finCronometro: Long = 0


    private fun importaDatos() {
        this.configuracion = archivoAMatriz("configuracion.txt")
        this.PPDCDMX = archivoAMatriz("PPDCDMX.txt")
        this.PMVCDMX = archivoAMatriz("PMVCDMX.txt")
        this.TRMCDMX = archivoAMatriz("TRMCDMX.txt")
        this.PPDChihuahua = archivoAMatriz("PPDChihuahua.txt")
        this.PMVChihuahua = archivoAMatriz("PMVChihuahua.txt")
        this.TRMChihuahua = archivoAMatriz("TRMChihuahua.txt")
        this.PPDXalapa = archivoAMatriz("PPDXalapa.txt")
        this.PMVXalapa = archivoAMatriz("PMVXalapa.txt")
        this.TRMXalapa = archivoAMatriz("TRMXalapa.txt")
        this.PPDcancun = archivoAMatriz("PPDcancun.txt")
        this.PMVcancun = archivoAMatriz("PMVcancun.txt")
        this.TRMcancun = archivoAMatriz("TRMcancun.txt")
    }


    public fun estadoHilo(): Boolean {
        return this.hiloCorriendo
    }

    public override fun run() {

        main.ejecucionHiloAlgoritmo = true
        val resultados: ArrayList<ArrayList<Double>>? = iniciarAlgoritmo()
        if(resultados != null){
            analisisYProcesamientoDeResultados(resultados)
            main.ejecucionHiloAlgoritmo = false
        }
        //main.ventanaFlotante.visibility = View.GONE
    }

    private fun analisisYProcesamientoDeResultados(resultados: ArrayList<ArrayList<Double>>) {
        val valoresMedios = ArrayList<Int>().apply {
            for (yy in 0 until resultados[0].size) {
                var sumatoria = 0.0
                for (xx in 0 until resultados.size) {
                    sumatoria += resultados[xx][yy]
                }
                add(round(sumatoria / resultados.size).toInt())
            }
        }

        val recomendacion = ArrayList<Int>()

        var j = 0
        for (ii in 0 until 6) {
            if (valoresMedios[j] == 1) {
                recomendacion.add(1)
            } else {
                recomendacion.add(2)
            }
            j += 2
        }
        println(recomendacion)

        /*
        val intent = Intent(main, PanelResultadosActivity::class.java).apply {
            putExtra("resultados", recomendacion)
        }
        main.startActivity(intent)
        */
        main.resultado = recomendacion
    }

    public fun definirParametrosParaAlgoritmo(
        pa: Int,
        repeticiones: Int,
        maxEvaluaciones: Int,
        fcla: Double,
        cfg: Double,
        cfi: Double,
        mc: Int,
        pruebas: Int,
        temperatura: Double,
        precipitacion: Double,
    ) {
        if (!this.hiloCorriendo) {
            this.pa = pa
            this.repeticiones = repeticiones
            this.maxEvaluaciones = maxEvaluaciones
            this.fcla = fcla
            this.cfg = cfg
            this.cfi = cfi
            this.mc = mc
            this.pruebas = pruebas
            this.temperatura = temperatura
            this.precipitacion = precipitacion
        }

    }

    private fun iniciarAlgoritmo(): ArrayList<ArrayList<Double>>? {


        //Inicializo la barra de progreso
        val vbarraProgreso: ProgressBar = main.progressBar
        vbarraProgreso.progress = 0

        //Inicializo variables de datos y las cargo con la información
        importaDatos()

        //sol: array con la solución final
        var sol = ArrayList<ArrayList<Double>>()


        val a = configuracion.size
        var sol_corrida = ArrayList<ArrayList<Int>>()
        var objetivos = ArrayList<ArrayList<Double>>()
        for (i in 0 until (mc * pa)) objetivos.add(arrayListOf<Double>(0.0, 0.0, 0.0, 0.0))

        //Inicia ciclo de r hasta repeticiones
        for (r in 0 until repeticiones) {
            if(!main.ejecucionHiloAlgoritmo) return null

            actualizaBarraDeProgreso(r, repeticiones, vbarraProgreso)
            iniciarCronometro()

            var ppd = ArrayList<ArrayList<Double>>()
            var pmv = ArrayList<ArrayList<Double>>()
            var trm = ArrayList<ArrayList<Double>>()
            var disTT = 0.0


            //Reinicio Matrices
            sol_corrida = ArrayList<ArrayList<Int>>()
            for (i in 0 until (mc * pa)) sol_corrida.add(ArrayList<Int>())
            objetivos = ArrayList<ArrayList<Double>>()
            for (i in 0 until (mc * pa)) objetivos.add(arrayListOf<Double>(0.0, 0.0, 0.0, 0.0))

            //Soluciones iniciales
            for (i in 0 until (mc * pa)) {
                var aux = ArrayList<Int>()
                for (t in 0 until 6) aux.add(round(Random.nextDouble()).toInt())
                //for (t in 0 until 6) aux.add(0)

                for (j in 0 until 6) {
                    sol_corrida[i].add(aux[j])
                    sol_corrida[i].add(1 - aux[j])
                }

                var distancias = ArrayList<Double>()
                for (t in 0 until a) distancias.add(0.0)

                for (j in 0 until a) {
                    var configuracionMenosSolCorridaPow = ArrayList<Double>()
                    for (jk in 0 until 12) configuracionMenosSolCorridaPow.add(
                        (configuracion[j][jk] - sol_corrida[i][jk]).pow(
                            2
                        )
                    )
                    var tempSuma = 0.0
                    configuracionMenosSolCorridaPow.forEach { tempSuma += it }
                    distancias[j] = sqrt(tempSuma)
                }

                val posicionDistanciaMinima =
                    distancias.withIndex().minByOrNull { (_, f) -> f }?.index
                //LINEA 30 de MATLAB!!!!!!!!!
                val ppdPmvTrm = funcion_datos(pruebas, temperatura, precipitacion)
                ppd = ppdPmvTrm[0]
                pmv = ppdPmvTrm[1]
                trm = ppdPmvTrm[2]

                val max_TRM = trm.maxOfOrNull { it.maxOfOrNull { it } ?: 0.0 } ?: 0.0
                val min_TRM = trm.minOfOrNull { it.minOfOrNull { it } ?: 0.0 } ?: 0.0
                disTT = max_TRM - min_TRM

                for (k in 0 until 6) {
                    if (ppd[posicionDistanciaMinima ?: 0][k] <= 15.0) {
                        objetivos[i][0] += 0.0
                    } else {
                        objetivos[i][0] += (1.0 / 6.0) * ((15.0 - ppd[posicionDistanciaMinima
                            ?: 0][k]) / (-85.0))
                    }
                    if (pmv[posicionDistanciaMinima ?: 0][k] == 0.0) {
                        objetivos[i][1] += 0.0
                    } else {
                        objetivos[i][1] += (1.0 / 6.0) * (abs(
                            pmv[posicionDistanciaMinima ?: 0][k]
                        )) / 3.0
                    }
                    if (trm[posicionDistanciaMinima ?: 0][k] in 18.0..23.0) {
                        objetivos[i][2] += 0.0
                    } else {
                        val aux1 = max(
                            18.0 - trm[posicionDistanciaMinima ?: 0][k],
                            23.0 - trm[posicionDistanciaMinima ?: 0][k]
                        )
                        objetivos[i][2] += (1.0 / 6.0) * abs(aux1) / disTT
                    }
                    objetivos[i][3] = doubleArrayOf(
                        objetivos[i][0],
                        objetivos[i][1],
                        objetivos[i][2]
                    ).maxOf { it }
                }

            }

            //VAMOS AQUI
            objetivos.forEach({ for (o in 0 until it.size) it[o] *= (1.0 / 5.0) })

            var links = ArrayList<ArrayList<Double>>()

            for (v in 0 until maxEvaluaciones) {
                //Crea o actualiza vínculos
                vinculos(pa, fcla, v, links)

                //Valorar información contenida en cada agente
                var sol_corrida1 = ArrayList<ArrayList<Int>>()
                sol_corrida.forEach({ sol_corrida1.add(ArrayList(it)) })

                var objetivos2 = ArrayList<ArrayList<Double>>()
                objetivos.forEach({ objetivos2.add(ArrayList(it)) })

                var Contadores = ArrayList<ArrayList<Double>>()
                for (i in 0 until pa) {
                    Contadores.add(ArrayList<Double>().apply { for (aa in 0 until 6) add(0.0) })
                    val a = 1 + (i) * mc
                    val b = a + mc - 2

                    val A = ArrayList<ArrayList<Double>>()
                    for (cc in (a - 1) until b) {
                        A.add(ArrayList(objetivos[cc]))
                    }

                    ArrayList<Double>().apply {
                        for (xx in 0 until A.size) add(A[xx][3])
                        Contadores[i][0] = maxOfOrNull { it } ?: 0.0
                        Contadores[i][1] =
                            withIndex().maxByOrNull { (_, f) -> f }?.index!!.toDouble()
                        Contadores[i][2] = minByOrNull { it } ?: 0.0
                        Contadores[i][3] =
                            withIndex().minByOrNull { (_, f) -> f }?.index!!.toDouble()
                    }

                    Contadores[i][1] = Contadores[i][1] + (i * mc)
                    Contadores[i][3] = Contadores[i][3] + (i * mc)
                    Contadores[i][4] = a.toDouble()
                    Contadores[i][5] = b.toDouble()
                }
                //Aquí hubo un detalle con el redondeo que luego deberías checar
                //Intercambiar información por una política de cambio

                val MI = ArrayList<ArrayList<Double>>()
                val MIo = ArrayList<ArrayList<Double>>()
                val destino = ArrayList<Int>()
                var contador = 0.0

                for (i in 0 until pa) {
                    val aux_i = Contadores[i][0]
                    for (j in 0 until pa) {
                        val aux_j = Contadores[j][0]
                        if (i != j) {
                            if (links[j][i] == 1.0) {
                                if (aux_j < aux_i) {
                                    val seleccion =
                                        round(Contadores[j][4] + Random.nextDouble() * Contadores[j][5] - Contadores[j][4])
                                    //round(Contadores[j][4] + 0.5 * Contadores[j][5] - Contadores[j][4])
                                    contador++
                                    MI.add(ArrayList<Double>().apply {
                                        for (xx in 0 until 12) {
                                            add(sol_corrida[seleccion.toInt()][xx].toDouble())
                                        }
                                    })
                                    MIo.add(ArrayList<Double>().apply {
                                        for (xx in 0 until 4) {
                                            add(objetivos[seleccion.toInt()][xx])
                                        }
                                    })
                                    destino.add(i)
                                }
                            }
                        }
                    }
                }

                val cMI = MI.size
                var aux3: ArrayList<Double>

                for (i in 0 until pa) {
                    val a = (i) * mc
                    val b = a + mc - 1
                    val MIndividual = ArrayList<ArrayList<Int>>().apply {
                        for (xx in a..b) add(ArrayList<Int>().apply {
                            for (yy in 0 until sol_corrida[xx].size) add(sol_corrida[xx][yy])
                        })
                    }
                    val MIndividualObjetivos = ArrayList<ArrayList<Double>>().apply {
                        for (xx in a..b) add(ArrayList<Double>().apply {
                            for (yy in 0 until objetivos[xx].size) add(objetivos[xx][yy])
                        })
                    }
                    val IA = ArrayList<ArrayList<Double>>()
                    val IAo = ArrayList<ArrayList<Double>>()

                    for (k in 0 until cMI) {
                        if (destino[k] == i) {
                            IA.add(ArrayList<Double>().apply {
                                for (xx in 0 until MI[k].size) add(MI[k][xx])
                            })

                            IAo.add(ArrayList<Double>().apply {
                                for (xx in 0 until MIo[k].size) add(MIo[k][xx])
                            })
                        }
                    }


                    if (Random.nextDouble() <= cfg) {
                        //if (0.01 <= cfg) {
                        val MK = ArrayList<ArrayList<Double>>().apply {
                            for (xx in 0 until MIndividual.size) {
                                add(ArrayList<Double>().apply {
                                    for (yy in 0 until MIndividual[xx].size) {
                                        add(MIndividual[xx][yy].toDouble())
                                    }
                                })
                            }
                            for (xx in 0 until IA.size) {
                                add(ArrayList<Double>().apply {
                                    for (yy in 0 until IA[xx].size) {
                                        add(IA[xx][yy])
                                    }
                                })
                            }
                        }

                        val MKo = ArrayList<ArrayList<Double>>().apply {
                            for (xx in 0 until MIndividualObjetivos.size) {
                                add(ArrayList<Double>().apply {
                                    for (yy in 0 until MIndividualObjetivos[xx].size) {
                                        add(MIndividualObjetivos[xx][yy])
                                    }
                                })
                            }
                            for (xx in 0 until IAo.size) {
                                add(ArrayList<Double>().apply {
                                    for (yy in 0 until IAo[xx].size) {
                                        add(IAo[xx][yy])
                                    }
                                })
                            }
                        }


                        var tempArr = ArrayList<Double>()
                        for (xx in 0 until MKo.size) {
                            tempArr.add(MKo[xx][3])
                        }
                        val peor = tempArr.maxByOrNull { it } ?: 0.0

                        val mejor =
                            ArrayList<Double>().apply { for (xx in 0 until MKo.size) add(MKo[xx][3]) }
                                .minByOrNull { it } ?: 0.0
                        val diferencia = peor - mejor
                        val er1 = MKo.size
                        val preponderancia: ArrayList<Double>

                        if (diferencia == 0.0) {
                            preponderancia =
                                ArrayList<Double>().apply { for (xx in 0 until er1) add(1.0 / er1) }
                        } else {
                            preponderancia =
                                ArrayList<Double>().apply {
                                    for (xx in 0 until MKo.size) add(0.1 + (peor - MKo[xx][3].toDouble()) / diferencia)
                                }
                        }

                        for (xx in 0 until preponderancia.size) preponderancia[xx] =
                            preponderancia[xx].pow(2)

                        val aux2 = preponderancia.maxByOrNull { it } ?: 0.0

                        val filtrado = ArrayList<ArrayList<Int>>().apply {
                            add(ArrayList<Int>().apply {
                                for (xx in 0 until MK[aux2.toInt()].size) add(
                                    MK[aux2.toInt()][xx].toInt()
                                )
                            })
                        }

                        val c1 = ArrayList<Double>().apply { add(preponderancia[aux2.toInt()]) }
                        preponderancia[aux2.toInt()] = 0.0

                        val auxSum = preponderancia.sum()

                        for (xx in 0 until preponderancia.size) preponderancia[xx] /= auxSum

                        var seleccion = Random.nextDouble()
                        //var seleccion = 0.03

                        var p = 0.0
                        var ps = 0.0

                        while (p < seleccion && ps < er1) {
                            ps = ps + 1
                            p = p + preponderancia[ps.toInt() - 1]
                        }

                        filtrado.add(ArrayList<Int>().apply {
                            for (xx in 0 until MK[(ps - 1).toInt()].size) add(MK[(ps - 1).toInt()][xx].toInt())
                        })

                        c1.add(preponderancia[(ps - 1).toInt()])

                        seleccion = round(Random.nextDouble() * (er1 - 1))
                        //seleccion = round( 0.265 * (er1 - 1))

                        filtrado.add(ArrayList<Int>().apply {
                            for (xx in 0 until MK[seleccion.toInt()].size) add(
                                MK[seleccion.toInt()][xx].toInt()
                            )
                        })

                        c1.add(preponderancia[seleccion.toInt()])
                        val tempC = c1.sum()
                        for (xx in 0 until c1.size) c1[xx] /= tempC


                        val mC1 = DMatrixRMaj(1, c1.size)
                        for (xx in 0 until c1.size) mC1.set(0, xx, c1[xx])
                        val mFiltrado = DMatrixRMaj(filtrado.size, filtrado[0].size)
                        for (xx in 0 until filtrado.size) for (yy in 0 until filtrado[xx].size) {
                            mFiltrado.set(xx, yy, filtrado[xx][yy].toDouble())
                        }


                        val aux3Mat = SimpleMatrix(mC1).mult(SimpleMatrix(mFiltrado))
                        aux3 = ArrayList<Double>().apply {
                            for (xx in 0 until aux3Mat.numCols()) add(aux3Mat.get(0, xx))
                        }

                        val aux4 = MK.size
                        val aux5 = MK[0].size


                        //### DETALLE DE ARRAY! CHECAR
                        for (iii in 0 until aux5 - 1 step 2) {
                            if (Random.nextDouble() <= cfi) {
                                //if (0.45 <= cfi) {
                                aux3[iii] = (1 - aux3[iii]).pow(2)
                                aux3[iii + 1] = (1 - aux3[iii]).pow(2)

                            } else {
                                aux3[iii + 1] = (1 - aux3[iii]).pow(2)
                            }
                        }

                    } else {
                        aux3 = ArrayList<Double>()
                        var j = 0
                        for (k in 0 until 6) {
                            aux3.add(round(Random.nextDouble()))
                            //aux3.add(round(0.45))
                            aux3.add((1 - aux3[j]).pow(2))
                            j += 2
                        }
                    }

                    val a1 = configuracion.size
                    val b1 = configuracion[0].size

                    val distancias = ArrayList<Double>().apply {
                        for (xx in 0 until a1) add(0.0)
                    }

                    for (j in 0 until a1) {
                        val tempMat = ArrayList<Double>()
                        for (xx in 0 until configuracion[j].size) tempMat.add(
                            (configuracion[j][xx] - sol_corrida[i][xx]).pow(
                                2
                            )
                        )
                        distancias[j] = sqrt(tempMat.sum())
                    }

                    val min1 = distancias.minByOrNull { it }
                    val min2 = distancias.withIndex().minByOrNull { (_, f) -> f }?.index
                    val objetivos1 = ArrayList<Double>().apply { for (xx in 0 until 4) add(0.0) }

                    for (k in 0 until 6) {
                        if (ppd[min2 ?: 0][k] <= 15.0) {
                            objetivos1[0] += 0.0
                        } else {
                            objetivos1[0] += (1.0 / 6.0) * ((15.0 - ppd[min2 ?: 0][k]) / (-85.0))
                        }
                        if (pmv[min2 ?: 0][k] == 0.0) {
                            objetivos1[1] += 0.0
                        } else {
                            objetivos1[1] += (1.0 / 6.0) * abs(pmv[min2 ?: 0][k]) / 3.0
                        }
                        if (trm[min2 ?: 0][k] in 18.0..23.0) {
                            objetivos1[2] += 0.0
                        } else {
                            val tempAux = max(18.0 - trm[min2 ?: 0][k], 23 - trm[min2 ?: 0][k])
                            objetivos1[2] += (1.0 / 6.0) * abs(tempAux) / disTT
                        }
                        objetivos1[3] = maxOf(objetivos1[0], objetivos1[1], objetivos1[2])
                    }

                    for (xx in 0 until objetivos1.size) objetivos1[xx] *= (1.0 / 5.0)

                    if (objetivos1[3] < Contadores[i][0]) {
                        for (xx in 0 until aux3.size) sol_corrida1[Contadores[i][1].toInt()][xx] =
                            aux3[xx].toInt()
                        for (xx in 0 until objetivos1.size) objetivos2[Contadores[i][1].toInt()][xx] =
                            objetivos1[xx]
                    }
                }

                sol_corrida = sol_corrida1
                objetivos = objetivos2
            }

            val tempArr =
                ArrayList<Double>().apply { for (xx in 0 until objetivos.size) add(objetivos[xx][3]) }
            val q1 = tempArr.minByOrNull { it } ?: 0.0
            val q2 = tempArr.withIndex().minByOrNull { (_, f) -> f }?.index

            val res_sol = ArrayList<Double>().apply {
                for (xx in 0 until sol_corrida[q2 ?: 0].size) add(
                    sol_corrida[q2 ?: 0][xx].toDouble()
                )
                for (xx in 0 until objetivos[q2 ?: 0].size) add(objetivos[q2 ?: 0][xx])
            }

            sol.add(res_sol)

            //FUNCIONALIDADES DE LA APP ANDROID
            detenerCronometro()
            calcularPorcentajeYTiempoRestante(r, repeticiones)
        }
        //Termina ciclo de repeticiones


        actualizaBarraDeProgreso(repeticiones, repeticiones, vbarraProgreso)
        calcularPorcentajeYTiempoRestante(repeticiones, repeticiones)
        return sol;
    }

    private fun vinculos(pa: Int, fcla: Double, v: Int, links: ArrayList<ArrayList<Double>>) {
        for (i in 0 until pa) links.add(ArrayList<Double>().apply { for (j in 0 until pa) add(0.0) })

        if (v == 1) {
            for (i in 0 until pa) {
                for (j in 1 until pa) {
                    if (i == j) {
                        links[i][j] = 1.0
                    } else {
                        if (Random.nextDouble() <= fcla) {
                            //if (1.2 <= fcla) {
                            links[i][j] = 1.0
                        } else {
                            links[i][j] = 0.0
                        }
                    }
                }
            }
        } else {
            for (i in 0 until pa) {
                for (j in i until pa) {
                    if (i == j) {
                        links[i][j] = 0.0
                    } else {
                        if (Random.nextDouble() <= fcla) {
                            //if (1.2 <= fcla) {
                            links[i][j] = (links[i][j] - 1).pow(2)
                        } else {
                            links[i][j] = links[i][j]
                        }
                    }
                }
            }
        }

        for (i in 0 until pa) {
            val aux = links[i].sum()
            if (aux == 0.0) {
                var seleccion = round(1.0 + Random.nextDouble() * (pa - 1)).toInt()
                //var seleccion = round(1.0 + 0.8 * (pa - 1)).toInt()

                while (seleccion - 1 == i) {
                    seleccion = round(1 + Random.nextDouble() * (pa - 1)).toInt()
                    //seleccion = round(1 + 0.6 * (pa - 1)).toInt()
                }
                links[i][seleccion - 1] = 1.0
            }
        }
    }

    private fun funcion_datos(
        pruebas: Int,
        temperatura: Double,
        precipitacion: Double
    ): ArrayList<ArrayList<ArrayList<Double>>> {
        var regreso = ArrayList<ArrayList<ArrayList<Double>>>()

        var a1 = TRMcancun.size
        var a2 = TRMcancun[0].size

        var distancia = ArrayList<Double>()
        distancia.add(sqrt((14.6 - temperatura).pow(2) + (275.7 - temperatura).pow(2)))
        distancia.add(sqrt((18.0 - temperatura).pow(2) + (1699.30 - temperatura).pow(2)))
        distancia.add(sqrt((28.5 - temperatura).pow(2) + (385.0 - temperatura).pow(2)))
        distancia.add(sqrt((27.3 - temperatura).pow(2) + (1300.0 - temperatura).pow(2)))

        var aux1 = distancia.minByOrNull { it } ?: 0.0
        var aux2 = distancia.maxByOrNull { it } ?: 0.0
        var diferencia = aux2 - aux1

        var diferencia1 = ArrayList<Double>()
        for (zz in 0 until distancia.size) diferencia1.add((0.01 + aux2 - distancia[zz]) / diferencia)
        val sumaTemp = diferencia1.sum()
        for (zz in 0 until diferencia1.size) diferencia1[zz] = diferencia1[zz] / sumaTemp

        var ppd = ArrayList<ArrayList<Double>>()
        for (kk in 0 until a1) {
            var arr = ArrayList<Double>()
            for (LL in 0 until a2) arr.add(0.0)
            ppd.add(arr)
        }
        var pmv = ArrayList<ArrayList<Double>>()
        for (kk in 0 until a1) {
            var arr = ArrayList<Double>()
            for (LL in 0 until a2) arr.add(0.0)
            pmv.add(arr)
        }
        var trm = ArrayList<ArrayList<Double>>()
        for (kk in 0 until a1) {
            var arr = ArrayList<Double>()
            for (LL in 0 until a2) arr.add(0.0)
            trm.add(arr)
        }

        for (p in 0 until pruebas) {

            var diferencia2 = ArrayList<Double>()
            for (q in 0 until 4) {
                diferencia2.add(diferencia1[q] + RandomAux().nextGaussian())
                //diferencia2.add(diferencia1[q] + 1.5)
            }


            val min1: Double = diferencia2.minByOrNull { it } ?: 0.0
            for (q in 0 until diferencia2.size) diferencia2[q] =
                diferencia2[q] + abs(min1) + 1.0 / 10.0
            val sumTemp = diferencia2.sum()
            for (q in 0 until diferencia2.size) diferencia2[q] = diferencia2[q] / sumTemp

            for (x in 0 until a1) {
                for (y in 0 until a2) {
                    ppd[x][y] +=
                        diferencia2[0] * PPDCDMX[x][y] + diferencia2[1] * PPDXalapa[x][y] + diferencia2[2] * PPDChihuahua[x][y] + diferencia2[3] * PPDcancun[x][y]
                    pmv[x][y] +=
                        diferencia2[0] * PMVCDMX[x][y] + diferencia2[1] * PMVXalapa[x][y] + diferencia2[2] * PMVChihuahua[x][y] + diferencia2[3] * PMVcancun[x][y]
                    trm[x][y] +=
                        diferencia2[0] * TRMCDMX[x][y] + diferencia2[1] * TRMXalapa[x][y] + diferencia2[2] * TRMChihuahua[x][y] + diferencia2[3] * TRMcancun[x][y]
                }
            }
        }

        for (x in 0 until a1) {
            for (y in 0 until a2) {
                ppd[x][y] = 1.0 / pruebas.toDouble() * ppd[x][y]
                pmv[x][y] = 1.0 / pruebas.toDouble() * pmv[x][y]
                trm[x][y] = 1.0 / pruebas.toDouble() * trm[x][y]
            }
        }

        regreso.add(ppd)
        regreso.add(pmv)
        regreso.add(trm)

        return regreso
    }


    /*
    * Lee el archivo matríz dentro de la carpeta de assets y lo regresa
    * como una matríz
    */
    private fun archivoAMatriz(ruta: String): ArrayList<ArrayList<Double>> {
        println("INICIANDO LECTURA DE $ruta")

        val matriz = ArrayList<ArrayList<Double>>()

        val lineas = main.requireContext().assets.open(ruta).bufferedReader().readLines()


        for (i in 0 until lineas.size) {
            val tokens = lineas[i].split("\t");
            val tempArray = ArrayList<Double>()
            tokens.forEach { tempArray.add(it.toDouble()) }
            matriz.add(tempArray)
        }



        return matriz
    }

    private fun actualizaBarraDeProgreso(
        progreso: Int,
        puntoFinal: Int,
        vbarraProgreso: ProgressBar
    ) {
        var progreso = (progreso.toDouble() * 100.0) / puntoFinal.toDouble()
        vbarraProgreso.progress = progreso.toInt()
    }


    private fun iniciarCronometro() {
        this.inicioCronometro = System.nanoTime()
    }

    private fun detenerCronometro() {
        this.finCronometro = System.nanoTime()
    }


    private fun calcularPorcentajeYTiempoRestante(r: Int, repeticiones: Int) {
        val porcentaje = (r.toDouble() * 100.0) / repeticiones.toDouble()
        val diferenciaDeTiempos = this.finCronometro - this.inicioCronometro
        val ciclosFaltantes = repeticiones - r
        val tiempoFaltante = ciclosFaltantes * diferenciaDeTiempos
        val tiempoFaltanteASegundos = (tiempoFaltante / (1000000000.0)).toInt()

        if (tiempoFaltanteASegundos < 60) {
            main.textoProgreso.setText("Porcentaje: ${porcentaje.toInt()}% | Tiempo: ${tiempoFaltanteASegundos}s")
        } else if (tiempoFaltanteASegundos in 60 until 60 * 60) {
            main.textoProgreso.setText("Porcentaje: ${porcentaje.toInt()}% | Tiempo: ${tiempoFaltanteASegundos / 60}m")
        } else if (tiempoFaltanteASegundos in 60 * 60 until 60 * 60 * 60) {
            main.textoProgreso.setText("Porcentaje: ${porcentaje.toInt()}% | Tiempo: ${tiempoFaltanteASegundos / 60 / 60}h")
        } else {
            main.textoProgreso.setText("Porcentaje: ${porcentaje.toInt()}% | Tiempo: ${tiempoFaltanteASegundos / 60 / 60 / 24}d")
        }
    }

}


