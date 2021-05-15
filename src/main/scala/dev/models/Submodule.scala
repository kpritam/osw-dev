package dev.models

import os.Path

enum Submodule(val name: String, val branch: String, val color: String):
  private final val wd: Path = os.pwd
  def dir: Path              = wd / name
  def buildProperties: Path  = dir / "project" / "build.properties"

  case CSW              extends Submodule("csw", "master", Console.YELLOW)
  case ESW              extends Submodule("esw", "master", Console.CYAN)
  case SequencerScripts extends Submodule("sequencer-scripts", "ui-setup", Console.BLUE)
  case OcsEngUi         extends Submodule("esw-ocs-eng-ui", "main", Console.MAGENTA)
