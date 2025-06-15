package com.lld.musicplaylist.repository;

import com.lld.musicplaylist.enums.PlaylistState;
import com.lld.musicplaylist.model.Music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Repository layer for playlist data management.
 * In a real application, this would interact with a database.
 */
public class PlaylistRepository {
    private List<Music> playlist;
    private List<Music> originalPlaylist;
    private int currentIndex;
    private PlaylistState state;
    private List<Integer> shuffledIndices;
    private int shufflePointer;
    
    public PlaylistRepository(List<Music> songs) {
        this.originalPlaylist = new ArrayList<>(songs);
        this.playlist = new ArrayList<>(songs);
        this.currentIndex = -1;
        this.state = PlaylistState.NORMAL;
        this.shuffledIndices = new ArrayList<>();
    }
    
    public List<Music> getPlaylist() {
        return new ArrayList<>(playlist);
    }
    
    public List<Music> getOriginalPlaylist() {
        return new ArrayList<>(originalPlaylist);
    }
    
    public void setPlaylist(List<Music> playlist) {
        this.playlist = new ArrayList<>(playlist);
    }
    
    public int getCurrentIndex() {
        return currentIndex;
    }
    
    public void setCurrentIndex(int index) {
        this.currentIndex = index;
    }
    
    public PlaylistState getState() {
        return state;
    }
    
    public void setState(PlaylistState state) {
        this.state = state;
        
        // If transitioning to a shuffle state, initialize shuffled indices
        if (state == PlaylistState.SHUFFLE || state == PlaylistState.SHUFFLE_REPEAT_LIST) {
            initializeShuffledIndices();
        }
    }
    
    public Music getCurrentMusic() {
        if (currentIndex < 0 || currentIndex >= playlist.size()) {
            return null;
        }
        return playlist.get(currentIndex);
    }
    
    private void initializeShuffledIndices() {
        shuffledIndices.clear();
        for (int i = 0; i < playlist.size(); i++) {
            shuffledIndices.add(i);
        }
        Collections.shuffle(shuffledIndices);
        shufflePointer = 0;
        
        // Set current index to first shuffled song
        if (!shuffledIndices.isEmpty()) {
            currentIndex = shuffledIndices.get(0);
        }
    }
    
    public List<Integer> getShuffledIndices() {
        return new ArrayList<>(shuffledIndices);
    }
    
    public int getShufflePointer() {
        return shufflePointer;
    }
    
    public void setShufflePointer(int pointer) {
        this.shufflePointer = pointer;
    }
    
    public void incrementShufflePointer() {
        shufflePointer++;
    }
    
    public void decrementShufflePointer() {
        shufflePointer--;
    }
    
    public int getShuffledIndex(int pointer) {
        if (pointer >= 0 && pointer < shuffledIndices.size()) {
            return shuffledIndices.get(pointer);
        }
        return -1;
    }
}