package com.koala.importer.models.gtfs

case class Shapes(
                   shapeId: String,
                   shapePtLat: String,
                   shapePtLon: String,
                   shapePtSequence: String
                 ) extends STM
