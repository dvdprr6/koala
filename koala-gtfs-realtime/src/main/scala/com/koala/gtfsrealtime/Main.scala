package com.koala.gtfsrealtime
import com.google.protobuf.util.JsonFormat
import com.google.transit.realtime.GtfsRealtime
import com.koala.gtfsrealtime.compositions.ApplicationContextComposition.ApplicationContextCompositionEnv
import com.koala.gtfsrealtime.compositions.{ApplicationContextComposition, CommandLineOptionComposition, HttpComposition}
import com.koala.gtfsrealtime.compositions.CommandLineOptionComposition.CommandLineOptionCompositionEnv
import com.koala.gtfsrealtime.compositions.HttpComposition.HttpCompositionEnv
import com.koala.gtfsrealtime.models.CommandLineOptions
import com.koala.gtfsrealtime.services.{ApplicationContextService, CommandLineOptionService, HttpService, SparkConnectionService}
import zio.cli.CliApp
import zio.cli.HelpDoc.Span.text
import zio.console.putStrLn
import zio.{ExitCode, RIO, RLayer, URIO, ZEnv, ZLayer}

// REFERENCE: https://medium.com/geekculture/how-to-execute-a-rest-api-call-on-apache-spark-the-right-way-in-python-4367f2740e78
// REFERENCE: https://github.com/jamesshocking/Spark-REST-API-UDF-Scala
object Main extends zio.App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    //val gtfs = GtfsRealtime.TripUpdate

    (for{
      command <- CommandLineOptionComposition.parse().provideLayer(commandLineOptionsLayer)
      cliApp = CliApp.make("Koala GTFS Realtime", "0.0.1", text("Koala GTFS Realtime"), command)(execute)
      _ <- cliApp.run(args)
    } yield()).exitCode
  }

  private def execute(params: CommandLineOptions): RIO[ZEnv, Unit] = {
    for{
      applicationContext <- ApplicationContextComposition.build(params).provideLayer(applicationContextLayer)
      body <- HttpComposition.getRequest(applicationContext).provideCustomLayer(httpContextLayer)
      parseBody = GtfsRealtime.TripUpdate.parser().parseFrom(body)
      _ <- putStrLn(JsonFormat.printer().print(parseBody))
      //_ <- putStrLn(parseBody.getVehicle.getId)
    } yield ()
  }

  private lazy val commandLineOptionsLayer: ZLayer[ZEnv, Nothing, CommandLineOptionCompositionEnv] =
    CommandLineOptionService.live >>> CommandLineOptionComposition.live

  private lazy val applicationContextLayer: RLayer[ZEnv, ApplicationContextCompositionEnv] =
    (ApplicationContextService.live ++ SparkConnectionService.live) >>> ApplicationContextComposition.live

  private lazy val httpContextLayer: RLayer[ZEnv, HttpCompositionEnv] =
    HttpService.live >>> HttpComposition.live
}
