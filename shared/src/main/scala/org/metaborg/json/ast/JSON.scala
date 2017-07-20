package org.metaborg.json.ast

sealed trait JSON
case class JSONObject(pairs: List[(String, JSON)]) extends JSON
case class JSONArray(values: List[JSON]) extends JSON
case class JSONString(s: String) extends JSON
case class JSONNumber[N](n: N)(implicit numeric: Numeric[N]) extends JSON
case class JSONBoolean(b: Boolean) extends JSON
case class JSONNull() extends JSON

object JSON{
  def obj(entries: List[(String, JSON)]): JSONObject = JSONObject(entries)
  def obj(entries: (String, JSON)*): JSONObject = obj(entries.toList)
  def array(values: List[JSON]): JSONArray = JSONArray(values)
  def array(values: JSON*): JSONArray = array(values.toList)
  def string(s: String): JSONString = JSONString(s)
  def num[N](n: N)(implicit numeric: Numeric[N]): JSONNumber[N] = JSONNumber(n)
  def boolean(b: Boolean): JSONBoolean = JSONBoolean(b)
  val nil = JSONNull()

  object Implicits{
    implicit def pairs2JSON[A](entries: List[(String, A)])(implicit transform: A => JSON): JSONObject = JSONObject(entries.map{
      case (k,v) => (k, transform(v))
    })
    implicit def pair2JSONPair[A](pair: (String, A))(implicit transform: A => JSON): (String, JSON) = (pair._1, transform(pair._2))
    implicit def list2JSON[A](values: List[A])(implicit transform: A => JSON): JSONArray = JSONArray(values.map(transform))
    implicit def string2JSON(s: String): JSONString = string(s)
    implicit def boolean2JSON(b: Boolean): JSONBoolean = boolean(b)
    implicit def num2JSON(n: Double): JSONNumber[Double] = num(n)
    implicit def int2JSON(i : Int): JSONNumber[Int] = num(i)
    implicit def nil2JSON(nullValue: Nothing): JSONNull = nil

    implicit class RichJSONObject(val jsonObject: JSONObject) extends AnyRef{
      def merge(entries: List[(String, JSON)]): JSONObject = obj(jsonObject.pairs ++ entries)
      def merge(entries: (String, JSON)*): JSONObject = obj(jsonObject.pairs ++ entries)
      def merge(that: JSONObject) = obj(jsonObject.pairs ++ that.pairs)
    }
  }
}