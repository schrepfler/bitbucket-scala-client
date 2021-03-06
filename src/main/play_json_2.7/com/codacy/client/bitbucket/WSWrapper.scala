package com.codacy.client.bitbucket

object WSWrapper {
  type WSClient = play.api.libs.ws.StandaloneWSClient
  type WSRequest = play.api.libs.ws.StandaloneWSRequest

  def withQueryString(request: WSRequest, parameters: (String, String)*): WSRequest = {
    request.withQueryStringParameters(parameters: _*)
  }

  def build(materializer: akka.stream.Materializer): WSClient = {
    play.api.libs.ws.ahc.StandaloneAhcWSClient()(materializer)
  }

}
