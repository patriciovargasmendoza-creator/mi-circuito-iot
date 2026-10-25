package cl.inacap.myapplication.util

/**
 * Utilidades para formatear y validar RUT chileno de manera ordenada y consistente.
 */
object RutUtils {

    /**
     * Formatea una cadena de texto en formato RUT chileno: XX.XXX.XXX-Y
     * Permite ingresar números y la letra K/k al final.
     */
    fun format(input: String): String {
        // Filtrar sólo dígitos y k/K
        val raw = input.filter { it.isDigit() || it == 'k' || it == 'K' }
        if (raw.isEmpty()) return ""

        // Sanitizar: 'K' sólo se permite como último carácter (dígito verificador)
        val clean = StringBuilder()
        for (i in raw.indices) {
            val char = raw[i]
            if (char.isDigit()) {
                clean.append(char)
            } else if ((char == 'k' || char == 'K') && i == raw.length - 1) {
                clean.append('K')
            }
        }

        // Máximo 9 caracteres limpios (8 cuerpo + 1 DV)
        val cleanStr = clean.toString().take(9)
        if (cleanStr.length <= 1) return cleanStr.uppercase()

        val body = cleanStr.dropLast(1)
        val dv = cleanStr.last().uppercaseChar()

        val formattedBody = StringBuilder()
        var count = 0
        for (i in body.length - 1 downTo 0) {
            formattedBody.append(body[i])
            count++
            if (count == 3 && i != 0) {
                formattedBody.append('.')
                count = 0
            }
        }
        return "${formattedBody.reverse()}-$dv"
    }

    /**
     * Retorna el RUT limpio sin puntos ni guión, en mayúsculas (ej: "111111111" o "12345678K")
     */
    fun clean(input: String): String {
        return input.replace(".", "").replace("-", "").trim().uppercase()
    }

    /**
     * Verifica si el RUT tiene un formato válido (entre 8 y 9 caracteres limpios)
     */
    fun isValidFormat(input: String): Boolean {
        val c = clean(input)
        return c.length in 8..9
    }

    /**
     * Cuenta los caracteres válidos de un RUT (dígitos y K).
     */
    fun contarValidos(texto: String): Int = texto.count { it.isDigit() || it == 'k' || it == 'K' }

    /**
     * Posición del cursor en el RUT formateado, justo después del N-ésimo carácter válido.
     * Permite que el cursor no salte al final al escribir o borrar en medio del número.
     */
    fun cursorTras(formateado: String, cantidadValidos: Int): Int {
        if (cantidadValidos <= 0) return 0
        var conteo = 0
        for ((i, c) in formateado.withIndex()) {
            if (c.isDigit() || c == 'K') {
                conteo++
                if (conteo == cantidadValidos) return i + 1
            }
        }
        return formateado.length
    }
}
