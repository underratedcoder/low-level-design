package com.lld.splitwise.util.observer;

import com.lld.splitwise.model.Member;

public interface NotificationObserver {
    void notify(Member member, String message);
}
