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
				<div class="text-center">
					<button class="btn-large btn" type="button" onclick="location.href = '${authorizeUrl}';">Login to
						Spotify</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>