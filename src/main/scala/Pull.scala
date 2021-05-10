import Constants._
import zio.blocking.Blocking
import zio.console.{Console, putStrLn}
import zio.process.Command
import zio.stream.{ZStream, ZTransducer}
import zio.{ExitCode, URIO, ZIO}

import java.io.IOException
import java.nio.file.{Files, Path, Paths}

object Constants {
  private val pwd                    = System.getProperty("user.dir")
  val CswDir: Path                   = Paths.get(pwd, "/csw/project/Libs.scala")
  val EswDir: Path                   = Paths.get(pwd, "/esw")
  val EswLibsPath: Path              = Paths.get(pwd, "/esw/project/Libs.scala")
  val SequencerScriptsLibsPath: Path = Paths.get(pwd, "/sequencer-scripts/project/Libs.scala")
}

object Version {
  def readFile(path: Path): ZStream[Blocking, IOException, String] = {
    val is = Files.newInputStream(path)
    ZStream
      .fromInputStream(is)
      .aggregate(ZTransducer.utf8Decode)
      .aggregate(ZTransducer.splitLines)
  }

  def sequencerScripts: ZIO[Console with Blocking, IOException, Option[String]] =
    readFile(SequencerScriptsLibsPath)
      .takeUntil(_.contains("case _"))
      .runLast
      .map(_.flatMap(_.split("=>").tail.headOption.map(_.trim)))
}

object Git {
  def checkout(sha: String, project: String): URIO[Blocking with Console, ExitCode] =
    Command("git", "-C", project, "checkout", sha).inheritIO.run.exitCode
}

object Pull extends zio.App {

  private val program = for {
    eswVersion <- Version.sequencerScripts
    _          <- putStrLn(s"SequencerScripts Version: $eswVersion")
    version    <- ZIO.fromOption(eswVersion)
    _          <- Git.checkout(version, "esw")
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = program.exitCode
}
