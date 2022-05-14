package com.koala.importer.compositions

import com.koala.importer.models.common.ApplicationContext
import com.koala.importer.models.enrichment.{AgencyTransform, CalendarDatesTransform, CalendarTransform, FareAttributesTransform, FareRulesTransform, FeedInfoTransform, FrequenciesTransform, RoutesTransform, ShapesTransform, StopTimesTransform, StopsTransform, TripsTransform}
import com.koala.importer.models.gtfs.{Agency, Calendar, CalendarDates, FareAttributes, FareRules, FeedInfo, Frequencies, Routes, Shapes, StopTimes, Stops, Trips}
import com.koala.importer.services.TransformService.TransformServiceEnv
import org.apache.spark.sql.{Dataset, Encoder}
import zio.{Has, UIO, URIO, ZIO, ZLayer}

object TransformComposition {
  type TransformCompositionEnv = Has[TransformComposition.Service]

  class Service(){
    def transformAgencyWithPartitionDate(dataset: Dataset[Agency], applicationContext: ApplicationContext)(implicit encoder: Encoder[AgencyTransform]): UIO[Dataset[AgencyTransform]] = {
      UIO {
        val partitionDate = applicationContext.commandLineOptions.date

        dataset.map{
          item =>
            AgencyTransform(
              item.agency_id,
              item.agency_name,
              item.agency_url,
              item.agency_timezone,
              item.agency_lang,
              item.agency_phone,
              item.agency_fare_url,
              partitionDate
            )
        }
      }
    }

    def transformCalendarWithPartitionDate(dataset: Dataset[Calendar], applicationContext: ApplicationContext)(implicit encoder: Encoder[CalendarTransform]): UIO[Dataset[CalendarTransform]] =
      UIO{
        val partitionDate = applicationContext.commandLineOptions.date

        dataset.map{
          item =>
            CalendarTransform(
              item.service_id,
              item.monday,
              item.tuesday,
              item.wednesday,
              item.thursday,
              item.friday,
              item.saturday,
              item.start_date,
              partitionDate
            )
        }
      }

    def transformCalendarDatesWithPartitionDate(dataset: Dataset[CalendarDates], applicationContext: ApplicationContext)(implicit encoder: Encoder[CalendarDatesTransform]): UIO[Dataset[CalendarDatesTransform]] =
      UIO{
        val partitionDate = applicationContext.commandLineOptions.date

        dataset.map{
          item =>
            CalendarDatesTransform(
              item.service_id,
              item.date,
              item.exception_type,
              partitionDate
            )
        }
      }

    def transformFareAttributesWithPartitionDate(dataset: Dataset[FareAttributes], applicationContext: ApplicationContext)(implicit encoder: Encoder[FareAttributesTransform]): UIO[Dataset[FareAttributesTransform]] =
      UIO{
        val partitionDate = applicationContext.commandLineOptions.date

        dataset.map{
          item =>
            FareAttributesTransform(
              item.fare_id,
              item.price,
              item.currency_type,
              item.payment_method,
              item.transfers,
              item.transfer_duration,
              partitionDate
            )
        }
      }

    def transformFareRulesWithPartitionDate(dataset: Dataset[FareRules], applicationContext: ApplicationContext)(implicit encoder: Encoder[FareRulesTransform]): UIO[Dataset[FareRulesTransform]] =
      UIO{
        val partitionDate = applicationContext.commandLineOptions.date

        dataset.map{
          item =>
            FareRulesTransform(
              item.fare_id,
              item.route_id,
              item.origin_id,
              item.destination_id,
              item.contains_id,
              partitionDate
            )
        }
      }

    def transformFeedInfoWithPartitionDate(dataset: Dataset[FeedInfo], applicationContext: ApplicationContext)(implicit encoder: Encoder[FeedInfoTransform]): UIO[Dataset[FeedInfoTransform]] =
      UIO{
        val partitionDate = applicationContext.commandLineOptions.date

        dataset.map{
          item =>
            FeedInfoTransform(
              item.feed_publisher_name,
              item.feed_publisher_url,
              item.feed_lang,
              item.feed_start_date,
              item.feed_end_date,
              partitionDate
            )
        }
      }

