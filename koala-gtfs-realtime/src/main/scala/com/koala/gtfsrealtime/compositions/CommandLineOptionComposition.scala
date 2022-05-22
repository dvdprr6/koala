package com.koala.gtfsrealtime.compositions

import com.koala.gtfsrealtime.models.CommandLineOptions
import com.koala.gtfsrealtime.services.CommandLineOptionService
import com.koala.gtfsrealtime.services.CommandLineOptionService.CommandLineOptionServiceEnv
import zio.{Has, UIO, URIO, URLayer, ZIO, ZLayer}
import zio.cli.Command

object CommandLineOptionComposition {
  type CommandLineOptionCompositionEnv = Has[CommandLineOptionComposition.Service]

  class Service(commandLineOptionService: CommandLineOptionService.Service){
    def parse(): UIO[Command[CommandLineOptions]] = {
      for{
        command <- commandLineOptionService.parseCommandLineOptions()
      } yield command
    }
  }

  def parse(): URIO[CommandLineOptionCompositionEnv, Command[CommandLineOptions]] =
    ZIO.accessM(_.get.parse())

  lazy val live: URLayer[CommandLineOptionServiceEnv, CommandLineOptionCompositionEnv] =
    ZLayer.fromService(commandLineOptionService => new Service(commandLineOptionService))

}
