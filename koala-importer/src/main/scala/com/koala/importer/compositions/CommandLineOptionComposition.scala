package com.koala.importer.compositions

import com.koala.importer.models.common.CommandLineOptions
import com.koala.importer.services.CommandLineOptionService
import com.koala.importer.services.CommandLineOptionService.CommandLineOptionServiceEnv
import zio.{Has, Task, ZIO, ZLayer}
import zio.cli.Command

object CommandLineOptionComposition {
  type CommandLineOptionCompositionEnv = Has[CommandLineOptionComposition.Service]

  class Service(commandLineOptionService: CommandLineOptionService.Service){
    def parse(): Task[Command[CommandLineOptions]] = {
      for{
        command <- commandLineOptionService.parseCommandLineOptions()
      } yield command
    }
  }

  def parse(): ZIO[CommandLineOptionCompositionEnv, Throwable, Command[CommandLineOptions]] =
    ZIO.accessM(_.get.parse())

  lazy val live: ZLayer[CommandLineOptionServiceEnv, Nothing, CommandLineOptionCompositionEnv] =
    ZLayer.fromService(commandLineOptionService => new Service(commandLineOptionService))
}
