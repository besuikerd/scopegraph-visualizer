package org.metaborg.util

class RichString(val s: String) extends AnyVal {
  def unquote = s.replaceAll("^\"|\"$", "")
}
