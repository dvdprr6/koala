package com.koala.importer.models

case class fareAttributes(
                           fareId: String,
                           price: String,
                           currencyType: String,
                           paymentMethod: String,
                           transfers: String,
                           transferDuration: String
                         )
