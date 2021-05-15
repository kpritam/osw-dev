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
  val liveFlag: Options[Boolean] = Options.bool("live", false)

  val start =
    Command("start", liveFlag, Args.none, HelpDoc.Paragraph(text("Start CSW and ESW Services")))
      .map { case (live, _) => TMTRunnerCommand.Start(live) }

  val init = Command(
    "init",
    Options.none,
    Args.none,
    HelpDoc.Paragraph(text("Initializes submodules for the first time"))
  ).map(_ => TMTRunnerCommand.Init)

  val printVersions = Command(
    "print-versions",
    Options.none,
    Args.none,
    HelpDoc.Paragraph(text("Prints versions compatibility table"))
  ).map(_ => TMTRunnerCommand.PrintVersions)

  val updateSubmodules = Command(
    "update-submodules",
    Options.none,
    Args.none,
    HelpDoc.Paragraph(text("Updates all the submodules"))
  ).map(_ => TMTRunnerCommand.UpdateSubmodules)

  val tmt = Command("tmt", Options.none, Args.none, HelpDoc.Header(text("TMT runner"), 2))
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
