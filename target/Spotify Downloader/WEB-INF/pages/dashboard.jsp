<!doctype html>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
  <meta charset="utf-8">
  <title>Spotify Downloader</title>

  <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <link href="<c:url value="/css/bootstrap.css" />" rel="stylesheet">
  <link href="<c:url value="/css/bootstrap-responsive.css" />" rel="stylesheet">
  <script src="<c:url value="/js/jquery-2.1.4.min.js" />"></script>
  <script src="<c:url value="/js/main.js" />"></script>
</head>
<body>
<div class="container">
  <div class="row">
    <div class="span8 offset2">
      <c:if test="${!empty userSimplePlaylists}">
        <h3>Playlists</h3>
        <table class="table table-bordered table-striped">
          <thead>
            <tr>
              <th>Name</th>
              <th>Size</th>
              <th>Download</th>
            </tr>
          </thead>
          <tbody>
          <c:forEach items="${userSimplePlaylists}" var="userSimplePlaylist">
            <tr>
              <td class="playlist-name">${userSimplePlaylist.name}</td>
              <td class="playlist-size">${userSimplePlaylist.tracks.total}</td>
              <td class="playlist_download-button"><button class="btn-mini btn" type="button" onclick="location.href =
              '/playlist?playlistId=${userSimplePlaylist.id}&ownerId=${userSimplePlaylist.owner.id}';">Download
              </button></td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </c:if>
      <div class="text-center">
        <button class="btn-large" type="button" onclick="logoutFromSpotifyInOtherTab()">Logout</button>
      </div>
    </div>
  </div>
</div>
</body>
</html>
