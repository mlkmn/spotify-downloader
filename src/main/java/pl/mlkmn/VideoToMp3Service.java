package pl.mlkmn;

public class VideoToMp3Service {

    private String prefix;
    
    private String suffix;
    
    public String getDownloadUrl(String youtubeVideoId) {
        return prefix + youtubeVideoId + suffix;
    }
    
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
