package com.koala.importer.services

import com.koala.importer.models.common.CommandLineOptions
import zio.{Has, RIO, Task, ZEnv, ZIO, ZLayer}
import zio.cli.{Command, Options}

object CommandLineOptionService {
  type CommandLineOptionServiceEnv = Has[CommandLineOptionService.Service]

  trait Service{
    def parseCommandLineOptions(): Task[Command[CommandLineOptions]]
  }

  private val input: Options[String] = Options.text("input")
  private val date: Options[String] = Options.text("date")

  def parseCommandLineOptions(): RIO[CommandLineOptionServiceEnv, Command[CommandLineOptions]] =
    ZIO.accessM(_.get.parseCommandLineOptions())

  lazy val live: ZLayer[ZEnv, Nothing, CommandLineOptionServiceEnv] =
    ZLayer.succeed(() => {
      Task {
        val options = (input ++ date).as(CommandLineOptions)
        Command("Koala Importer", options)
      }
    })
}
