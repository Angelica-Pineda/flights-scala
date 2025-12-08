package org.ntic.flights

import org.ntic.flights.data.{Flight, FlightsFileReport, Row}
import scala.util.Try

object FlightsLoaderApp extends App {
  // 1. Carga de datos
  // Usamos la configuración para saber dónde está el archivo
  val fileLines: Seq[String] = FileUtils.getLinesFromFile(FlightsLoaderConfig.filePath)
  // Transformamos las líneas de texto en intentos de filas (Try[Row])
  val rows: Seq[Try[Row]] = FileUtils.loadFromFileLines(fileLines)
  // 2. Generación del Reporte
  // Separamos válidos de inválidos automáticamente
  val flightReport: FlightsFileReport = FlightsFileReport.fromRows(rows)
  // Obtenemos la lista limpia de vuelos para trabajar
  val flights: Seq[Flight] = flightReport.flights

  // 3. Procesamiento y Exportación
  // Iteramos sobre la lista de aeropuertos que queremos filtrar (definida en reference.conf)
  FlightsLoaderConfig.filteredOrigin.foreach { originCode =>

    // a. Filtrar los vuelos por origen
    val flightsByOrigin = flights.filter(_.origin.code == originCode)

    // b. Separar vuelos retrasados y NO retrasados
    // Además, los ordenamos (.sorted) usando la lógica que definimos en Flight (por hora de llegada real)
    val delayedFlights = flightsByOrigin.filter(_.isDelayed).sorted
    val notDelayedFlights = flightsByOrigin.filter(!_.isDelayed).sorted

    // c. Definir nombres de archivos de salida
    // Construimos la ruta: outputDir + CODIGO + sufijo + .obj
    val delayedObjPath = s"${FlightsLoaderConfig.outputDir}/${originCode}_delayed.obj"
    val flightsObjPath = s"${FlightsLoaderConfig.outputDir}/$originCode.obj"

    // d. Escribir los archivos binarios
    // Solo escribimos si hay vuelos, aunque el enunciado implica hacerlo siempre.
    // Usamos FileUtils para guardar la lista de objetos.
    FileUtils.writeFile(delayedFlights, delayedObjPath)
    FileUtils.writeFile(notDelayedFlights, flightsObjPath)

    println(s"Procesado $originCode: ${delayedFlights.length} retrasados, ${notDelayedFlights.length} a tiempo.")
  }

  // 4. Imprimir el reporte final por consola
  println(flightReport)
}
