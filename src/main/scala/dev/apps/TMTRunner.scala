package dev.apps

import dev.utils.Git
import dev.utils.Logger

import zio.cli.{Args, CliApp, Command, Exists, Options}
import zio.cli.HelpDoc
import zio.cli.HelpDoc.Span.text

import zio.*
import zio.blocking.effectBlocking
import zio.console.putStrLn

enum TMTRunnerCommand:
  case Start(live: Boolean)
  case PrintVersions
  case UpdateSubmodules
  case Init

object GitExample extends App:
  val liveFlag: Options[Boolean]         = Options.bool("live", false)
  def command(name: String, doc: String) = Command(name, Options.none, Args.none, HelpDoc.p(doc))

  val start =
    Command("start", liveFlag, Args.none, HelpDoc.p("Start CSW and ESW Services"))
      .map { case (live, _) => TMTRunnerCommand.Start(live) }

  val init = command("init", "Initializes submodules for the first time").as(TMTRunnerCommand.Init)

  val printVersions = command("print-versions", "Prints version compatibility table").as(
    TMTRunnerCommand.PrintVersions
  )

  val updateSubmodules =
    command("update-submodules", "Updates all the submodules").as(TMTRunnerCommand.UpdateSubmodules)

  val tmt = Command("tmt", Options.none, Args.none, HelpDoc.h2("TMT runner"))
    .subcommands(start | init | printVersions | updateSubmodules)
    .map(_._2)

  val app = CliApp(
    "TMT Runner application",
    "0.1.0",
    text("a client for running TMT specific services"),
    tmt,
    execute = {
      case TMTRunnerCommand.Init             => effectBlocking(Git.initSubmodules())
      case TMTRunnerCommand.UpdateSubmodules => effectBlocking(GitUpdateSubmodules.update())
      case TMTRunnerCommand.PrintVersions    => effectBlocking(Versions.prettyPrint())
      case TMTRunnerCommand.Start(live)      => effectBlocking(ServicesLauncher.launch(live))
    }
  )

  override def run(args: List[String]) = app.run(args)
