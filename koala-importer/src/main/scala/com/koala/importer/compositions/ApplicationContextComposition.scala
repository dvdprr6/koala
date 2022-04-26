package com.koala.importer.compositions

import com.koala.importer.models.common.{ApplicationContext, CommandLineOptions}
import com.koala.importer.services.ApplicationContextService.ApplicationContextServiceEnv
import com.koala.importer.services.SparkConnectionService.SparkConnectionServiceEnv
import com.koala.importer.services.{ApplicationContextService, SparkConnectionService}
import zio.{Has, RIO, Task, ZIO, ZLayer}

object ApplicationContextComposition {
  type ApplicationContextCompositionEnv = Has[ApplicationContextComposition.Service]

  class Service(sparkConnectionService: SparkConnectionService.Service, applicationContextService: ApplicationContextService.Service){
    def build(commandLineOptions: CommandLineOptions): Task[ApplicationContext] =
      for{
        sparkSession <- sparkConnectionService.getSparkSession()
        applicationContext <- applicationContextService.getApplicationContext(commandLineOptions, sparkSession)
      } yield applicationContext
  }

  def build(commandLineOptions: CommandLineOptions): RIO[ApplicationContextCompositionEnv, ApplicationContext] =
    ZIO.accessM(_.get.build(commandLineOptions))

  lazy val live: ZLayer[SparkConnectionServiceEnv with ApplicationContextServiceEnv, Nothing, ApplicationContextCompositionEnv] =
    ZLayer.fromServices[SparkConnectionService.Service, ApplicationContextService.Service, ApplicationContextComposition.Service]{
      (sparkConnectionService, applicationContextService) => new Service(sparkConnectionService, applicationContextService)
    }
}
