package com.koala.importer.models.gtfs

case class FareAttributes(
                           fare_id: String,
                           price: String,
                           currency_type: String,
                           payment_method: String,
                           transfers: String,
                           transfer_duration: String
                         ) extends GTFS
