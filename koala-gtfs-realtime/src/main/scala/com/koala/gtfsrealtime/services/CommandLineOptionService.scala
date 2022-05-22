package com.koala.gtfsrealtime.services

import com.koala.gtfsrealtime.models.CommandLineOptions
import zio.{Has, UIO, URIO, URLayer, ZEnv, ZIO, ZLayer}
import zio.cli.{Command, Options}

object CommandLineOptionService {
  type CommandLineOptionServiceEnv = Has[CommandLineOptionService.Service]

  trait Service{
    def parseCommandLineOptions(): UIO[Command[CommandLineOptions]]
  }

  private val apikey: Options[String] = Options.text("apikey")
  private val placeholder: Options[Option[String]] = Options.text("placeholder").optional("Another option for using 'as' in zio cli")

  def parseCommandLineOptions(): URIO[CommandLineOptionServiceEnv, Command[CommandLineOptions]] =
    ZIO.accessM(_.get.parseCommandLineOptions())

  lazy val live: URLayer[ZEnv, CommandLineOptionServiceEnv] =
    ZLayer.succeed(() => {
      UIO{
        val options = (apikey ++ placeholder).as(CommandLineOptions)
        Command("Koala GTFS Realtime", options)
      }
    })

}
