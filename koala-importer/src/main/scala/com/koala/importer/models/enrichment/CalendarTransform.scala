package com.koala.importer.models.enrichment

case class CalendarTransform(
                              serviceId: String,
                              monday: String,
                              tuesday: String,
                              wednesday: String,
                              thursday: String,
                              friday: String,
                              saturday: String,
                              startDate: String,
                              partitionDate: String
                            ) extends Transform
