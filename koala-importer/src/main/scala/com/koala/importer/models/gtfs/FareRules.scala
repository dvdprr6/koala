package com.koala.importer.models.gtfs

case class FareRules(
                      fareId: String,
                      routeId: String,
                      originId: String,
                      destinationId: String,
                      containsId: String
                    ) extends STM
