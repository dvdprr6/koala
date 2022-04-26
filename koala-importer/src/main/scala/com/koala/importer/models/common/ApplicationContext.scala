package com.koala.importer.models.common

import org.apache.spark.sql.SparkSession

case class ApplicationContext(sparkSession: SparkSession, commandLineOptions: CommandLineOptions)
