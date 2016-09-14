package com.example.kuwagata.teste.chat.model;

import com.sendbird.android.User;

import java.io.Serializable;

/**
 * Created by kuwagata on 14/09/16.
 */
public class ChatUser implements Serializable {

    private String mNickname;
    private String mProfileUrl;
    private String mUserId;
    private User.ConnectionStatus mConnectionStatus;
    private long mLastSeenAt;

    public ChatUser(String email, String name) {
        setmUserId(email);
        setmNickname(name);
    }

    public ChatUser(User user) {
        setmNickname(user.getNickname());
        setmConnectionStatus(user.getConnectionStatus());
        setmUserId(user.getUserId());
        setmLastSeenAt(user.getLastSeenAt());
        setmProfileUrl(user.getProfileUrl());
    }

    public String getmNickname() {
        return mNickname;
    }

    public void setmNickname(String mNickname) {
        this.mNickname = mNickname;
    }

    public String getmProfileUrl() {
        return mProfileUrl;
    }

    public void setmProfileUrl(String mProfileUrl) {
        this.mProfileUrl = mProfileUrl;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public User.ConnectionStatus getmConnectionStatus() {
        return mConnectionStatus;
    }

    public void setmConnectionStatus(User.ConnectionStatus mConnectionStatus) {
        this.mConnectionStatus = mConnectionStatus;
    }

    public long getmLastSeenAt() {
        return mLastSeenAt;
    }

    public void setmLastSeenAt(long mLastSeenAt) {
        this.mLastSeenAt = mLastSeenAt;
    }

}
