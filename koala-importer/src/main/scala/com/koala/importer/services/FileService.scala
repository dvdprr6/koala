package com.koala.importer.services

import org.apache.spark.sql.{Dataset, Encoder, SparkSession}
import zio.{Has, RIO, Task, ZEnv, ZIO, ZLayer}

object FileService {
  type FileEnv = Has[FileService.Service]

  trait Service{
    def readFile[T](input: String)(implicit sparkSession: SparkSession, encoder: Encoder[T]): Task[Dataset[T]]
    def importToHive[T](dataset: Dataset[T], partition: String)(implicit sparkSession: SparkSession): Task[Unit]
  }

  def readFile[T](input: String)(implicit sparkSession: SparkSession, encoder: Encoder[T]): RIO[FileEnv, Dataset[T]] =
    ZIO.accessM(_.get.readFile[T](input))

  def importToHive[T](dataset: Dataset[T], partition: String)(implicit sparkSession: SparkSession): RIO[FileEnv, Unit] =
    ZIO.accessM(_.get.importToHive[T](dataset, partition))

  lazy val live: ZLayer[ZEnv, Throwable, FileEnv] =
    ZLayer.succeed(new Service {
      override def readFile[T](input: String)(implicit sparkSession: SparkSession, encoder: Encoder[T]): Task[Dataset[T]] =
        Task{
          sparkSession.read
            .format("csv")
            .option("delimiter", ",")
            .option("header", true)
            .load(input)
            .as[T]
        }

      override def importToHive[T](dataset: Dataset[T], partition: String)(implicit sparkSession: SparkSession): Task[Unit] = ???
    })

}
