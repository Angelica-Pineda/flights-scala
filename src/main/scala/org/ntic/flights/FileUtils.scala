package org.ntic.flights

import org.ntic.flights.data.{Flight, Row}
import java.io.{FileOutputStream, ObjectOutputStream}
import scala.io.Source // Necesario para leer ficheros
import scala.util.Try

object FileUtils {

  /**
   * This function is used to check if the line is valid or not
   * @param s: String
   * @return Boolean: true if the line is invalid, false otherwise
   */
  def isInvalidLine(s: String): Boolean = {
    // Usamos el delimitador definido en tu configuración
    val delimiter = FlightsLoaderConfig.delimiter

    // El parámetro -1 en split es vital: evita que Scala descarte cadenas vacías al final de la línea
    val tokens = s.split(delimiter, -1)

    // Es inválida si está vacía o si el número de tokens no coincide con las cabeceras
    s.trim.isEmpty || tokens.length != FlightsLoaderConfig.headers.length || tokens.exists(_.trim.isEmpty)
  }

  /**
   * This function is used to read the file located in the path `filePath` and return a list of lines of the file
   *
   * @param filePath: String
   * @return List[String]
   */
  def getLinesFromFile(filePath: String): List[String] = {
    // Abrimos el fichero
    val source = Source.fromFile(filePath)
    try {
      // Leemos todas las líneas y las convertimos a una lista
      source.getLines().toList
    } finally {
      // ¡Buenas prácticas! Siempre cerramos el recurso para no dejar ficheros abiertos en el SO
      source.close()
    }
  }

  /**
   * This function is used to load the rows from the file lines
   *
   * @param fileLines: Seq[String]
   * @return Seq[Try[Row]]
   */
  def loadFromFileLines(fileLines: Seq[String]): Seq[Try[Row]] = {
    val dataLines = if (FlightsLoaderConfig.hasHeaders) {
      fileLines.drop(1) // Ignora la primera línea
    } else {
      fileLines
    }
    // Iteramos sobre cada línea (map)
    dataLines.map { line =>
      // 1. Partimos la línea en trozos (tokens) usando el delimitador
      val tokens = line.split(FlightsLoaderConfig.delimiter, -1).toSeq

      // 2. Delegamos la creación del objeto al Row.scala
      Row.fromStringList(tokens)
    }
  }

  def writeFile(flights: Seq[Flight], outputFilePath: String): Unit = {
    val out = new ObjectOutputStream(new FileOutputStream(outputFilePath))
    out.writeObject(flights)
    out.close()
  }

}