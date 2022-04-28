package com.koala.importer.models.gtfs

case class FeedInfo(
                     feed_publisher_name: String,
                     feed_publisher_url: String,
                     feed_lang: String,
                     feed_start_date: String,
                     feed_end_date: String
                   ) extends GTFS
