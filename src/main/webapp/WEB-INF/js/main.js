function openAllDownloadLinks() {
    var downloadLinks = document.querySelectorAll('.download-link > a');
    var downloadLinksSize = downloadLinks.length;
    for (i = 0; i < downloadLinksSize; i++) {
        window.open(downloadLinks[i].href);
    }
}

function logoutFromSpotifyInOtherTab() {
    window.open("/logout");
    window.location='https://www.spotify.com/pl/logout/';
}