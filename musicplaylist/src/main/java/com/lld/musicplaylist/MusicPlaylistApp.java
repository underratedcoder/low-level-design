package com.lld.musicplaylist;

import com.lld.musicplaylist.controller.PlaylistController;
import com.lld.musicplaylist.model.Music;
import com.lld.musicplaylist.service.PlaylistService;

import java.util.Arrays;
import java.util.List;

public class MusicPlaylistApp {
    public static void main(String[] args) {
        // Create a sample playlist
        List<Music> songs = Arrays.asList(
            new Music("The Beatles", "Hey Jude"),
            new Music("Queen", "Bohemian Rhapsody"),
            new Music("The Beatles", "Let It Be"),
            new Music("Pink Floyd", "Comfortably Numb"),
            new Music("Led Zeppelin", "Stairway to Heaven")
        );
        
        // Initialize service and controller
        PlaylistService service = new PlaylistService(songs);
        PlaylistController playlist = new PlaylistController(service);
        
        // Demo the playlist functionality
        System.out.println("Playing: " + playlist.play());
        System.out.println("Next song: " + playlist.next());
        System.out.println("Previous song: " + playlist.back());
        
        // Toggle repeat mode
        System.out.println("Repeat mode: " + playlist.repeat());
        System.out.println("Next song (should be same as current): " + playlist.next());
        System.out.println("Turning off repeat mode: " + playlist.repeat());
        
        // Toggle repeat list mode
        System.out.println("Repeat list mode: " + playlist.repeatList());
        
        // Play through the playlist to demonstrate repeat list
        System.out.println("Playing through playlist with repeat list on...");
        for (int i = 0; i < 7; i++) {
            System.out.println(playlist.next());
        }
        
        // Filter by artist
        System.out.println("\nFiltering by The Beatles:");
        PlaylistController filteredPlaylist = playlist.filterByArtist(new String[]{"The Beatles"});
        System.out.println("Play: " + filteredPlaylist.play());
        System.out.println("Next: " + filteredPlaylist.next());
        
        // Shuffle demo
        System.out.println("\nShuffle mode: " + playlist.shuffle());
        System.out.println("Play in shuffle mode: " + playlist.play());
        System.out.println("Next (shuffled): " + playlist.next());
        System.out.println("Next (shuffled): " + playlist.next());
    }
}