package com.koala.importer.compositions

import com.koala.importer.models.common.ApplicationContext
import com.koala.importer.models.gtfs.STM
import com.koala.importer.services.FileService
import com.koala.importer.services.FileService.FileServiceEnv
import org.apache.spark.sql.{Dataset, Encoder}
import zio.{Has, Task, ZIO, ZLayer}

object FileComposition {
  type FileCompositionEnv = Has[FileComposition.Service]

  class Service(fileService: FileService.Service){
    def readFile[T <: STM](applicationContext: ApplicationContext)(implicit encoder: Encoder[T]): Task[Dataset[T]] = {
      implicit val sparkSession = applicationContext.sparkSession
      val inputPath = applicationContext.commandLineOptions.input

      fileService.readFile[T](inputPath)
    }
  }

  def readFile[T <: STM](applicationContext: ApplicationContext)(implicit encoder: Encoder[T]): ZIO[FileCompositionEnv, Throwable, Dataset[T]] =
    ZIO.accessM(_.get.readFile(applicationContext))

  lazy val live: ZLayer[FileServiceEnv, Throwable, FileCompositionEnv] =
    ZLayer.fromService(fileService => new Service(fileService))

}
