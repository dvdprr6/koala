package com.koala.gtfsrealtime.services

import com.koala.gtfsrealtime.models.{ApplicationContext, CommandLineOptions}
import org.apache.spark.sql.SparkSession
import zio.{Has, UIO, URIO, URLayer, ZEnv, ZIO, ZLayer}

object ApplicationContextService {
  type ApplicationContextServiceEnv = Has[ApplicationContextService.Service]

  trait Service{
    def getApplicationContext(commandLineOptions: CommandLineOptions, sparkSession: SparkSession): UIO[ApplicationContext]
  }

  def getApplicationContext(commandLineOptions: CommandLineOptions, sparkSession: SparkSession): URIO[ApplicationContextServiceEnv, ApplicationContext] =
    ZIO.accessM(_.get.getApplicationContext(commandLineOptions, sparkSession))

  lazy val live: URLayer[ZEnv, ApplicationContextServiceEnv] =
    ZLayer.succeed((commandLineOptions, sparkSession) =>
      UIO{
        ApplicationContext(commandLineOptions, sparkSession)
      }
    )
}
