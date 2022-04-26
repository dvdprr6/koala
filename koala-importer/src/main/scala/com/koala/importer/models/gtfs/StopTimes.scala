package com.koala.importer.models.gtfs

case class StopTimes(
                      tripId: String,
                      arrivalTime: String,
                      departureTime: String,
                      stopId: String,
                      stopSequence: String
                    ) extends STM
