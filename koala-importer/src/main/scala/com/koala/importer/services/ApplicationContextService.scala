package com.koala.importer.services

import com.koala.importer.models.common
import com.koala.importer.models.common.{ApplicationContext, CommandLineOptions}
import org.apache.spark.sql.SparkSession
import zio.{Has, UIO, URIO, ZEnv, ZIO, ZLayer}

object ApplicationContextService {
  type ApplicationContextServiceEnv = Has[ApplicationContextService.Service]

  trait Service{
    def getApplicationContext(commandLineOptions: CommandLineOptions, sparkSession: SparkSession): UIO[ApplicationContext]
  }

  def getApplicationContext(commandLineOptions: CommandLineOptions, sparkSession: SparkSession): URIO[ApplicationContextServiceEnv, ApplicationContext] =
    ZIO.accessM(_.get.getApplicationContext(commandLineOptions, sparkSession))

  lazy val live: ZLayer[ZEnv, Nothing, ApplicationContextServiceEnv] =
    ZLayer.succeed((commandLineOptions, sparkSession) => UIO.succeed(common.ApplicationContext(sparkSession, commandLineOptions)))
}
