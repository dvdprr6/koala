package com.koala.importer.compositions

import com.koala.importer.models.common.ApplicationContext
import com.koala.importer.models.gtfs.GTFS
import com.koala.importer.services.EnrichmentService
import com.koala.importer.services.EnrichmentService.EnrichmentServiceEnv
import org.apache.spark.sql.{Dataset, Encoder}
import zio.{Has, RIO, UIO, ZIO, ZLayer}

object EnrichmentComposition {
  type EnrichmentCompositionEnv = Has[EnrichmentComposition.Service]

  class Service(enrichmentService: EnrichmentService.Service){
    def enrichmentWithPartitionDate[T <: GTFS](dataset: Dataset[T], applicationContext: ApplicationContext)(implicit encoder0: Encoder[GTFS], encoder1: Encoder[T]): UIO[Dataset[T]] = {
      val partitionDate = applicationContext.commandLineOptions.date

      enrichmentService.enrichWithPartitionDate[T](dataset, partitionDate)
    }
  }

  def enrichmentWithPartitionDate[T <: GTFS](dataset: Dataset[T], applicationContext: ApplicationContext)(implicit encoder0: Encoder[GTFS], encoder1: Encoder[T]): RIO[EnrichmentCompositionEnv, Dataset[T]] =
    ZIO.accessM(_.get.enrichmentWithPartitionDate(dataset, applicationContext))

  lazy val live: ZLayer[EnrichmentServiceEnv, Throwable, EnrichmentCompositionEnv] =
    ZLayer.fromService(enrichmentService => new Service(enrichmentService))

}
