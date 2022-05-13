package com.koala.importer.models.enrichment

case class StopsTransform(
                           stopId: String,
                           stopCode: String,
                           stopName: String,
                           stopLat: String,
                           stopLon: String,
                           stopUrl: String,
                           locationType: String,
                           parentStation: String,
                           wheelchairBoarding: String,
                           partitionDate: String
                         ) extends Transform
