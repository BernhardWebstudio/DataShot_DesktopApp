/**
 * StreamReader.java
 * edu.harvard.mcz.imagecapture
 */
package edu.harvard.mcz.imagecapture


import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Reads an InputStream in a separate thread.
 */
class StreamReader internal constructor(var inputStream: InputStream?, var type: String?) : Runnable {
    override fun run() {
        try {
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var line: String? = null
            while (bufferedReader.readLine().also({ line = it }) != null) {
                println("$type>$line")
            }
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }
    }

}
