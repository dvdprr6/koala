package com.koala.importer.models

case class stopTimes(
                      tripId: String,
                      arrivalTime: String,
                      departureTime: String,
                      stopId: String,
                      stopSequence: String
                    )
