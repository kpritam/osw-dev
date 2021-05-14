package dev.utils

import dev.models.Submodule
import os.CommandResult

object Git:
  def initSubmodules(): CommandResult =
    os.proc("git", "submodule", "update", "--init", "--recursive").call(stdout = os.Inherit)

  def checkoutBranch(submodule: Submodule): CommandResult =
    os.proc("git", "-C", submodule.name, "checkout", submodule.branch)
      .call(stdout = os.ProcessOutput.Readlines(Logger.log(submodule, _)))

  def pull(submodule: Submodule): CommandResult =
    Logger.log(submodule, "Pulling latest changes ...")
    checkoutBranch(submodule)
    os.proc("git", "-C", submodule.name, "pull", "--rebase")
      .call(stdout = os.ProcessOutput.Readlines(Logger.log(submodule, _)))

  def checkout(submodule: Submodule, sha: String): CommandResult =
    val command = Seq("git", "-C", submodule.name, "checkout", sha)
    Logger.log(submodule, command.mkString(" "))
    checkoutBranch(submodule)
    os.proc(command).call(stdout = os.ProcessOutput.Readlines(Logger.log(submodule, _)))

  def head(submodule: Submodule): String =
    os.proc("git", "-C", submodule.name, "rev-parse", "--short", "HEAD").call().out.text()
