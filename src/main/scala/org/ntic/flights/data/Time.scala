package org.ntic.flights.data

case class Time(hours: Int, minutes: Int) extends Ordered[Time] {
  require(hours >= 0 && hours <= 23, "hours must be within 0 and 23")
  require(minutes >= 0 && minutes <= 59, "minutes must be within 0 and 59")
  val asMinutes: Int = hours * 60 + minutes
  override lazy val toString: String = f"$hours%02d:$minutes%02d"

  def minus(that: Time): Int =
    this.asMinutes - that.asMinutes

  def -(that: Time): Int =
    minus(that)

  override def compare(that: Time): Int =
    this - that
}

object Time {

  private val totalMinutesInADay = 1440

  /**
   * This function is used to create a Time object from a string
   * @param timeStr: String
   * @return Time
   */
  def fromString(timeStr: String): Time = {
    /*cadena con 4 caracteres (HHMM)*/
    val formatted: String = ("0000" + timeStr.trim).takeRight(4)

   /* extrae posiciones 0 y 1 para horas y convierte a int*/
    val hours: Int = formatted.substring(0, 2).toInt

    /*Extrae posiciones 2 y 3 para minutos y convierte a int*/
    val minutes: Int = formatted.substring(2, 4).toInt

    Time(hours,minutes)
  }

  def fromMinutes(minutes: Int): Time = {
    val normalized = if (minutes < 0) 0 else minutes % totalMinutesInADay
    Time(normalized / 60, normalized % 60)
  }
}