package com.koala.gtfsrealtime.services

import com.koala.gtfsrealtime.utils.Constants.APP_NAME
import org.apache.spark.sql.SparkSession
import zio.{Has, RIO, RLayer, Task, ZEnv, ZIO, ZLayer}

object SparkConnectionService {
  type SparkConnectionServiceEnv = Has[SparkConnectionService.Service]

  trait Service{
    def getSparkSession(): Task[SparkSession]
  }

  def getSparkSession(): RIO[SparkConnectionServiceEnv, SparkSession] =
    ZIO.accessM(_.get.getSparkSession())

  lazy val live: RLayer[ZEnv, SparkConnectionServiceEnv] =
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
