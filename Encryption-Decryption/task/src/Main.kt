package encryptdecrypt

import java.io.File

enum class Flag(val flag: String) {
    MODE("-mode"),
    KEY("-key"),
    DATA("-data"),
    IN("-in"),
    OUT("-out"),
    ALG("-alg"),
}



fun findModeKeyData(data: Array<String>): Array<String> {
    val result = arrayOf("enc", "0", "", "", "","unicode")

    for (i in data.indices) {

        when (data[i]) {
            Flag.DATA.flag -> {
                if (i <= data.lastIndex) {

                    result[2] += data[i + 1]
                    if (result[2].last() !== '\"') {
                        Loop@ for (x in i + 2..data.lastIndex) {
                            if (data[x].first() == '-') break@Loop
                            result[2] += " " + data[x]

                        }
                    }
                    result[2] = result[2].replaceFirst("\"", "").removeSuffix("\"")
                }
            }
            Flag.KEY.flag -> {
                if (i <= data.lastIndex) result[1] = data[i + 1]

            }
            Flag.MODE.flag -> {
                if (i <= data.lastIndex) result[0] = data[i + 1]
            }
            Flag.IN.flag -> {
                if (i <= data.lastIndex) result[3] = data[i + 1]
            }
            Flag.OUT.flag -> {
                if (i <= data.lastIndex) result[4] = data[i + 1]
            }
            Flag.ALG.flag -> {
                if (i <= data.lastIndex) result[5] = data[i + 1]
            }

        }


    }
    return result
}

fun main(args: Array<String>) {


    val argument = findModeKeyData(args)
    val path = System.getProperty("user.dir")
    val separator = File.separator
    var cipherMessage = ""
    try {
        if (argument[3].isNotEmpty())
            argument[2] = File("$path$separator${argument[3]}").readText()

    } catch (e: Exception) {
        println("Error $e")
    }

    if (argument[0] == "enc") {

        for (i in argument[2]) {
            cipherMessage += if (argument[5]=="unicode") (i.code + argument[1].toInt()).toChar()
            else {
                if(i.code in 65..90){
                    if(i.code + argument[1].toInt() > 90){
                        (i.code + argument[1].toInt() - 90 + 65 - 1).toChar()
                    } else  (i.code + argument[1].toInt()).toChar()

                } else if(i.code in 97..122){
                    if(i.code + argument[1].toInt() > 122){
                        (i.code + argument[1].toInt() - 122 + 97  -1 ).toChar()
                    } else  (i.code + argument[1].toInt()).toChar()
                } else{
                    i
                }
            }

        }

    } else if (argument[0] == "dec") {
        for (i in argument[2]) {
            cipherMessage += if (argument[5]=="unicode") (i.code - argument[1].toInt()).toChar()
            else {
                if(i.code in 65..90){
                    if(i.code - argument[1].toInt() < 65){
                        (90 - (65 - (i.code - argument[1].toInt())) + 1).toChar()
                    } else  (i.code - argument[1].toInt()).toChar()

                } else if(i.code in 97..122){
                    if(i.code - argument[1].toInt() < 97){
                        (122 - (97 - (i.code - argument[1].toInt())) + 1).toChar()
                    } else  (i.code - argument[1].toInt()).toChar()
                } else{
                    i
                }
            }
        }
    }



    if(argument[4].isNotEmpty()){
        try { File("$path$separator${argument[4]}").writeText(cipherMessage)

        }catch (e: Exception){
            println("Error $e")
        }

    } else {
        println(cipherMessage)
    }



}