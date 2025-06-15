package com.lld.musicplaylist.util.state;

import com.lld.musicplaylist.model.Music;
import com.lld.musicplaylist.util.PlaylistState;

public class RepeatListState implements PlaylistState {

    @Override
    public Music play(PlaylistContext context) {
        if (context.getCurrentIndex() < 0 || context.getCurrentIndex() >= context.getPlaylist().size()) {
            return null;
        }
        return context.getPlaylist().get(context.getCurrentIndex());
    }

    @Override
    public Music next(PlaylistContext context) {
        int nextIndex = context.getCurrentIndex() + 1;
        if (nextIndex >= context.getPlaylist().size()) {
            // Go back to the beginning of the playlist
            context.setCurrentIndex(0);
        } else {
            context.setCurrentIndex(nextIndex);
        }
        return play(context);
    }

    @Override
    public Music back(PlaylistContext context) {
        int prevIndex = context.getCurrentIndex() - 1;
        if (prevIndex < 0) {
            return null;
        }
        context.setCurrentIndex(prevIndex);
        return play(context);
    }
}