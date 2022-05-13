package com.koala.importer.models.enrichment

case class ShapesTransform(
                            shapeId: String,
                            shapePtLat: String,
                            shapePtLon: String,
                            shapePtSequence: String,
                            partitionDate: String
                          ) extends Transform
