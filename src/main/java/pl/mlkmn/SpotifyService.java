package pl.mlkmn;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.CurrentUserRequest;
import com.wrapper.spotify.methods.UserPlaylistsRequest;
import com.wrapper.spotify.models.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpotifyService {

    @Resource(name = "spotifyServiceScopes")
    private List<String> scopes;

    private String state;
    
    public List<Track> getAllTracksFromPlaylist(Api api, String ownerId, String playlistId) throws IOException,
            WebApiException {
        Playlist parsedPlaylist = api.getPlaylist(ownerId, playlistId).build().get();
        List<PlaylistTrack> playlistTracks = parsedPlaylist.getTracks().getItems();
        List<Track> tracks = new ArrayList<Track>();
        for (PlaylistTrack playlistTrack : playlistTracks) {
            tracks.add(playlistTrack.getTrack());
        }
        return tracks;
    }

    public String getCreatedAuthorizeUrl(Api api) {
        return api.createAuthorizeURL(scopes, state);
    }
    
    public List<SimplePlaylist> getUserSimplePlaylists(Api api) throws IOException, WebApiException {
        String userId = getUserId(api);
        UserPlaylistsRequest userPlaylistsRequest = api.getPlaylistsForUser(userId).build();
        Page<SimplePlaylist> playlistsPage = userPlaylistsRequest.get();
        return playlistsPage.getItems();
    }
    
    public String getUserId(Api api) throws IOException, WebApiException {
        CurrentUserRequest currentUserRequest = api.getMe().build();
        User user = currentUserRequest.get();
        return user.getId();
    }

    public String getPlaylistName(Api api, String ownerId, String playlistId) throws IOException, WebApiException {
        Playlist parsedPlaylist = api.getPlaylist(ownerId, playlistId).build().get();
        return parsedPlaylist.getName();
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }
}
