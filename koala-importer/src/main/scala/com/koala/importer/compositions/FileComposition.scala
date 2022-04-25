package com.koala.importer.compositions

import com.koala.importer.models.ApplicationContext
import com.koala.importer.services.FileService
import com.koala.importer.services.FileService.FileEnv
import org.apache.spark.sql.{Dataset, Encoder}
import zio.{Has, Task, ZIO, ZLayer}

object FileComposition {
  type FileCompositionEnv = Has[FileComposition.Service]

  class Service(fileService: FileService.Service){
    def readFile[T](applicationContext: ApplicationContext)(implicit encoder: Encoder[T]): Task[Dataset[T]] = {
      implicit val sparkSession = applicationContext.sparkSession
      val inputPath = applicationContext.commandLineOptions.input

      fileService.readFile[T](inputPath)
    }
  }

  def readFile[T](applicationContext: ApplicationContext)(implicit encoder: Encoder[T]): ZIO[FileCompositionEnv, Throwable, Dataset[T]] =
    ZIO.accessM(_.get.readFile(applicationContext))

  lazy val live: ZLayer[FileEnv, Throwable, FileCompositionEnv] =
    ZLayer.fromService(fileService => new Service(fileService))

}