    def transformFrequenciesWithPartitionDate(dataset: Dataset[Frequencies], applicationContext: ApplicationContext)(implicit encoder: Encoder[FrequenciesTransform]): UIO[Dataset[FrequenciesTransform]] =
      UIO{
        val partitionDate = applicationContext.commandLineOptions.date

        dataset.map{
          item => FrequenciesTransform(
            item.trip_id,
            item.start_time,
            item.end_time,
            item.headway_secs,
            partitionDate
          )

        }
      }

    def transformRouteWithPartitionDate(dataset: Dataset[Routes], applicationContext: ApplicationContext)(implicit encoder: Encoder[RoutesTransform]): UIO[Dataset[RoutesTransform]] =
      UIO{
        val partitionDate = applicationContext.commandLineOptions.date

        dataset.map{
          item => RoutesTransform(
            item.route_id,
            item.agency_id,
            item.route_short_name,
            item.route_long_name,
            item.route_type,
            item.route_url,
            item.route_color,
            item.route_text_color,
            partitionDate
          )
        }
      }

    def transformShapesWithPartitionDate(dataset: Dataset[Shapes], applicationContext: ApplicationContext)(implicit encoder: Encoder[ShapesTransform]): UIO[Dataset[ShapesTransform]] =
      UIO{
        val partitionDate = applicationContext.commandLineOptions.date

        dataset.map{
          item => ShapesTransform(
            item.shape_id,
            item.shape_pt_lat,
            item.shape_pt_lon,
            item.shape_pt_sequence,
            partitionDate
          )
        }
      }

    def transformStopsWithPartitionDate(dataset: Dataset[Stops], applicationContext: ApplicationContext)(implicit encoder: Encoder[StopsTransform]): UIO[Dataset[StopsTransform]] =
      UIO{
        val partitionDate = applicationContext.commandLineOptions.date

        dataset.map{
          item => StopsTransform(
            item.stop_id,
            item.stop_code,
            item.stop_name,
            item.stop_lat,
            item.stop_lon,
            item.stop_url,
            item.location_type,
            item.parent_station,
            item.wheelchair_boarding,
            partitionDate
          )
        }
      }

    def transformStopTimesWithPartitionDate(dataset: Dataset[StopTimes], applicationContext: ApplicationContext)(implicit encoder: Encoder[StopTimesTransform]): UIO[Dataset[StopTimesTransform]] =
      UIO{
        val partitionDate = applicationContext.commandLineOptions.date

        dataset.map{
          item => StopTimesTransform(
            item.trip_id,
            item.arrival_time,
            item.departure_time,
            item.stop_id,
            item.stop_sequence,
            partitionDate
          )
        }
      }

    def transformTripsWithPartitionDate(dataset: Dataset[Trips], applicationContext: ApplicationContext)(implicit encoder: Encoder[TripsTransform]): UIO[Dataset[TripsTransform]] =
      UIO{
        val partitionDate = applicationContext.commandLineOptions.date

        dataset.map{
          item => TripsTransform(
            item.route_id,
            item.service_id,
            item.trip_id,
            item.trip_headsign,
            item.direction_id,
            item.shape_id,
            item.wheelchair_accessible,
            item.note_fr,
            item.note_fr,
            partitionDate
          )
        }
      }
  }

  def transformAgencyWithPartitionDate(dataset: Dataset[Agency], applicationContext: ApplicationContext)(implicit encoder: Encoder[AgencyTransform]): URIO[TransformCompositionEnv, Dataset[AgencyTransform]] =
    ZIO.accessM(_.get.transformAgencyWithPartitionDate(dataset, applicationContext))

