package com.koala.importer.models.gtfs

case class StopTimes(
                      trip_id: String,
                      arrival_time: String,
                      departure_time: String,
                      stop_id: String,
                      stop_sequence: String
                    ) extends GTFS
