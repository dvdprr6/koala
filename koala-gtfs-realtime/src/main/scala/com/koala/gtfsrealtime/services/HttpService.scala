package com.koala.gtfsrealtime.services

import zhttp.http.{Headers, Method, Response}
import zhttp.service.{ChannelFactory, Client, EventLoopGroup}
import zio.{Has, RIO, RLayer, Task, ZEnv, ZIO, ZLayer}

object HttpService {
  type HttpServiceEnv = Has[HttpService.Service]

  trait Service{
    def getRequest(apiKey: String, url: String): RIO[ZEnv, Response]
  }

  def getRequest(apiKey: String, url: String): RIO[ZEnv with HttpServiceEnv, Response] =
    ZIO.accessM(_.get[HttpService.Service].getRequest(apiKey, url))

  lazy val live: RLayer[ZEnv, HttpServiceEnv] =
    ZLayer.succeed((apikey, url) =>{
      val headers = Headers.apply("apikey", apikey)

      for{
        response <- Client.request(url = url, method = Method.GET, headers = headers).provideCustomLayer(ChannelFactory.auto ++ EventLoopGroup.auto())
      } yield response
    })
}
