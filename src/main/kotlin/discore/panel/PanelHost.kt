package discore.panel

import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors
import kotlin.concurrent.thread
import kotlin.system.exitProcess

/**
 * @author zp4rker
 */
val connections = mutableListOf<SocketRunnable>()

fun main() {
    val ss = ServerSocket(6000)
    val exec = Executors.newCachedThreadPool()

    thread {
        run@ while (true) {
            val connection = ss.accept()
            exec.submit(SocketRunnable(connection))
        }
    }

    println("Listening to connections on: ${ss.inetAddress.hostName}:${ss.localPort}")
}

class SocketRunnable(private val connection: Socket) : Runnable {
    private val rd = connection.getInputStream().bufferedReader()
    private val wr = connection.getOutputStream().bufferedWriter()

    init {
        connections.add(this)
    }

    override fun run() {
        while (true) {
            var line = rd.readLine()
            while (line != null) {
                if (line != "quit") {
                    connections.filter { it != this }.forEach { it.sendMessage("[${connection.let { c -> "${c.inetAddress.hostName}:${c.port}" }}]: $line") }
                    sendMessage("[You]: $line")
                    println("Received message from ${connection.let { "${it.inetAddress.hostName}:${it.port}" }}")
                    line = rd.readLine()
                } else {
                    println("Closing server...")
                    exitProcess(0)
                }
            }
        }
    }

    fun sendMessage(message: String) {
        wr.write("$message\n")
        wr.flush()
    }
}