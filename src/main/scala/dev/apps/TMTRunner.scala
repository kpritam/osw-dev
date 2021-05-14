package dev.apps

import dev.utils.Git
import dev.utils.Logger

@main
def TMTRunner(cmd: String) =
  cmd match
    case "init"              => Git.initSubmodules()
    case "update-submodules" => GitUpdateSubmodules.update()
    case "print-versions"    => Versions.prettyPrint()
    case "start"             => ServicesLauncher.launch(false)
    case invalid             => Logger.logRed(s"$invalid command not supported!")
