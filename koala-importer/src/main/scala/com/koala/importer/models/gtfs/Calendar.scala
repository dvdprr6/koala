package com.koala.importer.models.gtfs

case class Calendar(
                     serviceId: String,
                     monday: String,
                     tuesday: String,
                     wednesday: String,
                     thursday: String,
                     friday: String,
                     saturday: String,
                     sunday: String,
                     startDate: String,
                     endDate: String
                   ) extends STM
