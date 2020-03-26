package com.codacy.client.bitbucket.v2.service

import java.net.URLEncoder

import com.codacy.client.bitbucket.client.{BitbucketClient, PageRequest, Request, RequestResponse}
import com.codacy.client.bitbucket.v2.{Team, UserPermission}

class TeamServices(client: BitbucketClient) {

  private val BaseUrl = "https://bitbucket.org/api/2.0/teams"

  def list(role: String = "member", pageRequest: Option[PageRequest] = None): RequestResponse[Seq[Team]] = {
    pageRequest match {
      case Some(request) =>
        request.cursor match {
          case Some(cursor) => client.executeWithCursor(Request(cursor, classOf[Team]))
          case None => client.executeWithCursor(Request(s"$BaseUrl?role=$role", classOf[Team]))
        }
      case None =>
        client.executePaginated(Request(s"$BaseUrl?role=$role", classOf[Seq[Team]]))
    }
  }

  /**
    * Gets the public information associated with a team.
    *
    * @param username The username or the UUID of the account surrounded by curly-braces
    */
  def getTeam(username: String): RequestResponse[Team] = {
    val encodedUsername = URLEncoder.encode(username, "UTF-8")
    client.execute(Request(s"$BaseUrl/$encodedUsername", classOf[Team]))
  }

  /**
    * Retrieve the permissions matching the supplied username and repositorySlug.
    *
    * @param username The username or the UUID of the account surrounded by curly-braces
    * @param repositorySlug The repository slug or the UUID of the repository surrounded by curly-braces
    * @return A [[RequestResponse]] with the user permissions for the repository
    */
  def getRepositoryUserPermissions(
      username: String,
      repositorySlug: String,
      pageRequest: Option[PageRequest] = None
  ): RequestResponse[Seq[UserPermission]] = {
    pageRequest match {
      case Some(request) =>
        request.cursor match {
          case Some(cursor) => client.executeWithCursor(Request(cursor, classOf[UserPermission]))
          case None =>
            client.executeWithCursor(
              Request(s"$BaseUrl/$username/permissions/repositories/$repositorySlug", classOf[UserPermission])
            )
        }
      case None =>
        client.executePaginated(
          Request(s"$BaseUrl/$username/permissions/repositories/$repositorySlug", classOf[Seq[UserPermission]])
        )
    }
  }
}
