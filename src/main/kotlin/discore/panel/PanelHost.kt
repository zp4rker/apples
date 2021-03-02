package discore.panel

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import java.util.concurrent.Executors
import kotlin.concurrent.thread

/**
 * @author zp4rker
 */
val connections = mutableListOf<SocketConnection>()

fun main() {
    embeddedServer(Netty, port = 8080) {
        routing {
            get ("/list-channels/{id}") {
                val guild = call.request.queryParameters["guild"]
                val name = call.request.queryParameters["name"]
                val request = JSONObject()
                request.put("command", "list channels")
                val options = JSONObject()
                if (guild != null) options.put("guild", guild)
                if (name != null) options.put("name", name)
                request.put("options", options)

                connections.find { it.id.toString() == call.parameters["id"] }?.apply {
                    wr.write("$request\n")
                    wr.flush()

                    val response = JSONObject(rd.readLine())
                    call.respondText(response.toString(2))
                }
            }
        }
    }.start()

    val ss = ServerSocket(6000)
    val exec = Executors.newCachedThreadPool()

    thread {
        while (true) {
            val connection = ss.accept()
            connections.add(SocketConnection(connection))
        }
    }

    println("Listening to connections on: ${ss.inetAddress.hostName}:${ss.localPort}")
}

class SocketConnection(connection: Socket, val id: UUID = UUID.randomUUID()) {
    val rd = connection.getInputStream().bufferedReader()
    val wr = connection.getOutputStream().bufferedWriter()

    init {
        println("$id has connected!")
    }
}