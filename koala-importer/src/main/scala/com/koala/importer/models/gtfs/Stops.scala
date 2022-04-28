package com.koala.importer.models.gtfs

case class Stops(
                  stop_id: String,
                  stop_code: String,
                  stop_name: String,
                  stop_lat: String,
                  stop_lon: String,
                  stop_url: String,
                  location_type: String,
                  parent_station: String,
                  wheelchair_boarding: String
                ) extends GTFS
