package com.koala.importer
import com.koala.importer.compositions.ApplicationContextComposition.ApplicationContextCompositionEnv
import com.koala.importer.compositions.{ApplicationContextComposition, CommandLineOptionComposition, EnrichmentComposition, FileComposition}
import com.koala.importer.compositions.CommandLineOptionComposition.CommandLineOptionCompositionEnv
import com.koala.importer.compositions.EnrichmentComposition.EnrichmentCompositionEnv
import com.koala.importer.compositions.FileComposition.FileCompositionEnv
import com.koala.importer.models.common.CommandLineOptions
import com.koala.importer.models.gtfs.{Agency, Calendar, CalendarDates, FareAttributes, FareRules, FeedInfo, Frequencies, Routes, STM, Shapes, StopTimes, Stops, Trips}
import com.koala.importer.services.{ApplicationContextService, CommandLineOptionService, EnrichmentService, FileService, SparkConnectionService}
import org.apache.spark.sql.Encoders
import zio.cli.CliApp
import zio.cli.HelpDoc.Span.text
import zio.{ExitCode, RIO, URIO, ZEnv, ZLayer}

object Main extends zio.App {
  private implicit val stmEncoder = Encoders.product[STM]
  private implicit val agencyEncoder = Encoders.product[Agency]
  private implicit val calendarEncoder = Encoders.product[Calendar]
  private implicit val calendarDatesEncoder = Encoders.product[CalendarDates]
  private implicit val fareAttributesEncoder = Encoders.product[FareAttributes]
  private implicit val fareRulesEncoder = Encoders.product[FareRules]
  private implicit val feedInfoEncoder = Encoders.product[FeedInfo]
  private implicit val frequenciesEncoder = Encoders.product[Frequencies]
  private implicit val routesEncoder = Encoders.product[Routes]
  private implicit val shapesEncoder = Encoders.product[Shapes]
  private implicit val stopsEncoder = Encoders.product[Stops]
  private implicit val stopTimesEncoder = Encoders.product[StopTimes]
  private implicit val tripsEncoder = Encoders.product[Trips]

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
      agencyDataset <- FileComposition.readFile[Agency](applicationContext).provideLayer(fileCompositionLayer)
      calendarDataset <- FileComposition.readFile[Calendar](applicationContext).provideLayer(fileCompositionLayer)
      calendarDatesDataset <- FileComposition.readFile[CalendarDates](applicationContext).provideLayer(fileCompositionLayer)
      fareAttributesDataset <- FileComposition.readFile[FareAttributes](applicationContext).provideLayer(fileCompositionLayer)
      fareRulesDataset <- FileComposition.readFile[FareRules](applicationContext).provideLayer(fileCompositionLayer)
      feedInfoDataset <- FileComposition.readFile[FeedInfo](applicationContext).provideLayer(fileCompositionLayer)
      frequenciesDataset <- FileComposition.readFile[Frequencies](applicationContext).provideLayer(fileCompositionLayer)
      routesDataset <- FileComposition.readFile[Routes](applicationContext).provideLayer(fileCompositionLayer)
      shapesDataset <- FileComposition.readFile[Shapes](applicationContext).provideLayer(fileCompositionLayer)
      stopsDataset <- FileComposition.readFile[Routes](applicationContext).provideLayer(fileCompositionLayer)
      stopTimesDataset <- FileComposition.readFile[StopTimes](applicationContext).provideLayer(fileCompositionLayer)
      tripsDataset <- FileComposition.readFile[Trips](applicationContext).provideLayer(fileCompositionLayer)
      agencyEnrichWithPartitionDate <- EnrichmentComposition.enrichmentWithPartitionDate[Agency](agencyDataset, applicationContext).provideLayer(enrichmentCompositionLayer)
    } yield ()

  private lazy val commandLineOptionsLayer: ZLayer[ZEnv, Nothing, CommandLineOptionCompositionEnv] =
    CommandLineOptionService.live >>> CommandLineOptionComposition.live

  private lazy val applicationContextCompositionLayer: ZLayer[ZEnv, Nothing, ApplicationContextCompositionEnv] =
    (SparkConnectionService.live ++ ApplicationContextService.live) >>> ApplicationContextComposition.live

  private lazy val fileCompositionLayer: ZLayer[ZEnv, Throwable, FileCompositionEnv] =
    FileService.live >>> FileComposition.live

  private lazy val enrichmentCompositionLayer: ZLayer[ZEnv, Throwable, EnrichmentCompositionEnv] =
    EnrichmentService.live >>> EnrichmentComposition.live
}

