package dev.apps

import dev.utils.Git
import dev.utils.Logger

@main
def TMTRunner(cmd: String, live: String*) =
  cmd match
    case "init"              => Git.initSubmodules()
    case "update-submodules" => GitUpdateSubmodules.update()
    case "print-versions"    => Versions.prettyPrint()
    case "start"             => ServicesLauncher.launch(live.headOption.contains("--live"))
    case invalid             => Logger.logRed(s"$invalid command not supported!")
