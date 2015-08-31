package pl.mlkmn;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.wrapper.spotify.models.Track;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YoutubeService implements InitializingBean {
    
    private final static String YOUTUBE_SEARCH_PART = "id,snippet";
    private final static String YOUTUBE_SEARCH_TYPE = "video";
    private final static String YOUTUBE_SEARCH_FIELDS = "items(id/kind,id/videoId,snippet/title," +
            "snippet/thumbnails/default/url)";
    private final static Long YOUTUBE_MAX_RESULTS = 10L;
    private final static String YOUTUBE_VIDEOS_PART = "contentDetails";
    private final static int ACCEPTABLE_DURATION_DIFFERENCE = 10;
    
    
    private String youtubeApplicationName;
    private String youtubeApiKey;
    private String youtubeUrlPrefix;

    private YouTube youtube;

    public String getBestVideoIdForTrack(Track track) throws IOException {
        Map<String, Integer> youtubeVideoDurationMap = new HashMap<String, Integer>();
        int trackDuration = SpotifyDownloaderHelper.getRoundedUpTrackDurationInSeconds(track);
        String youtubeQuery = getYoutubeQueryForTrack(track);

        YouTube.Search.List search = prepareYoutubeSearch(youtubeQuery);
        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResponseItems = searchResponse.getItems();
        if (!searchResponseItems.isEmpty()) {
            for (SearchResult searchResult : searchResponseItems) {
                String searchResultVideoId = searchResult.getId().getVideoId();
                Integer searchResultVideoDuration = getYoutubeVideoDuration(searchResultVideoId);
                if (Math.abs(trackDuration - searchResultVideoDuration) < ACCEPTABLE_DURATION_DIFFERENCE) {
                    return searchResultVideoId;
                }
                youtubeVideoDurationMap.put(searchResultVideoId, searchResultVideoDuration);
            }
            return SpotifyDownloaderHelper.findClosestNumber(trackDuration, youtubeVideoDurationMap);
        } else {
            System.out.println("No videos found for query: " + youtubeQuery);
            return "";
        }
    }

    public int getYoutubeVideoDuration(String youtubeVideoId) throws IOException {
        YouTube.Videos.List videos = prepareYoutubeVideos(youtubeVideoId);
        VideoListResponse videoListResponse = videos.execute();
        List<Video> videoListResponseItems = videoListResponse.getItems();
        if (!videoListResponseItems.isEmpty()) {
            String videoDurationInISO8601Format = videoListResponseItems.get(0).getContentDetails().getDuration();
            return SpotifyDownloaderHelper.convertISO8601TimeToSeconds(videoDurationInISO8601Format);
        } else {
            System.out.println("No video found for id: " + youtubeVideoId);
            return 0;
        }
    }

    private YouTube.Videos.List prepareYoutubeVideos(String youtubeVideoId) throws IOException {
        YouTube.Videos.List videos = youtube.videos().list(YOUTUBE_VIDEOS_PART);
        videos.setId(youtubeVideoId);
        videos.setKey(youtubeApiKey);
        videos.setPart(YOUTUBE_VIDEOS_PART);
        videos.setMaxResults(YOUTUBE_MAX_RESULTS);
        return videos;
    }

    private YouTube.Search.List prepareYoutubeSearch(String youtubeQuery) throws IOException {
        YouTube.Search.List search = youtube.search().list(YOUTUBE_SEARCH_PART);
        search.setKey(youtubeApiKey);
        search.setQ(youtubeQuery);
        search.setType(YOUTUBE_SEARCH_TYPE);
        search.setFields(YOUTUBE_SEARCH_FIELDS);
        search.setMaxResults(YOUTUBE_MAX_RESULTS);
        return search;
    }

    public String getYoutubeVideoUrl(String youtubeVideoId) {
        return youtubeUrlPrefix + youtubeVideoId;
    }

    public String getYoutubeQueryForTrack(Track track) {
        String trackName = track.getName();
        String trackAuthors = track.getArtists().get(0).getName();
        return trackAuthors + " - " + trackName;
    }

    public void setYoutubeApplicationName(String youtubeApplicationName) {
        this.youtubeApplicationName = youtubeApplicationName;
    }

    public void setYoutubeApiKey(String youtubeApiKey) {
        this.youtubeApiKey = youtubeApiKey;
    }

    public void setYoutubeUrlPrefix(String youtubeUrlPrefix) {
        this.youtubeUrlPrefix = youtubeUrlPrefix;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName(youtubeApplicationName).build();
    }
}
