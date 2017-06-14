package org.metaborg.json.pp

import org.metaborg.json.ast._
import org.metaborg.pp.Doc

object JSONPrettyPrinter {
  import Doc._
  import Doc.Implicits._
  import org.metaborg.util.Implicits._

  def ppJSON(json: JSON): Doc = json match {
    case jsonObject: JSONObject => ppJSONObject(jsonObject)
    case jsonArray: JSONArray => ppJSONArray(jsonArray)
    case jsonString: JSONString => ppJSONString(jsonString)
    case jsonNumber: JSONNumber[_] => ppJSONNumber(jsonNumber)
    case jsonBoolean: JSONBoolean => ppJSONBoolean(jsonBoolean)
    case jsonNull: JSONNull => ppJSONNull(jsonNull)
  }

  def ppJSONObject(jsonObject: JSONObject): Doc =
    if(jsonObject.pairs.isEmpty)
      "{}"
    else
      "{" <> newline <> nest(2, group(jsonObject.pairs.map(ppPair), "," <> newline)) <> newline <> "}"

  def ppPair(pair: (String, JSON)): Doc =
    "\"" <> pair._1 <> "\"" <> ": " <> ppJSON(pair._2)

  def ppJSONArray(jsonArray: JSONArray): Doc =
    if(jsonArray.values.isEmpty)
      "[]"
    else
      "[" <> newline <> nest(2, group(jsonArray.values.map(ppJSON), "," <> newline)) <> newline <> "]"

  def ppJSONString(jsonString: JSONString): Doc =
    "\"" <> jsonEscape(jsonString.s) <> "\""

  val jsonEscapeRegex = """(["\\])""".r

  def jsonEscape(s: String): String =
    jsonEscapeRegex.replaceAllIn(s, """\\$1""")


  def ppJSONNumber(jsonNumber: JSONNumber[_]): Doc =
    jsonNumber.n.toString

  def ppJSONBoolean(jsonBoolean: JSONBoolean) : Doc =
    if(jsonBoolean.b)
      "true"
    else
      "false"
  def ppJSONNull(jsonNull : JSONNull): Doc = "null"
}
