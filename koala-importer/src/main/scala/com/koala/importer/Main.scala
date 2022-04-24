package com.koala.importer
import com.koala.importer.compositions.ApplicationContextComposition.ApplicationContextCompositionEnv
import com.koala.importer.compositions.{ApplicationContextComposition, CommandLineOptionComposition}
import com.koala.importer.compositions.CommandLineOptionComposition.CommandLineOptionCompositionEnv
import com.koala.importer.models.CommandLineOptions
import com.koala.importer.services.{ApplicationContextService, CommandLineOptionService, SparkConnectionService}
import zio.cli.CliApp
import zio.cli.HelpDoc.Span.text
import zio.{ExitCode, RIO, URIO, ZEnv, ZLayer}

object Main extends zio.App {
  override def run(args: List[String]): URIO[ZEnv, ExitCode] = {
    (for{
      command <- CommandLineOptionComposition.parse().provideLayer(commandLineOptionsLayer)
      cliApp = CliApp.make("Koala Importer", "0.0.1", text("Koala Importer"), command)(execute)
      _ <- cliApp.run(args)
    } yield()).exitCode
  }

  private def execute(params: CommandLineOptions): RIO[zio.ZEnv, Unit] =
    for{
      applicationContext <- ApplicationContextComposition.build(params).provideLayer(applicationContextCompositionLayer)
    } yield ()

  private lazy val commandLineOptionsLayer: ZLayer[ZEnv, Nothing, CommandLineOptionCompositionEnv] =
    CommandLineOptionService.live >>> CommandLineOptionComposition.live

  private lazy val applicationContextCompositionLayer: ZLayer[ZEnv, Nothing, ApplicationContextCompositionEnv] =
    (SparkConnectionService.live ++ ApplicationContextService.live) >>> ApplicationContextComposition.live
}

