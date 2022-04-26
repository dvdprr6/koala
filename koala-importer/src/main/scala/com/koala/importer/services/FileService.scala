package com.koala.importer.services

import com.koala.importer.models.gtfs.STM
import org.apache.spark.sql.{Dataset, Encoder, SparkSession}
import zio.{Has, RIO, Task, ZEnv, ZIO, ZLayer}

object FileService {
  type FileServiceEnv = Has[FileService.Service]

  trait Service{
    def readFile[T <: STM](input: String)(implicit sparkSession: SparkSession, encoder: Encoder[T]): Task[Dataset[T]]
    def importToHive[T <: STM](dataset: Dataset[T], partition: String)(implicit sparkSession: SparkSession): Task[Unit]
  }

  def readFile[T <: STM](input: String)(implicit sparkSession: SparkSession, encoder: Encoder[T]): RIO[FileServiceEnv, Dataset[T]] =
    ZIO.accessM(_.get.readFile[T](input))

  def importToHive[T <: STM](dataset: Dataset[T], partition: String)(implicit sparkSession: SparkSession): RIO[FileServiceEnv, Unit] =
    ZIO.accessM(_.get.importToHive[T](dataset, partition))

  lazy val live: ZLayer[ZEnv, Throwable, FileServiceEnv] =
    ZLayer.succeed(new Service {
      override def readFile[T <: STM](input: String)(implicit sparkSession: SparkSession, encoder: Encoder[T]): Task[Dataset[T]] =
        Task{
          sparkSession.read
            .format("csv")
            .option("delimiter", ",")
            .option("header", true)
            .load(input)
            .as[T]
        }

      override def importToHive[T <: STM](dataset: Dataset[T], partition: String)(implicit sparkSession: SparkSession): Task[Unit] = ???
    })

}
