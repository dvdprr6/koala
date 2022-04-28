package com.koala.importer.models.gtfs

case class Frequencies(
                        trip_id: String,
                        start_time: String,
                        end_time: String,
                        headway_secs: String
                      ) extends GTFS
