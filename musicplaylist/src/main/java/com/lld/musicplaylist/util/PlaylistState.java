package com.lld.musicplaylist.util;

import com.lld.musicplaylist.model.Music;

public interface PlaylistState {
    Music play(PlaylistContext context);
    Music next(PlaylistContext context);
    Music back(PlaylistContext context);
}