package com.koala.importer.services

import com.koala.importer.models.gtfs.STM
import org.apache.spark.sql.{Dataset, Encoder}
import zio.{Has, RIO, UIO, URLayer, ZEnv, ZIO, ZLayer}

object EnrichmentService {
  type EnrichmentServiceEnv = Has[EnrichmentService.Service]

  trait Service{
    def enrichWithPartitionDate[T <: STM](dataset: Dataset[T], partitionDate: String)(implicit encoder0: Encoder[STM], encoder1: Encoder[T]): UIO[Dataset[T]]
  }

  def enrichWithPartitionDate[T <: STM](dataset: Dataset[T], partitionDate: String)(implicit encoder0: Encoder[STM], encoder1: Encoder[T]): RIO[EnrichmentServiceEnv, Dataset[T]] =
    ZIO.accessM(_.get.enrichWithPartitionDate[T](dataset, partitionDate))


  lazy val live: URLayer[ZEnv, EnrichmentServiceEnv] =
    ZLayer.succeed(new Service{
      override def enrichWithPartitionDate[T <: STM](dataset: Dataset[T], partitionDate: String)(implicit encoder0: Encoder[STM], encoder1: Encoder[T]): UIO[Dataset[T]] = {
        UIO{
          dataset.map(item => item.copy(partitionDate = partitionDate)).as[T]
        }
      }
    })
}
