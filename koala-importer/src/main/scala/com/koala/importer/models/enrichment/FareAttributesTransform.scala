package com.koala.importer.models.enrichment

case class FareAttributesTransform(
                                    fareId: String,
                                    price: String,
                                    currencyType: String,
                                    paymentMethod: String,
                                    transfers: String,
                                    transferDuration: String,
                                    partitionDate: String
                                  ) extends Transform
