package com.koala.importer.models.gtfs

case class Routes(
                   routeId: String,
                   agencyId: String,
                   routeShortName: String,
                   routeLongName: String,
                   routeType: String,
                   routeUrl: String,
                   routeColor: String,
                   routeTextColor: String
                 ) extends STM
