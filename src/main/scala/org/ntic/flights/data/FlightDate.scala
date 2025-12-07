package org.ntic.flights.data

/**
 * This class is used to represent a date of a flight
 * @param day: Int
 * @param month: Int
 * @param year: Int
 */
case class FlightDate(day: Int,
                      month: Int,
                      year: Int) {
/*override de toString */
  override lazy val toString: String = f"$day%02d/$month%02d/$year"
}

object FlightDate {
  /**
   * This function is used to convert a string to a FlightDate
   * @param date: String
   * @return FlightDate
   */
  def fromString(date: String): FlightDate = {
    // extrae la fecha `mm/dd/yyyy` y define un patrón que te permita extraer el mes, día y año
    // para hacer la validación.
    // Pista: usa `split`
    // Pista: obtén una lista de Int
    val dateparts = date.split(" ")(0).split("/").map(_.toInt).toList

    dateparts match {
      case List(month, day, year) =>
        // Validaciones requeridas
        // assert lanza un java.lang.AssertionError si la condición es falsa
        assert(year >= 1987, "Año invalido: Año deberia ser >= 1987")
        assert(month >= 1 && month <= 12, "Mes invalido")
        assert(day >= 1 && day <= 31, "Dia invalido")

        // devolvemos el objeto
        FlightDate(day, month, year)
      //Si el formato no coincide con una lista de 3 números, lanzamos error
      case _ => throw new Exception(s"$date tiene un formato inválido")
    }
  }
}
