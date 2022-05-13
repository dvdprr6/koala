package com.koala.importer.models.enrichment

case class FareRulesTransform(fareId: String,
                              routeId: String,
                              originId: String,
                              destinationId: String,
                              containsId: String,
                              partitionDate: String
                             ) extends Transform
