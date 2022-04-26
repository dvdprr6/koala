package com.koala.importer.models.gtfs

case class Agency(
                   agencyId: String,
                   agencyName: String,
                   agencyUrl: String,
                   agencyTimezone: String,
                   agencyLang: String,
                   agencyPhone: String,
                   agencyFareUrl: String
                 ) extends STM
