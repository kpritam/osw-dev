package dev.utils

import dev.models.Submodule

object Logger:
  def log(submodule: Submodule, msg: String): Unit =
    println(s"${submodule.color}${submodule.name} | ${Console.RESET}$msg")

  def logGreen(msg: String): Unit = println(s"${Console.GREEN}$msg${Console.RESET}")
  def logRed(msg: String): Unit   = println(s"${Console.RED}$msg${Console.RESET}")
