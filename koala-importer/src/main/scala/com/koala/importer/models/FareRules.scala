package com.koala.importer.models

case class FareRules(
                      fareId: String,
                      routeId: String,
                      originId: String,
                      destinationId: String,
                      containsId: String
                    )
