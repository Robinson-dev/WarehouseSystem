package cl.arriagada.todoapp

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

const val FILE_NAME = "todolist.dat"

fun writeData(items: SnapshotStateList<String>, context: Context) {
    // fos significa file output stream: abre el archivo para escribir
    val fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
    // oas significa object output stream: convierte objetos a bytes para guardarlos
    val oas = ObjectOutputStream(fos)
    // crea una lista normal porque objectoutputstream no entiende snapshotstatelist
    val itemList = ArrayList<String>()
    // copia todas las tareas a la lista normal
    itemList.addAll(items)
    // guarda la lista en el archivo
    oas.writeObject(itemList)
    // cierra el archivo
    oas.close()
}

fun readData(context: Context): SnapshotStateList<String> {
    // variable donde guardaremos las tareas leidas del archivo
    var itemList: ArrayList<String>

    try {
        // fis significa file input stream: abre el archivo para leer
        val fis = context.openFileInput(FILE_NAME)
        // ois significa object input stream: convierte bytes a objetos
        val ois = ObjectInputStream(fis)
        // lee el archivo y lo convierte a arraylist de strings
        itemList = ois.readObject() as ArrayList<String>
    } catch (e: FileNotFoundException) {
        // si el archivo no existe, crea una lista vacia
        itemList = ArrayList()
    }

    // crea una snapshotstatelist vacia
    val items = SnapshotStateList<String>()
    // copia todas las tareas del arraylist a la snapshotstatelist
    items.addAll(itemList)

    // devuelve la lista para usarla en compose
    return items
}