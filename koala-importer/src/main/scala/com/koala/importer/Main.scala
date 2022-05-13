package com.koala.importer
import com.koala.importer.compositions.ApplicationContextComposition.ApplicationContextCompositionEnv
import com.koala.importer.compositions.{ApplicationContextComposition, CommandLineOptionComposition, FileComposition, TransformComposition}
import com.koala.importer.compositions.CommandLineOptionComposition.CommandLineOptionCompositionEnv
import com.koala.importer.compositions.TransformComposition.TransformCompositionEnv
import com.koala.importer.compositions.FileComposition.FileCompositionEnv
import com.koala.importer.models.common.CommandLineOptions
import com.koala.importer.models.enrichment.{AgencyTransform, CalendarDatesTransform, CalendarTransform, FareAttributesTransform, FareRulesTransform, FeedInfoTransform, FrequenciesTransform, RoutesTransform, ShapesTransform, StopTimesTransform, StopsTransform, TripsTransform}
import com.koala.importer.models.gtfs.{Agency, Calendar, CalendarDates, FareAttributes, FareRules, FeedInfo, Frequencies, Routes, Shapes, StopTimes, Stops, Trips}
import com.koala.importer.services.{ApplicationContextService, CommandLineOptionService, FileService, SparkConnectionService, TransformService}
import com.koala.importer.utils.Constants.{FILE_NAME_AGENCY, FILE_NAME_CALENDAR, FILE_NAME_CALENDAR_DATES, FILE_NAME_FARE_ATTRIBUTES, FILE_NAME_FARE_RULES, FILE_NAME_FEED_INFO, FILE_NAME_FREQUENCIES, FILE_NAME_ROUTES, FILE_NAME_SHAPES, FILE_NAME_STOPS, FILE_NAME_STOP_TIMES, FILE_NAME_TRIPS, HIVE_TABLE_AGENCY, HIVE_TABLE_CALENDAR, HIVE_TABLE_CALENDAR_DATES, HIVE_TABLE_FARE_ATTRIBUTES, HIVE_TABLE_FARE_RULES, HIVE_TABLE_FEED_INFO, HIVE_TABLE_FREQUENCIES, HIVE_TABLE_ROUTES, HIVE_TABLE_SHAPES, HIVE_TABLE_STOPS, HIVE_TABLE_STOP_TIMES, HIVE_TABLE_TRIPS, PARTITION_COLUMN}
import org.apache.spark.sql.Encoders
import zio.cli.CliApp
import zio.cli.HelpDoc.Span.text
import zio.{ExitCode, RIO, URIO, ZEnv, ZLayer}

object Main extends zio.App {
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

