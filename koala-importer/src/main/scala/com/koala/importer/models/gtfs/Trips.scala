package com.koala.importer.models.gtfs

case class Trips(
                  routeId: String,
                  serviceId: String,
                  tripId: String,
                  tripHeadsign: String,
                  directionId: String,
                  shapeId: String,
                  wheelchairAccessible: String,
                  noteFr: String,
                  noteEn: String
                ) extends STM
