package com.koala.importer.models.enrichment

case class FrequenciesTransform(
                                 tripId: String,
                                 startTime: String,
                                 endTime: String,
                                 headwaySecs: String,
                                 partitionDate: String
                               ) extends Transform