  def transformCalendarWithPartitionDate(dataset: Dataset[Calendar], applicationContext: ApplicationContext)(implicit encoder: Encoder[CalendarTransform]): URIO[TransformCompositionEnv, Dataset[CalendarTransform]] =
    ZIO.accessM(_.get.transformCalendarWithPartitionDate(dataset, applicationContext))

  def transformCalendarDatesWIthPartitionDate(dataset: Dataset[CalendarDates], applicationContext: ApplicationContext)(implicit encoder: Encoder[CalendarDatesTransform]): URIO[TransformCompositionEnv, Dataset[CalendarDatesTransform]] =
    ZIO.accessM(_.get.transformCalendarDatesWithPartitionDate(dataset, applicationContext))

  def transformFareAttributesWithPartitionDate(dataset: Dataset[FareAttributes], applicationContext: ApplicationContext)(implicit encoder: Encoder[FareAttributesTransform]): URIO[TransformCompositionEnv, Dataset[FareAttributesTransform]] =
    ZIO.accessM(_.get.transformFareAttributesWithPartitionDate(dataset, applicationContext))

  def transformFareRulesWithPartitionDate(dataset: Dataset[FareRules], applicationContext: ApplicationContext)(implicit encoder: Encoder[FareRulesTransform]): URIO[TransformCompositionEnv, Dataset[FareRulesTransform]] =
    ZIO.accessM(_.get.transformFareRulesWithPartitionDate(dataset, applicationContext))

  def transformFeedInfoWithPartitionDate(dataset: Dataset[FeedInfo], applicationContext: ApplicationContext)(implicit encoder: Encoder[FeedInfoTransform]): URIO[TransformCompositionEnv, Dataset[FeedInfoTransform]] =
    ZIO.accessM(_.get.transformFeedInfoWithPartitionDate(dataset, applicationContext))

  def transformFrequenciesWithPartitionDate(dataset: Dataset[Frequencies], applicationContext: ApplicationContext)(implicit encoder: Encoder[FrequenciesTransform]): URIO[TransformCompositionEnv, Dataset[FrequenciesTransform]] =
    ZIO.accessM(_.get.transformFrequenciesWithPartitionDate(dataset, applicationContext))

  def transformRouteWithPartitionDate(dataset: Dataset[Routes], applicationContext: ApplicationContext)(implicit encoder: Encoder[RoutesTransform]): URIO[TransformCompositionEnv, Dataset[RoutesTransform]] =
    ZIO.accessM(_.get.transformRouteWithPartitionDate(dataset, applicationContext))

  def transformShapesWithPartitionDate(dataset: Dataset[Shapes], applicationContext: ApplicationContext)(implicit encoder: Encoder[ShapesTransform]): URIO[TransformCompositionEnv, Dataset[ShapesTransform]] =
    ZIO.accessM(_.get.transformShapesWithPartitionDate(dataset, applicationContext))

  def transformStopsWithPartitionDate(dataset: Dataset[Stops], applicationContext: ApplicationContext)(implicit encoder: Encoder[StopsTransform]): URIO[TransformCompositionEnv, Dataset[StopsTransform]] =
    ZIO.accessM(_.get.transformStopsWithPartitionDate(dataset, applicationContext))

  def transformStopTimesWithPartitionDate(dataset: Dataset[StopTimes], applicationContext: ApplicationContext)(implicit encoder: Encoder[StopTimesTransform]): URIO[TransformCompositionEnv, Dataset[StopTimesTransform]] =
    ZIO.accessM(_.get.transformStopTimesWithPartitionDate(dataset, applicationContext))

  def transformTripsWithPartitionDate(dataset: Dataset[Trips], applicationContext: ApplicationContext)(implicit encoder: Encoder[TripsTransform]): URIO[TransformCompositionEnv, Dataset[TripsTransform]] =
    ZIO.accessM(_.get.transformTripsWithPartitionDate(dataset, applicationContext))

  lazy val live: ZLayer[TransformServiceEnv, Nothing, TransformCompositionEnv] =
    ZLayer.fromService(_ => new Service())

}
