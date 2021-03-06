package com.bpedigo.pomodoro.model;


public enum AttemptKind {
    FOCUS(25 * 60, "Focus time"),
    BREAK(5 * 60, "Break time");

    private int mTotalSeconds;
    private String mDisplayName;

    AttemptKind(int totalSeconds, String displayName) {
        this.mTotalSeconds = totalSeconds;
        this.mDisplayName = displayName;
    }

    public int getmTotalSeconds() {
        return mTotalSeconds;
    }

    public String getDisplayName() {
        return mDisplayName;

    }
}
