package com.koala.importer.models.enrichment

case class TripsTransform(
                           routeId: String,
                           serviceId: String,
                           tripId: String,
                           tripHeadsign: String,
                           directionId: String,
                           shapeId: String,
                           wheelchairAccessible: String,
                           noteFr: String,
                           noteEn: String,
                           partitionDate: String
                         ) extends Transform
