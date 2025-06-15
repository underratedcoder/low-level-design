package com.lld.musicplaylist.controller;

import com.lld.musicplaylist.model.Music;
import com.lld.musicplaylist.service.PlaylistService;

/**
 * Controller layer that handles user interactions and delegates to service layer.
 */
public class PlaylistController {
    private final PlaylistService service;
    
    public PlaylistController(PlaylistService service) {
        this.service = service;
    }
    
    public Music play() {
        return service.play();
    }
    
    public Music next() {
        return service.next();
    }
    
    public Music back() {
        return service.back();
    }
    
    public boolean repeat() {
        return service.toggleRepeat();
    }
    
    public boolean repeatList() {
        return service.toggleRepeatList();
    }
    
    public boolean shuffle() {
        return service.toggleShuffle();
    }
    
    public PlaylistController filterByArtist(String[] artists) {
        PlaylistService filteredService = service.filterByArtist(artists);
        return new PlaylistController(filteredService);
    }
}