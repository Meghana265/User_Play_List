package Assignemt;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class exetntreport {
    private static final int CAPACITY = 3;
    private static ExtentReports extent;
    private static ExtentTest test;

    @BeforeSuite
    public void setUp() {
        // Initialize ExtentReports and ExtentHtmlReporter
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter("ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
    }

    @AfterSuite
    public void tearDown() {
        // Flush the extent report
        extent.flush();
    }

    @Test
    public void testRecentlyPlayedSongs() {
        test = extent.createTest("testRecentlyPlayedSongs", "Test recently played songs");
        RecentlyPlayedSongs store = new RecentlyPlayedSongs(CAPACITY);
        store.addSong("user1", "S1");
        store.addSong("user1", "S2");
        store.addSong("user1", "S3");
        Assert.assertEquals(store.getRecentlyPlayedSongs("user1"), new String[]{"S1", "S2", "S3"});
        test.pass("Recently played songs test passed.");

        store.addSong("user1", "S4");
        Assert.assertEquals(store.getRecentlyPlayedSongs("user1"), new String[]{"S2", "S3", "S4"});
        test.pass("Recently played songs test passed.");

        store.addSong("user1", "S2");
        Assert.assertEquals(store.getRecentlyPlayedSongs("user1"), new String[]{"S3", "S4", "S2"});
        test.pass("Recently played songs test passed.");

        store.addSong("user1", "S1");
        Assert.assertEquals(store.getRecentlyPlayedSongs("user1"), new String[]{"S4", "S2", "S1"});
        test.pass("Recently played songs test passed.");
    }

    @Test
    public void testAddSong() {
        test = extent.createTest("testAddSong", "Test adding a song");
        RecentlyPlayedSongs store = new RecentlyPlayedSongs(CAPACITY);
        store.addSong("user1", "S1");
        Assert.assertEquals(store.getRecentlyPlayedSongs("user1"), new String[]{"S1"});
        test.pass("Add song test passed.");

        store.addSong("user1", "S2");
        Assert.assertEquals(store.getRecentlyPlayedSongs("user1"), new String[]{"S1", "S2"});
        test.pass("Add song test passed.");

        store.addSong("user1", "S3");
        Assert.assertEquals(store.getRecentlyPlayedSongs("user1"), new String[]{"S1", "S2", "S3"});
        test.pass("Add song test passed.");
    }

    @Test
    public void testDeleteSong() {
        test = extent.createTest("testDeleteSong", "Test deleting a song");
        RecentlyPlayedSongs store = new RecentlyPlayedSongs(CAPACITY);
        store.addSong("user1", "S1");
        store.addSong("user1", "S2");
        store.addSong("user1", "S3");
        Assert.assertEquals(store.getRecentlyPlayedSongs("user1"), new String[]{"S1", "S2", "S3"});
        test.pass("Delete song test passed.");

        store.deleteSong("user1", "S2");
        Assert.assertEquals(store.getRecentlyPlayedSongs("user1"), new String[]{"S1", "S3"});
        test.pass("Delete song test passed.");

        store.deleteSong("user1", "S3");
        Assert.assertEquals(store.getRecentlyPlayedSongs("user1"), new String[]{"S1"});
        test.pass("Delete song test passed.");

        store.deleteSong("user1", "S1");
        Assert.assertEquals(store.getRecentlyPlayedSongs("user1"), new String[]{});
        test.pass("Delete song test passed.");
    }


    public class RecentlyPlayedSongs {
        private final int capacity;
        private final Map<String, List<String>> userToSongsMap;

        public RecentlyPlayedSongs(int capacity) {
            this.capacity = capacity;
            this.userToSongsMap = new HashMap<>();
        }

        public void addSong(String user, String song) {
            if (!userToSongsMap.containsKey(user)) {
                userToSongsMap.put(user, new ArrayList<String>());
            }
            List<String> songs = userToSongsMap.get(user);
            songs.remove(song);
            songs.add(song);
            if (songs.size() > capacity) {
                songs.remove(0);
            }
        }

        public void deleteSong(String user, String song) {
            if (userToSongsMap.containsKey(user)) {
                userToSongsMap.get(user).remove(song);
            }
        }

        public String[] getRecentlyPlayedSongs(String user) {
            if (!userToSongsMap.containsKey(user)) {
                return new String[0];
            }
            return userToSongsMap.get(user).toArray(new String[0]);
        }
    }
}

