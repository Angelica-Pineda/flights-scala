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
    // Agrupar errores por identity
    val errorSummary = invalidRows
      .groupBy(identity)
      .map { case (errorMsg, list) => s"  <$errorMsg>: ${list.length}" }
      .mkString("\n")

    // 2. Construimos el string final usando interpolación y multilínea (""")
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
    // 1. Extraemos las filas válidas (Success)
    val validRows = rows.collect { case Success(row) => row }

    // 2. Extraemos los errores (Failure) y los convertimos a String para el reporte
    // Usamos e.toString para tener el tipo de excepción y el mensaje (ej: "java.lang.NumberFormatException: For input string: 'A'")
    val invalidRows = rows.collect { case Failure(e) => e.toString }

    // 3. Convertimos las filas válidas a objetos Flight usando el método fromRow que creamos antes
    val flights = validRows.map(Flight.fromRow)

    // 4. Devolvemos el objeto reporte
    FlightsFileReport(validRows, invalidRows, flights)
  }
}
