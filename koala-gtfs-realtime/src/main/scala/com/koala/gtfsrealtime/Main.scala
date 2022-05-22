package com.koala.gtfsrealtime
import com.koala.gtfsrealtime.compositions.CommandLineOptionComposition
import com.koala.gtfsrealtime.compositions.CommandLineOptionComposition.CommandLineOptionCompositionEnv
import com.koala.gtfsrealtime.models.CommandLineOptions
import com.koala.gtfsrealtime.services.CommandLineOptionService
import zio.cli.CliApp
import zio.cli.HelpDoc.Span.text
import zio.{ExitCode, RIO, URIO, ZEnv, ZLayer}

object Main extends zio.App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    (for{
      command <- CommandLineOptionComposition.parse().provideLayer(commandLineOptionsLayer)
      cliApp = CliApp.make("Koala GTFS Realtime", "0.0.1", text("Koala GTFS Realtime"), command)(execute)
      _ <- cliApp.run(args)
    } yield()).exitCode
  }

  private def execute(params: CommandLineOptions): RIO[ZEnv, Unit] = ???

  private lazy val commandLineOptionsLayer: ZLayer[ZEnv, Nothing, CommandLineOptionCompositionEnv] =
    CommandLineOptionService.live >>> CommandLineOptionComposition.live
}
