package com.koala.importer.models

case class Stops(
                  stopId: String,
                  stopCode: String,
                  stopName: String,
                  stopLat: String,
                  stopLon: String,
                  stopUrl: String,
                  locationType: String,
                  parentStation: String,
                  wheelchairBoarding: String
                )
