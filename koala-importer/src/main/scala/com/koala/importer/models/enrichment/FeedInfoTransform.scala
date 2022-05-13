package com.koala.importer.models.enrichment

case class FeedInfoTransform(
                              feedPublisherName: String,
                              feedPublisherUrl: String,
                              feedLang: String,
                              feedStartDate: String,
                              feedEndDate: String,
                              partitionDate: String
                            ) extends Transform
