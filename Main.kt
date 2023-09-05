package encryptdecrypt
import java.io.File

val abc = "abcdefghijklmnopqrstuvwxyz"
val ABC = "ABCDEFGHIGKLMNOPQRSTUVWXYZ"

fun main(args: Array<String>) {
    if (!File(args[args.indexOf("-in") + 1]).exists()) {
        println("Error. File doesn't exist")
    } else if (args.size % 2 != 0) {
        println("Error. Missing argument")
    }
    val mode = if (args.contains("-mode")) args[args.indexOf("-mode") + 1] else "enc"
    val key = if (args.contains("-key")) args[args.indexOf("-key") + 1].toInt() else 0
    val data = if(args.contains("-data")) {
        args[args.indexOf("-data") + 1]
    } else if(args.contains("-in")) {
        File(args[args.indexOf("-in") + 1]).readText()
    } else {
        ""
    }
    if(!args.contains("-out")) {
        println(data)
    } else {
        when (mode) {
            "enc" -> encrypt(data, key, args)
            "dec" -> decrypt(data, key, args)
        }
    }
}

fun encrypt(data: String, key: Int, args: Array<String>) {
    var encodedStr = ""
    if (args.contains("-alg") && args[args.indexOf("-alg") + 1] == "unicode") {
        for (i in data) encodedStr += (i.code + key).toChar()
    } else {
        for (i in data) {
            encodedStr += if (i in abc) {
                abc[(abc.indexOf(i) + key) % 26]
            } else if (i in ABC) {
                ABC[(ABC.indexOf(i) + key) % 26]
            } else i
        }
    }
    File(args[args.indexOf("-out") + 1]).writeText(encodedStr)
}

fun decrypt(data: String, key: Int, args: Array<String>) {
    var decodedStr = ""
    if (args.contains("-alg") && args[args.indexOf("-alg") + 1] == "unicode") {
        for (i in data) decodedStr += (i.code - key).toChar()
    } else {
        for (i in data) {
            decodedStr += if (i in ABC) {
                (i.code - key + 26).toChar()
            } else if (i in abc) {
                if (i - key < 'a') abc[26 - (key - abc.indexOf(i))] else i - key
            } else {
                i
            }
        }
    }
    File(args[args.indexOf("-out") + 1]).writeText(decodedStr)
}

