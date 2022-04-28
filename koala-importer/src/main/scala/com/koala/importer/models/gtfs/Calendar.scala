package com.koala.importer.models.gtfs

case class Calendar(
                     service_id: String,
                     monday: String,
                     tuesday: String,
                     wednesday: String,
                     thursday: String,
                     friday: String,
                     saturday: String,
                     sunday: String,
                     start_date: String,
                     end_date: String
                   ) extends GTFS
