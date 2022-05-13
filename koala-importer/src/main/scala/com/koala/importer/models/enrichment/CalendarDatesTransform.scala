package com.koala.importer.models.enrichment

case class CalendarDatesTransform (
                                    serviceId: String,
                                    date: String,
                                    exceptionType: String,
                                    partitionDate: String
                                  ) extends Transform
