package com.lld.musicplaylist.util.state;

import com.lld.musicplaylist.model.Music;
import com.lld.musicplaylist.util.PlaylistState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShuffleRepeatListState implements PlaylistState {
    private List<Integer> shuffledIndices;
    private int shufflePointer;

    public ShuffleRepeatListState(PlaylistContext context) {
        initializeShuffledIndices(context.getPlaylist().size());
        this.shufflePointer = 0;
        // Set the current index to the first shuffled index
        context.setCurrentIndex(shuffledIndices.get(shufflePointer));
    }

    private void initializeShuffledIndices(int size) {
        shuffledIndices = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            shuffledIndices.add(i);
        }
        Collections.shuffle(shuffledIndices);
    }

    @Override
    public Music play(PlaylistContext context) {
        if (context.getCurrentIndex() < 0 || context.getCurrentIndex() >= context.getPlaylist().size()) {
            return null;
        }
        return context.getPlaylist().get(context.getCurrentIndex());
    }

    @Override
    public Music next(PlaylistContext context) {
        shufflePointer++;
        if (shufflePointer >= shuffledIndices.size()) {
            // Reshuffle and start from beginning
            initializeShuffledIndices(context.getPlaylist().size());
            shufflePointer = 0;
        }
        context.setCurrentIndex(shuffledIndices.get(shufflePointer));
        return play(context);
    }

    @Override
    public Music back(PlaylistContext context) {
        shufflePointer--;
        if (shufflePointer < 0) {
            shufflePointer = 0;
            return null;
        }
        context.setCurrentIndex(shuffledIndices.get(shufflePointer));
        return play(context);
    }
}