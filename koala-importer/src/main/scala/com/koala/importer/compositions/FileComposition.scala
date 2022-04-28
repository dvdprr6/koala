package com.koala.importer.compositions

import com.koala.importer.models.common.ApplicationContext
import com.koala.importer.models.gtfs.{Agency, Calendar, CalendarDates, FareAttributes, FareRules, FeedInfo, Frequencies, Routes, GTFS, Shapes, StopTimes, Stops, Trips}
import com.koala.importer.services.FileService
import com.koala.importer.services.FileService.FileServiceEnv
import org.apache.spark.sql.{Dataset, Encoder}
import zio.{Has, Task, ZIO, ZLayer}

object FileComposition {
  type FileCompositionEnv = Has[FileComposition.Service]

  class Service(fileService: FileService.Service){
    def readAgencyFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[Agency]): Task[Dataset[Agency]] = {
      implicit val sparkSession = applicationContext.sparkSession

      val input = applicationContext.commandLineOptions.input

      val inputPath = s"$input/agency.txt"

      fileService.readFile[Agency](inputPath)
    }

    def readCalendarFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[Calendar]): Task[Dataset[Calendar]] = {
      implicit val sparkSession = applicationContext.sparkSession

      val input = applicationContext.commandLineOptions.input

      val inputPath = s"$input/calendar.txt"

      fileService.readFile[Calendar](inputPath)
    }

    def readCalendarDatesFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[CalendarDates]): Task[Dataset[CalendarDates]] = {
      implicit val sparkSession = applicationContext.sparkSession

      val input = applicationContext.commandLineOptions.input

      val inputPath = s"$input/calendar_dates.txt"

      fileService.readFile[CalendarDates](inputPath)
    }

    def readFareAttributesFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[FareAttributes]): Task[Dataset[FareAttributes]] = {
      implicit val sparkSession = applicationContext.sparkSession

      val input = applicationContext.commandLineOptions.input

      val inputPath = s"$input/fare_attributes.txt"

      fileService.readFile[FareAttributes](inputPath)
    }

    def readFareRulesFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[FareRules]): Task[Dataset[FareRules]] = {
      implicit val sparkSession = applicationContext.sparkSession

      val input = applicationContext.commandLineOptions.input

      val inputPath = s"$input/fare_rules.txt"

      fileService.readFile[FareRules](inputPath)
    }

    def readFeedInfoFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[FeedInfo]): Task[Dataset[FeedInfo]] = {
      implicit val sparkSession = applicationContext.sparkSession

      val input = applicationContext.commandLineOptions.input

      val inputPath = s"$input/feed_info.txt"

      fileService.readFile[FeedInfo](inputPath)
    }

    def readFrequenciesFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[Frequencies]): Task[Dataset[Frequencies]] = {
      implicit val sparkSession = applicationContext.sparkSession

      val input = applicationContext.commandLineOptions.input

      val inputPath = s"$input/frequencies.txt"

      fileService.readFile[Frequencies](inputPath)
    }

    def readRoutesFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[Routes]): Task[Dataset[Routes]] = {
      implicit val sparkSession = applicationContext.sparkSession

      val input = applicationContext.commandLineOptions.input

      val inputPath = s"$input/routes.txt"

      fileService.readFile[Routes](inputPath)
    }

    def readShapesFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[Shapes]): Task[Dataset[Shapes]] = {
      implicit val sparkSession = applicationContext.sparkSession

      val input = applicationContext.commandLineOptions.input

      val inputPath = s"$input/shapes.txt"

      fileService.readFile[Shapes](inputPath)
    }

    def readStopsFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[Stops]): Task[Dataset[Stops]] = {
      implicit val sparkSession = applicationContext.sparkSession

      val input = applicationContext.commandLineOptions.input

      val inputPath = s"$input/stops.txt"

      fileService.readFile[Stops](inputPath)
    }

    def readStopTimesFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[StopTimes]): Task[Dataset[StopTimes]] = {
      implicit val sparkSession = applicationContext.sparkSession

      val input = applicationContext.commandLineOptions.input

      val inputPath = s"$input/stop_times.txt"

      fileService.readFile[StopTimes](inputPath)
    }

    def readTripsFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[Trips]): Task[Dataset[Trips]] = {
      implicit val sparkSession = applicationContext.sparkSession

      val input = applicationContext.commandLineOptions.input

      val inputPath = s"$input/trips.txt"

      fileService.readFile[Trips](inputPath)
    }
  }

  def readAgencyFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[Agency]): ZIO[FileCompositionEnv, Throwable, Dataset[Agency]] =
    ZIO.accessM(_.get.readAgencyFile(applicationContext))

  def readCalendarFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[Calendar]): ZIO[FileCompositionEnv, Throwable, Dataset[Calendar]] =
    ZIO.accessM(_.get.readCalendarFile(applicationContext))

  def readCalendarDatesFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[CalendarDates]): ZIO[FileCompositionEnv, Throwable, Dataset[CalendarDates]] =
    ZIO.accessM(_.get.readCalendarDatesFile(applicationContext))

  def readFareAttributesFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[FareAttributes]): ZIO[FileCompositionEnv, Throwable, Dataset[FareAttributes]] =
    ZIO.accessM(_.get.readFareAttributesFile(applicationContext))

  def readFareRulesFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[FareRules]): ZIO[FileCompositionEnv, Throwable, Dataset[FareRules]] =
    ZIO.accessM(_.get.readFareRulesFile(applicationContext))

  def readFeedInfoFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[FeedInfo]): ZIO[FileCompositionEnv, Throwable, Dataset[FeedInfo]] =
    ZIO.accessM(_.get.readFeedInfoFile(applicationContext))

  def readFrequenciesFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[Frequencies]): ZIO[FileCompositionEnv, Throwable, Dataset[Frequencies]] =
    ZIO.accessM(_.get.readFrequenciesFile(applicationContext))

  def readRoutesFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[Routes]): ZIO[FileCompositionEnv, Throwable, Dataset[Routes]] =
    ZIO.accessM(_.get.readRoutesFile(applicationContext))

  def readShapesFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[Shapes]): ZIO[FileCompositionEnv, Throwable, Dataset[Shapes]] =
    ZIO.accessM(_.get.readShapesFile(applicationContext))

  def readStopsFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[Stops]): ZIO[FileCompositionEnv, Throwable, Dataset[Stops]] =
    ZIO.accessM(_.get.readStopsFile(applicationContext))

  def readStopTimesFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[StopTimes]): ZIO[FileCompositionEnv, Throwable, Dataset[StopTimes]] =
    ZIO.accessM(_.get.readStopTimesFile(applicationContext))

  def readTripsFile(applicationContext: ApplicationContext)(implicit encoder: Encoder[Trips]): ZIO[FileCompositionEnv, Throwable, Dataset[Trips]] =
    ZIO.accessM(_.get.readTripsFile(applicationContext))

  lazy val live: ZLayer[FileServiceEnv, Throwable, FileCompositionEnv] =
    ZLayer.fromService(fileService => new Service(fileService))

}
