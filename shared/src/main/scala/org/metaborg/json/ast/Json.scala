package org.metaborg.json.ast

sealed trait JSON
case class JSONObject(pairs: List[(String, JSON)]) extends JSON
case class JSONArray(values: List[JSON]) extends JSON
case class JSONString(s: String) extends JSON
case class JSONNumber(n: Double) extends JSON
case class JSONBoolean(b: Boolean) extends JSON
case class JSONNull() extends JSON

object JSON{
  def obj(entries: List[(String, JSON)]): JSONObject = JSONObject(entries)
  def array(values: List[JSON]): JSONArray = JSONArray(values)
  def string(s: String): JSONString = JSONString(s)
  def num(n: Double): JSONNumber = JSONNumber(n)
  def boolean(b: Boolean): JSONBoolean = JSONBoolean(b)
  val nil = JSONNull()

  object Implicits{
    implicit def pairs2JSON[A](entries: List[(String, A)])(implicit transform: A => JSON): JSONObject = JSONObject(entries.map{
      case (k,v) => (k, transform(v))
    })
    implicit def list2JSON[A](values: List[A])(implicit transform: A => JSON): JSONArray = JSONArray(values.map(transform))
    implicit def string2JSON(s: String): JSONString = string(s)
    implicit def boolean2JSON(b: Boolean): JSONBoolean = boolean(b)
    implicit def num2JSON(n: Double): JSONNumber = num(n)
    implicit def int2JSON(i : Int): JSONNumber = num(i)
    implicit def nil2JSON(nullValue: Nothing): JSONNull = nil
  }
}