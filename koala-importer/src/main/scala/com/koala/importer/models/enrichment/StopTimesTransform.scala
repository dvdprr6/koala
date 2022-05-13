package com.koala.importer.models.enrichment

case class StopTimesTransform(
                               tripId: String,
                               arrivalTime: String,
                               departureTime: String,
                               stopId: String,
                               stopSequence: String,
                               partitionDate: String
                             ) extends Transform
