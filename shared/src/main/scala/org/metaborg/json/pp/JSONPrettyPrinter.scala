package org.metaborg.json.pp

import org.metaborg.json.ast._
import org.metaborg.pp.Doc

object JSONPrettyPrinter {
  import Doc._
  import Doc.Implicits._

  def ppJSON(json: JSON): Doc = json match {
    case jsonObject: JSONObject => ppJSONObject(jsonObject)
    case jsonArray: JSONArray => ppJSONArray(jsonArray)
    case jsonString: JSONString => ppJSONString(jsonString)
    case jsonNumber: JSONNumber => ppJSONNumber(jsonNumber)
    case jsonBoolean: JSONBoolean => ppJSONBoolean(jsonBoolean)
    case jsonNull: JSONNull => ppJSONNull(jsonNull)
  }

  def ppJSONObject(jsonObject: JSONObject): Doc =
    "{" $$ nest(2, sep(jsonObject.pairs.map(ppPair))) $$ "}"

  def ppPair(pair: (String, JSON)): Doc =
    "\"" <> pair._1 <> "\"" <> " : " <> ppJSON(pair._2)

  def ppJSONArray(jsonArray: JSONArray): Doc =
    "[" $$ nest(2, sep(jsonArray.values.map(ppJSON))) $$ "]"

  def ppJSONString(jsonString: JSONString): Doc =
    jsonString.s

  def ppJSONNumber(jsonNumber: JSONNumber): Doc =
    jsonNumber.n.toString

  def ppJSONBoolean(jsonBoolean: JSONBoolean) : Doc =
    if(jsonBoolean.b)
      "true"
    else
      "false"
  def ppJSONNull(jsonNull : JSONNull): Doc = "null"
}
