package pl.mlkmn;

import com.wrapper.spotify.models.Track;
import org.apache.commons.math.util.MathUtils;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

import java.util.Map;
import java.util.Set;

public class SpotifyDownloaderHelper {
    
    public static int convertISO8601TimeToSeconds(String timeInISO8601) {
        PeriodFormatter formatter = ISOPeriodFormat.standard();
        Period period = formatter.parsePeriod(timeInISO8601);
        Seconds seconds = period.toStandardSeconds();

        return seconds.getSeconds();
    }
    
    public static String findClosestNumber(int numberToFind, Map<String,Integer> mapToScan) {
        String bestCandidate = "";
        int leastDifference = Integer.MAX_VALUE;
        int difference;
        Set<Map.Entry<String, Integer>> entries = mapToScan.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            int entryValue = entry.getValue();
            String entryKey = entry.getKey();
            if (entryValue == numberToFind) {
                return entryKey;
            } else {
                difference = Math.abs(entryValue - numberToFind);
                if (difference < leastDifference) {
                    leastDifference = difference;
                    bestCandidate = entryKey;
                }
            }
        }
        return bestCandidate;
    }
    
    public static int getRoundedUpTrackDurationInSeconds(Track track) {
        return (int) MathUtils.round((double) track.getDuration(), -3)/1000;
    }
}
