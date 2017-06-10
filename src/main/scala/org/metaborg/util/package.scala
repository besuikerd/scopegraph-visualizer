package org.metaborg

package object util {

  object Implicits{
    @inline implicit def richString(s: String) = new RichString(s)
  }
}
