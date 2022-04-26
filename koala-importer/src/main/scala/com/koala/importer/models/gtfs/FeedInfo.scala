package com.koala.importer.models.gtfs

case class FeedInfo(
                     feedPublisherName: String,
                     feedPublisherUrl: String,
                     feedLang: String,
                     feedStartDate: String,
                     feedEndDate: String
                   ) extends STM
