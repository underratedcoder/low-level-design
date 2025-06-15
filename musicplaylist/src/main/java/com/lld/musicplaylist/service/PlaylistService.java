package com.lld.musicplaylist.service;

import com.lld.musicplaylist.enums.PlaylistState;
import com.lld.musicplaylist.model.Music;
import com.lld.musicplaylist.repository.PlaylistRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer that implements playlist business logic.
 * Delegates to the repository for data operations.
 */
public class PlaylistService {
    private final PlaylistRepository repository;
    
    public PlaylistService(List<Music> initialPlaylist) {
        this.repository = new PlaylistRepository(initialPlaylist);
    }
    
    public Music play() {
        if (repository.getCurrentIndex() == -1 && !repository.getPlaylist().isEmpty()) {
            repository.setCurrentIndex(0);
        }
        return repository.getCurrentMusic();
    }
    
    public Music next() {
        switch (repository.getState()) {
            case NORMAL:
                return normalNext();
            case REPEAT_ONE:
                return repeatOneNext();
            case REPEAT_LIST:
                return repeatListNext();
            case SHUFFLE:
                return shuffleNext();
            case SHUFFLE_REPEAT_LIST:
                return shuffleRepeatListNext();
            default:
                return null;
        }
    }
    
    public Music back() {
        switch (repository.getState()) {
            case NORMAL:
            case REPEAT_ONE:
            case REPEAT_LIST:
                return normalBack();
            case SHUFFLE:
            case SHUFFLE_REPEAT_LIST:
                return shuffleBack();
            default:
                return null;
        }
    }
    
    public boolean toggleRepeat() {
        boolean isRepeatOn = repository.getState() == PlaylistState.REPEAT_ONE;
        
        if (isRepeatOn) {
            // Turn repeat off
            if (repository.getState() == PlaylistState.REPEAT_ONE) {
                repository.setState(PlaylistState.NORMAL);
            }
        } else {
            // Turn repeat on
            repository.setState(PlaylistState.REPEAT_ONE);
        }
        
        return repository.getState() == PlaylistState.REPEAT_ONE;
    }
    
    public boolean toggleRepeatList() {
        boolean isRepeatListOn = repository.getState() == PlaylistState.REPEAT_LIST || 
                                repository.getState() == PlaylistState.SHUFFLE_REPEAT_LIST;
        boolean isShuffleOn = repository.getState() == PlaylistState.SHUFFLE || 
                             repository.getState() == PlaylistState.SHUFFLE_REPEAT_LIST;
        
        if (isRepeatListOn) {
            // Turn repeat list off
            repository.setState(isShuffleOn ? PlaylistState.SHUFFLE : PlaylistState.NORMAL);
        } else {
            // Turn repeat list on
            repository.setState(isShuffleOn ? PlaylistState.SHUFFLE_REPEAT_LIST : PlaylistState.REPEAT_LIST);
        }
        
        isRepeatListOn = repository.getState() == PlaylistState.REPEAT_LIST || 
                         repository.getState() == PlaylistState.SHUFFLE_REPEAT_LIST;
        return isRepeatListOn;
    }
    
    public boolean toggleShuffle() {
        boolean isShuffleOn = repository.getState() == PlaylistState.SHUFFLE || 
                             repository.getState() == PlaylistState.SHUFFLE_REPEAT_LIST;
        boolean isRepeatListOn = repository.getState() == PlaylistState.REPEAT_LIST || 
                                repository.getState() == PlaylistState.SHUFFLE_REPEAT_LIST;
        
        if (isShuffleOn) {
            // Turn shuffle off
            repository.setState(isRepeatListOn ? PlaylistState.REPEAT_LIST : PlaylistState.NORMAL);
        } else {
            // Turn shuffle on
            repository.setState(isRepeatListOn ? PlaylistState.SHUFFLE_REPEAT_LIST : PlaylistState.SHUFFLE);
        }
        
        isShuffleOn = repository.getState() == PlaylistState.SHUFFLE || 
                      repository.getState() == PlaylistState.SHUFFLE_REPEAT_LIST;
        return isShuffleOn;
    }
    
    public PlaylistService filterByArtist(String[] artists) {
        List<String> artistList = List.of(artists);
        
        List<Music> filteredList = repository.getOriginalPlaylist().stream()
            .filter(music -> artistList.contains(music.getArtist()))
            .collect(Collectors.toList());
        
        return new PlaylistService(filteredList);
    }
    
    private Music normalNext() {
        int nextIndex = repository.getCurrentIndex() + 1;
        if (nextIndex >= repository.getPlaylist().size()) {
            repository.setCurrentIndex(-1); // Playlist finished
            return null;
        }
        repository.setCurrentIndex(nextIndex);
        return repository.getCurrentMusic();
    }
    
    private Music repeatOneNext() {
        // Just returns the current song again
        return repository.getCurrentMusic();
    }
    
    private Music repeatListNext() {
        int nextIndex = repository.getCurrentIndex() + 1;
        if (nextIndex >= repository.getPlaylist().size()) {
            // Loop back to start
            repository.setCurrentIndex(0);
        } else {
            repository.setCurrentIndex(nextIndex);
        }
        return repository.getCurrentMusic();
    }
    
    private Music shuffleNext() {
        repository.incrementShufflePointer();
        if (repository.getShufflePointer() >= repository.getShuffledIndices().size()) {
            // End of shuffled list
            repository.setCurrentIndex(-1);
            return null;
        }
        int nextIndex = repository.getShuffledIndex(repository.getShufflePointer());
        repository.setCurrentIndex(nextIndex);
        return repository.getCurrentMusic();
    }
    
    private Music shuffleRepeatListNext() {
        repository.incrementShufflePointer();
        if (repository.getShufflePointer() >= repository.getShuffledIndices().size()) {
            // Reshuffle and start from beginning
            repository.setState(PlaylistState.SHUFFLE_REPEAT_LIST); // This will reinitialize shuffled indices
        }
        int nextIndex = repository.getShuffledIndex(repository.getShufflePointer());
        repository.setCurrentIndex(nextIndex);
        return repository.getCurrentMusic();
    }
    
    private Music normalBack() {
        int prevIndex = repository.getCurrentIndex() - 1;
        if (prevIndex < 0) {
            return null;
        }
        repository.setCurrentIndex(prevIndex);
        return repository.getCurrentMusic();
    }
    
    private Music shuffleBack() {
        repository.decrementShufflePointer();
        if (repository.getShufflePointer() < 0) {
            repository.setShufflePointer(0);
            return null;
        }
        int prevIndex = repository.getShuffledIndex(repository.getShufflePointer());
        repository.setCurrentIndex(prevIndex);
        return repository.getCurrentMusic();
    }
}
