package com.koala.importer.models.gtfs

case class Shapes(
                   shape_id: String,
                   shape_pt_lat: String,
                   shape_pt_lon: String,
                   shape_pt_sequence: String
                 ) extends GTFS
