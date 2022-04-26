package com.koala.importer.models.gtfs

case class FareAttributes(
                           fareId: String,
                           price: String,
                           currencyType: String,
                           paymentMethod: String,
                           transfers: String,
                           transferDuration: String
                         ) extends STM
