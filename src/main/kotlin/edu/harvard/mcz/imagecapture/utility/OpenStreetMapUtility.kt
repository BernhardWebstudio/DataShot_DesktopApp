package edu.harvard.mcz.imagecapture.utility


import edu.harvard.mcz.imagecapture.ImageCaptureApp
import edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtility
import org.apache.commons.logging.LogFactory
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.math.BigDecimal
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.function.Consumer
import kotlin.collections.HashMap
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

class OpenStreetMapUtility {
    @Throws(Exception::class)
    private fun getRequest(url: String): String? {
        val obj = URL(url)
        val con = (obj.openConnection() as HttpURLConnection)
        con.requestMethod = "GET"
        con.setRequestProperty("Content-Type", "application/json")
        // OpenStreetMap requires a personal User Agent: https://operations.osmfoundation.org/policies/nominatim/
        con.setRequestProperty("User-Agent", "Datashot/Imagecapture App " + ImageCaptureApp.getAppVersion() + ", Education, https://github.com/BernhardWebstudio/DataShot_DesktopApp/")
        val `in` = BufferedReader(InputStreamReader(con.inputStream))
        var inputLine: String?
        val response = StringBuilder()
        while (`in`.readLine().also({ inputLine = it }) != null) {
            response.append(inputLine)
        }
        `in`.close()
        if (con.responseCode != 200) {
            log!!.error("Error on request. Response code: " + con.responseCode
                    + ", message: " + con.responseMessage
                    + ", response: " + response)
            return null
        }
        con.disconnect()
        return response.toString()
    }

    /**
     * Shortcut/utility method to get the coordinates of an address
     *
     * @param address
     * @return
     */
    fun getCoordinates(address: String): MutableMap<String?, Double?> {
        val values = searchValues(address, ArrayList<String?>(Arrays.asList("lon", "lat")))
        val res: MutableMap<String?, Double?> = HashMap()
        values!!.forEach { (key: String?, value: Any?) -> res[key] = value as String?. toDouble () }
        return res
    }

    /**
     * Find an adress by a search term
     *
     * @param search the search terms, separated by space
     * @param keys
     * @return
     */
    fun searchValues(search: String, keys: MutableCollection<String?>): MutableMap<String?, Any?>? {
        val res: MutableMap<String?, Any?> = HashMap()
        var query: StringBuffer
        val split: Array<String?> = search.split(" ".toRegex()).toTypedArray()
        var url: String? = "http://nominatim.openstreetmap.org/search?q="
        if (split.size == 0) {
            return null
        }
        for (i in split.indices) {
            url += split[i]
            if (i < split.size - 1) {
                url += "+"
            }
        }
        url += "&format=json&addressdetails=1&limit=1"
        return fetchValues(url, keys)
    }

    /**
     * Find an address by its coordinates
     *
     * @param lat  the latitude of the address
     * @param lon  the longitude of the address
     * @param keys
     * @return
     */
    fun reverseSearchValues(lat: BigDecimal?, lon: BigDecimal?, keys: MutableCollection<String?>): MutableMap<String?, Any?>? {
        val url = "https://nominatim.openstreetmap.org/reverse?lat=$lat&lon=$lon&format=json&addressdetails=1"
        return fetchValues(url, keys)
    }

    /**
     * Fetch a values, specified with key, from OpenStreetMap API
     *
     * @param url  the endpoint
     * @param keys the JSONArray keys
     * @return a map of the key to its value
     */
    protected fun fetchValues(url: String?, keys: MutableCollection<String?>): MutableMap<String?, Any?>? {
        val res: MutableMap<String?, Any?> = HashMap()
        val query: StringBuffer
        var queryResult: String? = null
        query = StringBuffer()
        query.append(url)
        log!!.debug("Query:$query")
        try {
            queryResult = getRequest(query.toString())
        } catch (e: Exception) {
            log.error("Error when trying to get data with the following query $query")
            log.error(e)
        }
        if (queryResult == null) {
            return null
        }
        var obj: JSONObject? = null
        try {
            obj = JSONObject(queryResult)
        } catch (e: JSONException) {
            log.error(e)
        }
        log.debug("obj=$obj")
        if (obj != null) {
            if (obj.length() > 0) { // just take the first search result ^.^
                val finalObj: JSONObject? = obj
                keys.forEach(Consumer { key: String? -> res[key] = getValueForKeyInJson(finalObj, key!!) })
            }
        }
        return res
    }

    private fun getValueForKeyInJson(source: JSONObject, key: String): Any? {
        if (key.contains(".")) {
            val split: Array<String?> = key.split("\\.".toRegex()).toTypedArray()
            try { // rebuild all except 0th element
                val rest = StringBuilder(split[1])
                for (i in 2 until split.size) {
                    rest.append(".").append(split[i])
                }
                // use first part as key for recursion
                return getValueForKeyInJson(source.get(split[0]) as JSONObject, rest.toString())
            } catch (e: Exception) {
                log!!.error("key " + split[0] + " does not exist in JSONObject")
                log.error(e)
            }
        } else {
            try {
                return source.get(key)
            } catch (e: Exception) {
                log!!.error("key $key does not exist in JSONObject")
                log.error(e)
            }
        }
        return null
    }

    companion object {
        private val log = LogFactory.getLog(OpenStreetMapUtility::class.java)
        var instance: OpenStreetMapUtility? = null
            get() {
                if (field == null) {
                    field = OpenStreetMapUtility()
                }
                return field
            }
            private set
    }
}
