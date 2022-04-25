package com.koala.importer.models

case class StopTimes(
                      tripId: String,
                      arrivalTime: String,
                      departureTime: String,
                      stopId: String,
                      stopSequence: String
                    )