  private implicit val agencyTransformEncoderEncoder = Encoders.product[AgencyTransform]
  private implicit val calendarDatesTransformEncoder = Encoders.product[CalendarDatesTransform]
  private implicit val calendarTransformEncoder = Encoders.product[CalendarTransform]
  private implicit val fareAttributesTransformEncoder = Encoders.product[FareAttributesTransform]
  private implicit val fareRulesTransformEncoder = Encoders.product[FareRulesTransform]
  private implicit val feedInfoTransformEncoder = Encoders.product[FeedInfoTransform]
  private implicit val frequenciesTransformEncoder = Encoders.product[FrequenciesTransform]
  private implicit val routesTransformEncoder = Encoders.product[RoutesTransform]
  private implicit val shapesTransformEncoder = Encoders.product[ShapesTransform]
  private implicit val stopsTransformEncoder = Encoders.product[StopsTransform]
  private implicit val stopTimesTransformEncoder = Encoders.product[StopTimesTransform]
  private implicit val tripsTransformEncoder = Encoders.product[TripsTransform]

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
      agencyDataset <- FileComposition.readFile[Agency](applicationContext, FILE_NAME_AGENCY).provideLayer(fileCompositionLayer)
      calendarDataset <- FileComposition.readFile[Calendar](applicationContext, FILE_NAME_CALENDAR).provideLayer(fileCompositionLayer)
      calendarDatesDataset <- FileComposition.readFile[CalendarDates](applicationContext, FILE_NAME_CALENDAR_DATES).provideLayer(fileCompositionLayer)
      fareAttributesDataset <- FileComposition.readFile[FareAttributes](applicationContext, FILE_NAME_FARE_ATTRIBUTES).provideLayer(fileCompositionLayer)
      fareRulesDataset <- FileComposition.readFile[FareRules](applicationContext, FILE_NAME_FARE_RULES).provideLayer(fileCompositionLayer)
      feedInfoDataset <- FileComposition.readFile[FeedInfo](applicationContext, FILE_NAME_FEED_INFO).provideLayer(fileCompositionLayer)
      frequenciesDataset <- FileComposition.readFile[Frequencies](applicationContext, FILE_NAME_FREQUENCIES).provideLayer(fileCompositionLayer)
      routesDataset <- FileComposition.readFile[Routes](applicationContext, FILE_NAME_ROUTES).provideLayer(fileCompositionLayer)
      shapesDataset <- FileComposition.readFile[Shapes](applicationContext, FILE_NAME_SHAPES).provideLayer(fileCompositionLayer)
      stopsDataset <- FileComposition.readFile[Stops](applicationContext, FILE_NAME_STOPS).provideLayer(fileCompositionLayer)
      stopTimesDataset <- FileComposition.readFile[StopTimes](applicationContext, FILE_NAME_STOP_TIMES).provideLayer(fileCompositionLayer)
      tripsDataset <-  FileComposition.readFile[Trips](applicationContext, FILE_NAME_TRIPS).provideLayer(fileCompositionLayer)
      agencyTransformWithPartitionDate <- TransformComposition.transformAgencyWithPartitionDate(agencyDataset, applicationContext).provideLayer(transformCompositionLayer)
      calendarTransformWithPartitionDate <- TransformComposition.transformCalendarWithPartitionDate(calendarDataset, applicationContext).provideLayer(transformCompositionLayer)
      calendarDateTransformWithPartitionDate <- TransformComposition.transformCalendarDatesWIthPartitionDate(calendarDatesDataset, applicationContext).provideLayer(transformCompositionLayer)
      fareAttributesTransformWithPartitionDate <- TransformComposition.transformFareAttributesWithPartitionDate(fareAttributesDataset, applicationContext).provideLayer(transformCompositionLayer)
      fareRulesTransformWithPartitionDate <- TransformComposition.transformFareRulesWithPartitionDate(fareRulesDataset, applicationContext).provideLayer(transformCompositionLayer)
      feedInfoTransformWithPartitionDate <- TransformComposition.transformFeedInfoWithPartitionDate(feedInfoDataset, applicationContext).provideLayer(transformCompositionLayer)
      frequenciesTransformWithPartitionDate <- TransformComposition.transformFrequenciesWithPartitionDate(frequenciesDataset, applicationContext).provideLayer(transformCompositionLayer)
      routesTransformWithPartitionDate <- TransformComposition.transformRouteWithPartitionDate(routesDataset, applicationContext).provideLayer(transformCompositionLayer)
      shapesTransformWithPartitionDate <- TransformComposition.transformShapesWithPartitionDate(shapesDataset, applicationContext).provideLayer(transformCompositionLayer)
      stopTransformWithPartitionDate <- TransformComposition.transformStopsWithPartitionDate(stopsDataset, applicationContext).provideLayer(transformCompositionLayer)
      stopTimesTransformWithPartitionDate <- TransformComposition.transformStopTimesWithPartitionDate(stopTimesDataset, applicationContext).provideLayer(transformCompositionLayer)
      tripsTransformWithPartitionDate <- TransformComposition.transformTripsWithPartitionDate(tripsDataset, applicationContext).provideLayer(transformCompositionLayer)
      _ <- FileComposition.writeToHive[AgencyTransform](agencyTransformWithPartitionDate, PARTITION_COLUMN, HIVE_TABLE_AGENCY).provideLayer(fileCompositionLayer)
      _ <- FileComposition.writeToHive[CalendarTransform](calendarTransformWithPartitionDate, PARTITION_COLUMN, HIVE_TABLE_CALENDAR).provideLayer(fileCompositionLayer)
      _ <- FileComposition.writeToHive[CalendarDatesTransform](calendarDateTransformWithPartitionDate, PARTITION_COLUMN, HIVE_TABLE_CALENDAR_DATES).provideLayer(fileCompositionLayer)
      _ <- FileComposition.writeToHive[FareAttributesTransform](fareAttributesTransformWithPartitionDate, PARTITION_COLUMN, HIVE_TABLE_FARE_ATTRIBUTES).provideLayer(fileCompositionLayer)
      _ <- FileComposition.writeToHive[FareRulesTransform](fareRulesTransformWithPartitionDate, PARTITION_COLUMN, HIVE_TABLE_FARE_RULES).provideLayer(fileCompositionLayer)
      _ <- FileComposition.writeToHive[FeedInfoTransform](feedInfoTransformWithPartitionDate, PARTITION_COLUMN, HIVE_TABLE_FEED_INFO).provideLayer(fileCompositionLayer)
      _ <- FileComposition.writeToHive[FrequenciesTransform](frequenciesTransformWithPartitionDate, PARTITION_COLUMN, HIVE_TABLE_FREQUENCIES).provideLayer(fileCompositionLayer)
      _ <- FileComposition.writeToHive[RoutesTransform](routesTransformWithPartitionDate, PARTITION_COLUMN, HIVE_TABLE_ROUTES).provideLayer(fileCompositionLayer)
      _ <- FileComposition.writeToHive[ShapesTransform](shapesTransformWithPartitionDate, PARTITION_COLUMN, HIVE_TABLE_SHAPES).provideLayer(fileCompositionLayer)
      _ <- FileComposition.writeToHive[StopsTransform](stopTransformWithPartitionDate, PARTITION_COLUMN, HIVE_TABLE_STOPS).provideLayer(fileCompositionLayer)
      _ <- FileComposition.writeToHive[StopTimesTransform](stopTimesTransformWithPartitionDate, PARTITION_COLUMN, HIVE_TABLE_STOP_TIMES).provideLayer(fileCompositionLayer)
      _ <- FileComposition.writeToHive[TripsTransform](tripsTransformWithPartitionDate, PARTITION_COLUMN, HIVE_TABLE_TRIPS).provideLayer(fileCompositionLayer)
    } yield ()

  private lazy val commandLineOptionsLayer: ZLayer[ZEnv, Nothing, CommandLineOptionCompositionEnv] =
    CommandLineOptionService.live >>> CommandLineOptionComposition.live

  private lazy val applicationContextCompositionLayer: ZLayer[ZEnv, Nothing, ApplicationContextCompositionEnv] =
    (SparkConnectionService.live ++ ApplicationContextService.live) >>> ApplicationContextComposition.live

  private lazy val fileCompositionLayer: ZLayer[ZEnv, Throwable, FileCompositionEnv] =
    FileService.live >>> FileComposition.live

  private lazy val transformCompositionLayer: ZLayer[ZEnv, Nothing, TransformCompositionEnv] =
    TransformService.live >>> TransformComposition.live
}

