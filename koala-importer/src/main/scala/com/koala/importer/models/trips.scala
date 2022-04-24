package com.koala.importer.models

case class trips(
                  routeId: String,
                  serviceId: String,
                  tripId: String,
                  tripHeadsign: String,
                  directionId: String,
                  shapeId: String,
                  wheelchairAccessible: String,
                  noteFr: String,
                  noteEn: String
                )
