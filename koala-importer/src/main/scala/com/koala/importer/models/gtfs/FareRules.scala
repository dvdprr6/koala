package com.koala.importer.models.gtfs

case class FareRules(
                      fare_id: String,
                      route_id: String,
                      origin_id: String,
                      destination_id: String,
                      contains_id: String
                    ) extends GTFS
