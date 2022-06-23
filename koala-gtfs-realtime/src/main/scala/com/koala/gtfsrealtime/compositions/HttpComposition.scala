package com.koala.gtfsrealtime.compositions

import com.koala.gtfsrealtime.models.ApplicationContext
import com.koala.gtfsrealtime.services.HttpService
import com.koala.gtfsrealtime.services.HttpService.HttpServiceEnv
import io.netty.channel.{Channel, ServerChannel, ChannelFactory => JChannelFactory, EventLoopGroup => JEventLoopGroup}
import zhttp.service.{ChannelFactory, EventLoopGroup}
import zio.{Has, RIO, RLayer, Task, ZEnv, ZIO, ZLayer}

object HttpComposition {
  type HttpCompositionEnv = Has[HttpComposition.Service]

  class Service(httpService: HttpService.Service){

    def getRequest(applicationContext: ApplicationContext): RIO[ZEnv, Array[Byte]] = {
      val apikey = applicationContext.commandLineOptions.apikey
      val url ="https://api.stm.info/pub/od/gtfs-rt/ic/v2/vehiclePositions"

      for{
        response <- httpService.getRequest(apikey, url)
        body <- response.bodyAsByteArray
      } yield body
    }
  }

  def getRequest(applicationContext: ApplicationContext): RIO[ZEnv with HttpCompositionEnv, Array[Byte]] =
    ZIO.accessM(_.get[Service].getRequest(applicationContext))

  lazy val live: RLayer[HttpServiceEnv, HttpCompositionEnv] =
    ZLayer.fromService(httpService => new Service(httpService))
//    ZLayer.fromServices[HttpService.Service, ChannelFactory.Live, EventLoopGroup.Live, HttpComposition.Service] =
//    ZLayer.fromService(httpService => new Service(httpService))

}
