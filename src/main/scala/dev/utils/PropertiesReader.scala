package dev.utils

import java.io.{File, FileReader}
import java.util.Properties
import scala.util.Using

case class KeyNotFound(key: String, file: File)
    extends Exception(s"$key key not found in file: ${file.toString}")

object PropertiesReader:
  def read(key: String, file: File): String =
    Using.resource(new FileReader(file)) { reader =>
      val properties = new Properties()
      properties.load(reader)
      Option(properties.getProperty(key)).getOrElse(throw KeyNotFound(key, file))
    }
