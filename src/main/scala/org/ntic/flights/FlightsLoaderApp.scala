package org.ntic.flights

import org.ntic.flights.data.{Flight, FlightsFileReport, Row}
import scala.util.Try

object FlightsLoaderApp extends App {

  val fileLines: Seq[String] = FileUtils.getLinesFromFile(FlightsLoaderConfig.filePath)
  val rows: Seq[Try[Row]] = FileUtils.loadFromFileLines(fileLines)
  val flightReport: FlightsFileReport = FlightsFileReport.fromRows(rows)
  val flights: Seq[Flight] = flightReport.flights


  FlightsLoaderConfig.filteredOrigin.foreach { originCode =>

    val flightsByOrigin = flights.filter(_.origin.code == originCode)
    val delayedFlights = flightsByOrigin.filter(_.isDelayed).sorted
    val notDelayedFlights = flightsByOrigin.filter(!_.isDelayed).sorted
    val delayedObjPath = s"${FlightsLoaderConfig.outputDir}/${originCode}_delayed.obj"
    val flightsObjPath = s"${FlightsLoaderConfig.outputDir}/$originCode.obj"

    FileUtils.writeFile(delayedFlights, delayedObjPath)
    FileUtils.writeFile(notDelayedFlights, flightsObjPath)

    println(s"Procesado $originCode: ${delayedFlights.length} retrasados, ${notDelayedFlights.length} a tiempo.")
  }


  println(flightReport)
}
