package org.collectionjson

sealed trait Issue {
  def error: String
}

case class MissingFieldIssue() extends Issue {
  override lazy val error = "Missing field"
}