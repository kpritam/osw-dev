import Main.unsafeRun
import zio.blocking.Blocking
import zio.console.{putStrLn, Console => ZConsole}
import zio.internal.Platform
import zio.process.{Command, CommandError}
import zio.stream.SubscriptionRef
import zio.{ExitCode, Fiber, URIO, ZIO}

import java.io.File

object Logger {
  def log(prefixColor: String, prefix: String, msg: String): URIO[ZConsole, Unit] =
    putStrLn(s"$prefixColor$prefix |${Console.RESET}${msg.replace("[info]", "")}")
}

object Sbt {
  private val env = Map("INTERFACE_NAME" -> "en0", "AAS_INTERFACE_NAME" -> "en0")

  def run(prefixColor: String, prefix: String, waitForOutput: String, cmd: String*)(
      wd: File
  ): ZIO[ZConsole with Blocking, CommandError, Fiber.Runtime[CommandError, ExitCode]] =
    for {
      status  <- SubscriptionRef.make(false)
      process <- Command("sbt", cmd: _*).workingDirectory(wd).env(env).run
      fiber   <- process.exitCode.fork
      _       <- ZIO.succeed(Platform.addShutdownHook(() => unsafeRun(fiber.interrupt)))
      _ <-
        process.stdout.linesStream
          .mapM { line =>
            val bool = line.contains(waitForOutput)
            Logger.log(prefixColor, prefix, line) *> status.ref.set(bool).when(bool)
          }
          .runDrain
          .forkDaemon
      _ <- status.changes.takeUntil(identity).runDrain
    } yield fiber
}

object Main extends zio.App {
  private val pwd = System.getProperty("user.dir")
  private val csw = new File(pwd + "/csw")
  private val esw = new File(pwd + "/esw")

  private val program = for {
    cswFiber <- Sbt.run(Console.YELLOW, "CSW-SERVICES", "Server online at", "csw-services/run start -c")(csw)
    eswFiber <- Sbt.run(Console.MAGENTA, "ESW-SERVICES", "Server online at", "esw-services/run start-eng-ui-services")(esw)
    _        <- eswFiber.join
    _        <- cswFiber.join
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    program.exitCode
}
