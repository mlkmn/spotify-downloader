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
            <c:if test="${!empty results}">
                <h3>${playlistName}</h3>
                <table class="table table-bordered table-striped">
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>YouTube URL</th>
                        <th>Download URL</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${results}" var="result">
                        <tr>
                            <td class="track-name">${result.name}</td>
                            <td class="youtube-link"><a href=${result.youtubeUrl}>Show me the video on YT</a></td>
                            <td class="download-link"><a href=${result.downloadUrl}>Gimme that MP3</a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <div class="text-center">
                    <button class="btn-large" type="button" onclick="openAllDownloadLinks()">Open all download
                        links in separate tabs</button>
                    <button class="btn-large" type="button" onclick="location.href = '/';">Back to dashboard</button>
                </div>
            </c:if>
        </div>
    </div>
</div>
</body>
</html>