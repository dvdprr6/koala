package com.koala.importer.models.gtfs

case class Agency(
                   agency_id: String,
                   agency_name: String,
                   agency_url: String,
                   agency_timezone: String,
                   agency_lang: String,
                   agency_phone: String,
                   agency_fare_url: String
                 ) extends GTFS
