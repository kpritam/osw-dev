package dev.utils

import dev.models.Submodule
import os.SubProcess

object Sbt:
  private val sbt = "sbt"

  def run(submodule: Submodule, waitForOutput: String, cmd: String*): Option[SubProcess] =
    Logger.logGreen(s"========= Starting ${submodule.name}-services =========")
    @volatile var finished = false

    val process =
      os.proc(sbt, cmd)
        .spawn(
          env = Environment.get,
          cwd = submodule.dir,
          stdout = os.ProcessOutput.Readlines { line =>
            Logger.log(submodule, line)
            if line.contains(waitForOutput) then finished = true
          }
        )

    sys.addShutdownHook(process.destroyForcibly())
    while !finished && process.isAlive() do Thread.sleep(100)
    if process.isAlive() then Some(process) else None
