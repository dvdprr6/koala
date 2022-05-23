package com.koala.gtfsrealtime.compositions

import com.koala.gtfsrealtime.models.{ApplicationContext, CommandLineOptions}
import com.koala.gtfsrealtime.services.ApplicationContextService.ApplicationContextServiceEnv
import com.koala.gtfsrealtime.services.SparkConnectionService.SparkConnectionServiceEnv
import com.koala.gtfsrealtime.services.{ApplicationContextService, SparkConnectionService}
import zio.{Has, RIO, RLayer, Task, ZIO, ZLayer}

object ApplicationContextComposition {
  type ApplicationContextCompositionEnv = Has[ApplicationContextComposition.Service]

  class Service(applicationContextService: ApplicationContextService.Service, sparkConnectionService: SparkConnectionService.Service){
    def build(commandLineOptions: CommandLineOptions): Task[ApplicationContext] =
      for{
        sparkSession <- sparkConnectionService.getSparkSession()
        applicationContext <- applicationContextService.getApplicationContext(commandLineOptions, sparkSession)
      } yield applicationContext
  }

  def build(commandLineOptions: CommandLineOptions): RIO[ApplicationContextCompositionEnv, ApplicationContext] =
    ZIO.accessM(_.get.build(commandLineOptions))

  lazy val live: RLayer[ApplicationContextServiceEnv with SparkConnectionServiceEnv, ApplicationContextCompositionEnv] =
    ZLayer.fromServices[ApplicationContextService.Service, SparkConnectionService.Service, ApplicationContextComposition.Service]{
      (applicationContextService, sparkConnectionService) => new Service(applicationContextService, sparkConnectionService)
    }

}
