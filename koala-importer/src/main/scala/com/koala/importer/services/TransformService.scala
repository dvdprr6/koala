package com.koala.importer.services

import zio.{Has, UIO, URIO, ZEnv, ZIO, ZLayer}

object TransformService {
  type TransformServiceEnv = Has[TransformService.Service]

  trait Service{
    def transform(): UIO[Unit]
  }

  def transform(): URIO[TransformServiceEnv, Unit] =
    ZIO.accessM(_.get.transform())

  lazy val live: ZLayer[ZEnv, Nothing, TransformServiceEnv] =
    ZLayer.succeed(() => ZIO.succeed())

}
