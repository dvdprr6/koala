package com.koala.gtfsrealtime.services

import zhttp.http.{Headers, Method, Response}
import zhttp.service.{ChannelFactory, Client, EventLoopGroup}
import zio.{Has, RIO, RLayer, ZEnv, ZIO, ZLayer}

object HttpService {
  type HttpServiceEnv = Has[HttpService.Service]

  trait Service{
    def getRequest(apiKey: String, url: String): RIO[ChannelFactory with EventLoopGroup, Response]
  }

  def getRequest(apiKey: String, url: String): RIO[HttpServiceEnv with ChannelFactory with EventLoopGroup, Response] =
    ZIO.accessM(_.get.getRequest(apiKey, url))

  lazy val live: RLayer[ZEnv with ChannelFactory with EventLoopGroup, HttpServiceEnv] =
    ZLayer.succeed((apikey, url) =>{
      val headers = Headers.apply("apikey", apikey)

      for{
        response <- Client.request(url = url, method = Method.GET, headers = headers)
      } yield response
    })
}
