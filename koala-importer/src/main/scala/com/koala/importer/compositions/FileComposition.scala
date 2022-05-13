package com.koala.importer.compositions

import com.koala.importer.models.common.ApplicationContext
import com.koala.importer.models.enrichment.Transform
import com.koala.importer.models.gtfs.GTFS
import com.koala.importer.services.FileService
import com.koala.importer.services.FileService.FileServiceEnv
import org.apache.spark.sql.{Dataset, Encoder}
import zio.{Has, RIO, Task, ZIO, ZLayer}

object FileComposition {
  type FileCompositionEnv = Has[FileComposition.Service]

  class Service(fileService: FileService.Service){

    def readFile[T <: GTFS](applicationContext: ApplicationContext, fileName: String)(implicit encoder: Encoder[T]): Task[Dataset[T]] = {
      implicit val sparkSession = applicationContext.sparkSession

      val input = applicationContext.commandLineOptions.input
      val date = applicationContext.commandLineOptions.date

      val inputPath = s"$input/$date/$fileName"

      fileService.readFile[T](inputPath)
    }

    def writeToHive[T <: Transform](dataset: Dataset[T], partition: String, tableName: String): Task[Unit] = {
      fileService.importToHive[T](dataset, partition, tableName)
    }
  }

  def readFile[T <: GTFS](applicationContext: ApplicationContext, fileName: String)(implicit encoder: Encoder[T]): RIO[FileCompositionEnv, Dataset[T]] =
    ZIO.accessM(_.get.readFile(applicationContext, fileName))

  def writeToHive[T <: Transform](dataset: Dataset[T], partition: String, tableName: String): ZIO[FileCompositionEnv, Throwable, Unit] =
    ZIO.accessM(_.get.writeToHive(dataset, partition, tableName))

  lazy val live: ZLayer[FileServiceEnv, Throwable, FileCompositionEnv] =
    ZLayer.fromService(fileService => new Service(fileService))

}
