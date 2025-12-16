package org.ntic.flights.data


import scala.util.{Failure, Success, Try}
import scala.io.Source
import scala.util.Try

/**
 * This class is used to represent a report of the flights file with the valid rows, invalid rows and the flights
 * extracted from the valid rows.
 * @param validRows: Seq[Row]
 * @param invalidRows: Seq[String]
 * @param flights: Seq[Flight]
 */
case class FlightsFileReport(validRows: Seq[Row],
                         invalidRows: Seq[String],
                         flights: Seq[Flight]
                        ) {

  override val toString: String = {

    val errorSummary = invalidRows
      .groupBy(identity)
      .map { case (errorMsg, list) => s"  <$errorMsg>: ${list.length}" }
      .mkString("\n")


    s"""
       |FlightsReport:
       |  - ${validRows.length} valid rows.
       |  - ${invalidRows.length} invalid rows.
       |Error summary:
       |$errorSummary
       |""".stripMargin
  }
}

object FlightsFileReport {
  /**
   * This function is used to create a FlightsFileReport from a list of Try[Row] objects where each Try[Row] represents a row
   * loaded from the file. If the row is valid, it is added to the validRows list, otherwise the error message is added to
   * the invalidRows list. Finally, the valid rows are converted to Flight objects and added to the flights list.
   *
   * @param rows: Seq[Try[Row]]
   * @return FlightsFileReport
   */
  def fromRows(rows: Seq[Try[Row]]): FlightsFileReport = {
    // filas válidas
    val validRows = rows.collect { case Success(row) => row }

    // filas invalidas para el reporte
    val invalidRows = rows.collect { case Failure(e) => e.toString }

    // Convierte las filas válidas a objetos Flight
    val flights = validRows.map(Flight.fromRow)

    // Objeto Reporte
    FlightsFileReport(validRows, invalidRows, flights)
  }
}
