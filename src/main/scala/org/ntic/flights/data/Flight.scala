package org.ntic.flights.data

import org.ntic.flights.FlightsLoaderConfig

/**
 * This class is used to represent a flight with its information like the date, origin, destination, scheduled departure time,
 * scheduled arrival time, departure delay and arrival delay.
 *
 * @param flDate: String
 * @param origin: Airport
 * @param dest: Airport
 * @param scheduledDepTime: Time
 * @param scheduledArrTime: Time
 * @param depDelay: Double
 * @param arrDelay: Double
 */
case class Flight(flDate: String,
                  origin: Airport,
                  dest: Airport,
                  scheduledDepTime: Time,
                  scheduledArrTime: Time,
                  depDelay: Double,
                  arrDelay: Double) extends Ordered[Flight] {

  // define flightDate como lazy e inmutable
  lazy val flightDate: FlightDate = FlightDate.fromString(flDate)

  // Convierte la hora a minutos, suma el retraso (como Int) y reconstruye el Time.
  lazy val actualDepTime: Time = Time.fromMinutes(scheduledDepTime.asMinutes + depDelay.toInt)
  //    scheduledDepTime y depDelay
  //    Ten en cuenta que este campo debe representar la hora de salida real del vuelo, esto quiere decir que debe
  //    tener en cuenta el retraso, el campo depDelay representa el retraso en minutos, puede ser negativo y es Double.
  //    Pista: usa el méodo fromMinutes de Time

  lazy val actualArrTime: Time = Time.fromMinutes(scheduledArrTime.asMinutes + arrDelay.toInt)
  //    scheduledArrTime y arrDelay.
  //    Ten en cuenta que este campo debe representar la hora de llegada real del vuelo, esto quiere decir que debe
  //    tener en cuenta el retraso, el campo arrDelay representa el retraso en minutos, puede ser negativo y es Double.
  //    Pista: usa el méodo fromMinutes de Time

  val isDelayed: Boolean = depDelay != 0 || arrDelay != 0
  //  Pista: un vuelo está retrasado si el campo depDelay o el campo arrDelay son distintos de 0

  override def compare(that: Flight): Int =
    this.actualArrTime.compare(that.actualArrTime)


}
object Flight {
  /**
   * This function is used to create a Flight object from a string with the information of the flight separated by a
   * delimiter defined in the configuration. The function returns a Flight object with the information of the flight.
   * The input string should have the following format:
   * "FL_DATE,ORIGIN_AIRPORT_ID,ORIGIN,ORIGIN_CITY_NAME,ORIGIN_STATE_ABR,DEST_AIRPORT_ID,DEST,DEST_CITY_NAME,DEST_STATE_ABR,DEP_TIME,ARR_TIME,DEP_DELAY,ARR_DELAY"
   *
   * @param flightInfo: String
   * @return Flight
   */
  def fromString(flightInfo: String): Flight = {
    val columns = flightInfo.split(FlightsLoaderConfig.delimiter)

    /**
     * This function is used to get the value of a column from the array of String generated from the row of the csv
     * and stored in the variable `columns`.
     * @param colName: String name of the column
     * @return String value of the column
     */
    def getColValue(colName: String): String = {
      val index = FlightsLoaderConfig.columnIndexMap(colName)
      columns(index)
    }

    val oriAirport = Airport(
      airportId = getColValue("ORIGIN_AIRPORT_ID").toLong,
      code = getColValue("ORIGIN"),
      cityName = getColValue("ORIGIN_CITY_NAME"),
      stateAbr = getColValue("ORIGIN_STATE_ABR"))

    val destAirport = Airport(
      airportId = getColValue("DEST_AIRPORT_ID").toLong,
      code = getColValue("DEST"),
      cityName = getColValue("DEST_CITY_NAME"),
      stateAbr = getColValue("DEST_STATE_ABR"))

    Flight(
      flDate = getColValue("FL_DATE"),
      origin = oriAirport,
      dest = destAirport,
      scheduledDepTime = Time.fromString(getColValue("DEP_TIME")),
      scheduledArrTime = Time.fromString(getColValue("ARR_TIME")),
      depDelay = getColValue("DEP_DELAY").toDouble,
      arrDelay = getColValue("ARR_DELAY").toDouble
    )
  }

  /**
   * This function is used to create a Flight object from a Row object. The function returns a Flight object with the
   * information of the flight.
   *
   * @param row: Row
   * @return Flight
   */
  def fromRow(row: Row): Flight = {
    val originAirport = Airport(
      airportId = row.originAirportId,
      code = row.origin,
      cityName = row.originCityName,
      stateAbr = row.originStateAbr
    )

    val destAirport = Airport(
      airportId = row.destAirportId,
      code = row.dest,
      cityName = row.destCityName,
      stateAbr = row.destStateAbr
    )

    Flight(
      flDate = row.flDate,
      origin = originAirport,
      dest = destAirport,
      scheduledDepTime = Time.fromString(row.depTime),
      scheduledArrTime = Time.fromString(row.arrTime),
      depDelay = row.depDelay,
      arrDelay = row.arrDelay
    )
  }
}
