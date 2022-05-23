package com.koala.gtfsrealtime.models

import org.apache.spark.sql.SparkSession

case class ApplicationContext(commandLineOptions: CommandLineOptions, sparkSession: SparkSession)
