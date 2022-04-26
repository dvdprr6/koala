package com.koala.importer.models.gtfs

case class Frequencies(
                        tripId: String,
                        startTime: String,
                        endTime: String,
                        headwaySecs: String
                      ) extends STM
