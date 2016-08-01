package org.hyperspray.cj

sealed trait Issue {
  def error: String
}

case class MissingFieldIssue(message: String) extends Issue {
  override lazy val error = s"Missing field ($message)"
}