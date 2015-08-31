package pl.mlkmn;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.models.Track;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpotifyDownloaderService {

    @Autowired
    private YoutubeService youtubeService;
    @Autowired
    private SpotifyService spotifyService;
    @Autowired
    private VideoToMp3Service videoToMp3Service;

    public List<SpotifyDownloaderResult> getResultsForPlaylist(Api api, String ownerId, String playlistId) throws IOException, WebApiException {
        List<SpotifyDownloaderResult> spotifyDownloaderResults = new ArrayList<SpotifyDownloaderResult>();
        List<Track> tracks = spotifyService.getAllTracksFromPlaylist(api, ownerId, playlistId);
        for (Track track : tracks) {
            SpotifyDownloaderResult spotifyDownloaderResult = new SpotifyDownloaderResult();
            String youtubeVideoId = youtubeService.getBestVideoIdForTrack(track);
            String trackDownloadUrl = videoToMp3Service.getDownloadUrl(youtubeVideoId);
            spotifyDownloaderResult.setName(youtubeService.getYoutubeQueryForTrack(track));
            spotifyDownloaderResult.setYoutubeUrl(youtubeService.getYoutubeVideoUrl(youtubeVideoId));
            spotifyDownloaderResult.setDownloadUrl(trackDownloadUrl);
            spotifyDownloaderResults.add(spotifyDownloaderResult);
        }
        return spotifyDownloaderResults;
    }
    
    public void setYoutubeService(YoutubeService youtubeService) {
        this.youtubeService = youtubeService;
    }

    public void setSpotifyService(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    public void setVideoToMp3Service(VideoToMp3Service videoToMp3Service) {
        this.videoToMp3Service = videoToMp3Service;
    }
}
