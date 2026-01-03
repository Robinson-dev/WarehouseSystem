package com.example.myapplication

fun main (){
    var resp=true
        while(resp) {
            println("--- BIENVENIDO A LA PASTELERíA ---")
            println("Elija una opcion del menu")
            println(
                "Opcion 1: Torta de tres leches $8.000 \n" +
                        "Opcion 2: Torta de Milhojas $7.000 \n" +
                        "Opcion 3: Pie de Limon $12.000\n" +
                        "Opcion 4: Torta Amor $14.000 \n" +
                        "Opcion 0: Salir del menu"
            )

            var opcion = readln().toIntOrNull()
            var precio = 0



            when (opcion) {
                1 -> {
                    println("Excelente opcion, has elejido la torta de Tres Leches")
                    precio = 8000
                }

                2 -> {
                    println("Excelente opcion, has elejido la torta de Milhojas")
                    precio = 7000
                }

                3 -> {
                    println("Excelente opcion, has elejido el pie de Limon")
                    precio = 12000
                }

                4 -> {
                    println("Excelente opcion, has elejido la torta Amor")
                    precio = 14000
                }

                else -> {
                    println("Opcion no valida...")
                    precio = 0
                }
            }var resp= readln().toIntOrNull()
//                if (resp==0){
//                    break
//                }
        }
            if (precio > 0){
           println("--- resumen de su compra ---")
           println("El precio de su compra es $precio")

           val iva = precio * 0.19
           val total_A_pagar = precio + iva
           println("IVA = (19%) $$iva")
           println("Comnpra totas es :$$total_A_pagar")
//

      }
}



