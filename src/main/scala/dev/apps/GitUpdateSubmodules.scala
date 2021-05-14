package dev.apps

import dev.models.Submodule.{CSW, ESW, OcsEngUi, SequencerScripts}
import dev.utils.Git

object GitUpdateSubmodules:
  def update(): Unit =
    Git.pull(SequencerScripts)
    Git.pull(OcsEngUi)
    Git.checkout(ESW, Versions.eswVersion)
    Git.checkout(CSW, Versions.cswVersion)
