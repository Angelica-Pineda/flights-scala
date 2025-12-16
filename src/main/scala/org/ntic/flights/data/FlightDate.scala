package org.ntic.flights.data

import com.sun.media.sound.InvalidFormatException

/**
 * This class is used to represent a date of a flight
 *
 * @param day: Int
 * @param month: Int
 * @param year: Int
 */
case class FlightDate(day: Int,
                      month: Int,
                      year: Int) {

  override lazy val toString: String = f"$day%02d/$month%02d/$year"
}

object FlightDate {
  /**
   * This function is used to convert a string to a FlightDate
   * @param date: String
   * @return FlightDate
   */
  def fromString(date: String): FlightDate = {

    val dateparts = date.split(" ")(0).split("/").map(_.toInt).toList

    dateparts match {
      case List(month, day, year) =>

        assert(year >= 1987, "Año invalido: Año deberia ser >= 1987")
        assert(month >= 1 && month <= 12, "Mes invalido")
        assert(day >= 1 && day <= 31, "Dia invalido")


        FlightDate(day, month, year)

      case _ => throw new InvalidFormatException(s"$date tiene un formato inválido")
    }
  }
}
