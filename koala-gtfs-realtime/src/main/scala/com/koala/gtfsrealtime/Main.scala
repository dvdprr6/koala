package com.koala.gtfsrealtime
import com.koala.gtfsrealtime.compositions.ApplicationContextComposition.ApplicationContextCompositionEnv
import com.koala.gtfsrealtime.compositions.{ApplicationContextComposition, CommandLineOptionComposition}
import com.koala.gtfsrealtime.compositions.CommandLineOptionComposition.CommandLineOptionCompositionEnv
import com.koala.gtfsrealtime.models.CommandLineOptions
import com.koala.gtfsrealtime.services.{ApplicationContextService, CommandLineOptionService, SparkConnectionService}
import zio.cli.CliApp
import zio.cli.HelpDoc.Span.text
import zio.{ExitCode, RIO, RLayer, URIO, ZEnv, ZLayer}

// REFERENCE: https://youtu.be/CDYoLQR6744

object Main extends zio.App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    (for{
      command <- CommandLineOptionComposition.parse().provideLayer(commandLineOptionsLayer)
      cliApp = CliApp.make("Koala GTFS Realtime", "0.0.1", text("Koala GTFS Realtime"), command)(execute)
      _ <- cliApp.run(args)
    } yield()).exitCode
  }

  private def execute(params: CommandLineOptions): RIO[ZEnv, Unit] =
    for{
      applicationContext <- ApplicationContextComposition.build(params).provideLayer(applicationContextLayer)
    } yield ()

  private lazy val commandLineOptionsLayer: ZLayer[ZEnv, Nothing, CommandLineOptionCompositionEnv] =
    CommandLineOptionService.live >>> CommandLineOptionComposition.live

  private lazy val applicationContextLayer: RLayer[ZEnv, ApplicationContextCompositionEnv] =
    (ApplicationContextService.live ++ SparkConnectionService.live) >>> ApplicationContextComposition.live
}
