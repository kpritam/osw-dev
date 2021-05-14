package dev.models

import os.Path

sealed trait Submodule:
  def name: String
  def dir: Path
  def buildProperties: Path
  def branch: String
  def color: String

object Submodule:
  private val wd: Path = os.pwd

  object CSW extends Submodule:
    override val name: String          = "csw"
    override val dir: Path             = wd / name
    override val buildProperties: Path = dir / "project" / "build.properties"
    override val branch: String        = "master"
    override def color: String         = Console.YELLOW

  object ESW extends Submodule:
    override val name: String          = "esw"
    override val dir: Path             = wd / name
    override val buildProperties: Path = dir / "project" / "build.properties"
    override val branch: String        = "master"
    override def color: String         = Console.CYAN

  object SequencerScripts extends Submodule:
    override val name: String          = "sequencer-scripts"
    override val dir: Path             = wd / name
    override val buildProperties: Path = dir / "project" / "build.properties"
    override val branch: String        = "ui-setup"
    override val color: String         = Console.BLUE

  object OcsEngUi extends Submodule:
    override val name: String          = "esw-ocs-eng-ui"
    override val dir: Path             = wd / name
    override val buildProperties: Path = dir / "project" / "build.properties"
    override val branch: String        = "main"
    override val color: String         = Console.MAGENTA
