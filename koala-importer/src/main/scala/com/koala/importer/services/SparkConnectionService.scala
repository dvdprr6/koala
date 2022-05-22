package com.koala.importer.services

import com.koala.importer.utils.Constants.APP_NAME
import org.apache.spark.sql.SparkSession
import zio.{Has, RIO, Task, ZEnv, ZIO, ZLayer}

object SparkConnectionService {
  type SparkConnectionServiceEnv = Has[SparkConnectionService.Service]

  trait Service{
    def getSparkSession(): Task[SparkSession]
  }

  def getSparkSession(): RIO[SparkConnectionServiceEnv, SparkSession] =
    ZIO.accessM(_.get.getSparkSession())

  lazy val live: ZLayer[ZEnv, Nothing, SparkConnectionServiceEnv] =
    ZLayer.succeed(() => {
      Task{
        SparkSession.builder()
          .appName(APP_NAME)
          .config("hive.exec.dynamic.partition.mode", "nonstrict")
          .enableHiveSupport()
          .master("local[*]")
          .getOrCreate()
      }
    })
}
