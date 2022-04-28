package com.koala.importer.models.gtfs

case class Routes(
                   route_id: String,
                   agency_id: String,
                   route_short_name: String,
                   route_long_name: String,
                   route_type: String,
                   route_url: String,
                   route_color: String,
                   route_text_color: String
                 ) extends GTFS
