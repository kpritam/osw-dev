package dev.apps

import dev.models.Submodule.{CSW, ESW, SequencerScripts}
import dev.utils.{Git, PropertiesReader, Tabulator}

object Versions:
  def eswVersion: String =
    PropertiesReader.read("esw.version", SequencerScripts.buildProperties.toIO).trim
  def cswVersion: String              = PropertiesReader.read("csw.version", ESW.buildProperties.toIO).trim
  def sequencerScriptsVersion: String = Git.head(SequencerScripts).trim

  def tabulatedVersions: String =
    Tabulator.formatTable(
      List(
        List("Module", "Branch", "Version"),
        List(SequencerScripts.name, SequencerScripts.branch, sequencerScriptsVersion),
        List(ESW.name, ESW.branch, eswVersion),
        List(CSW.name, CSW.branch, cswVersion)
      )
    )

  def prettyPrint(): Unit =
    println(Console.YELLOW)
    println(tabulatedVersions)
    println(Console.RESET)
