package salah

import org.json.JSONObject
import java.net.URL

/**
 * @author zp4rker
 */
data class Location(val longitude: Double, val latitude: Double) {
    companion object {
        fun getLocation(): Location {
            val url = URL("https://get.geojs.io/v1/ip/geo.json")
            val data = JSONObject(url.readText())

            return Location(data.getString("longitude").toDouble(), data.getString("latitude").toDouble())
        }
    }
}
