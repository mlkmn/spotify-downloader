package pl.mlkmn;

import com.wrapper.spotify.Api;

public class SpotifyApiFactory {
    
    private String spotifyUserId;
    private String spotifyUserSecret;
    private String spotifyRedirectUri;

    public Api getApi() {
        return Api.builder()
                .clientId(spotifyUserId)
                .clientSecret(spotifyUserSecret)
                .redirectURI(spotifyRedirectUri)
                .build();
    }

    public void setSpotifyUserId(String spotifyUserId) {
        this.spotifyUserId = spotifyUserId;
    }

    public void setSpotifyUserSecret(String spotifyUserSecret) {
        this.spotifyUserSecret = spotifyUserSecret;
    }

    public void setSpotifyRedirectUri(String spotifyRedirectUri) {
        this.spotifyRedirectUri = spotifyRedirectUri;
    }
}
