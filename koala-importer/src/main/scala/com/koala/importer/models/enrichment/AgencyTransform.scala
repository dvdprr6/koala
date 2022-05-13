package com.koala.importer.models.enrichment

case class AgencyTransform(
                            agencyId: String,
                            agencyName: String,
                            agencyUrl: String,
                            agencyTimezone: String,
                            agencyLang: String,
                            agencyPhone: String,
                            agencyFareUrl: String,
                            partitionDate: String
                          ) extends Transform
