package com.koala.importer.models.enrichment

case class RoutesTransform(
                            routeId: String,
                            agencyId: String,
                            routeShortName: String,
                            routeLongName: String,
                            routeType: String,
                            routeUrl: String,
                            routeColor: String,
                            routeTextColor: String,
                            partitionDate: String
                          ) extends Transform
