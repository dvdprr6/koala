package com.koala.importer.services

import com.koala.importer.models.{ApplicationContext, CommandLineOptions}
import org.apache.spark.sql.SparkSession
import zio.{Has, UIO, URIO, ZEnv, ZIO, ZLayer}

object ApplicationContextService {
  type ApplicationContextEnv = Has[ApplicationContextService.Service]

  trait Service{
    def getApplicationContext(commandLineOptions: CommandLineOptions, sparkSession: SparkSession): UIO[ApplicationContext]
  }

  def getApplicationContext(commandLineOptions: CommandLineOptions, sparkSession: SparkSession): URIO[ApplicationContextEnv, ApplicationContext] =
    ZIO.accessM(_.get.getApplicationContext(commandLineOptions, sparkSession))

  lazy val live: ZLayer[ZEnv, Nothing, ApplicationContextEnv] =
    ZLayer.succeed((commandLineOptions, sparkSession) => UIO.succeed(ApplicationContext(sparkSession, commandLineOptions)))
}
