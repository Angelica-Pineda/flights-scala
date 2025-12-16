package org.ntic.flights.data

import scala.util.Try

/**
 * This class is used to represent a row of the flights data. It contains the following fields:
 * @param flDate: String
 * @param originAirportId: Long
 * @param origin: String
 * @param originCityName: String
 * @param originStateAbr: String
 * @param destAirportId: Long
 * @param dest: String
 * @param destCityName: String
 * @param destStateAbr: String
 * @param depTime: String
 * @param depDelay: Double
 * @param arrTime: String
 * @param arrDelay: Double
 */
case class Row (
                 flDate: String,
                 originAirportId: Long,
                 origin: String,
                 originCityName: String,
                 originStateAbr: String,
                 destAirportId: Long,
                 dest: String,
                 destCityName: String,
                 destStateAbr: String,
                 depTime: String,
                 depDelay: Double,
                 arrTime: String,
                 arrDelay: Double
               )

object Row {
  /**
   * This method is used to create a Row object from a list of tokens. It returns a Try[Row] object.
   * If the list of tokens is not valid or any of the token is invalid, it returns a Failure object. Otherwise, it returns a Success object.
   *
   * @param tokens: Seq[String]
   * @return Try[Row]
   */
  def fromStringList(tokens: Seq[String]): Try[Row] = {

    Try {
      val depTimeStr = tokens(9).trim
      val arrTimeStr = tokens(11).trim

      Time.fromString(depTimeStr)
      Time.fromString(arrTimeStr)

      Row(
        flDate = tokens(0).trim,
        originAirportId = tokens(1).trim.toLong,
        origin = tokens(2).trim,
        originCityName = tokens(3).trim,
        originStateAbr = tokens(4).trim,
        destAirportId = tokens(5).trim.toLong,
        dest = tokens(6).trim,
        destCityName = tokens(7).trim,
        destStateAbr = tokens(8).trim,
        depTime = depTimeStr,
        depDelay = tokens(10).trim.toDouble,
        arrTime = arrTimeStr,
        arrDelay = tokens(12).trim.toDouble
      )
    }

  }
}