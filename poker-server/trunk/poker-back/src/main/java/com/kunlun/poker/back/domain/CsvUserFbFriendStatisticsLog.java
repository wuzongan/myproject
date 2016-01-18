package com.kunlun.poker.back.domain;

public class CsvUserFbFriendStatisticsLog extends AbstactLog<Integer> implements
        Comparable<CsvUserFbFriendStatisticsLog> {
    private int shareFbCount;
    private int inviteFriendCount;
    public int getShareFbCount() {
        return shareFbCount;
    }
    public void setShareFbCount(int shareFbCount) {
        this.shareFbCount = shareFbCount;
    }
    public int getInviteFriendCount() {
        return inviteFriendCount;
    }
    public void setInviteFriendCount(int inviteFriendCount) {
        this.inviteFriendCount = inviteFriendCount;
    }
    @Override
    public boolean equals(Object obj) {
        return getClass() == obj.getClass() && getId() == ((CsvUserFbFriendStatisticsLog)obj).getId();
    }
    @Override
    public int compareTo(CsvUserFbFriendStatisticsLog o) {
        int result = Integer.compare(getId(), o.getId());
        return result;
    }

    @Override
    public void merge(AbstactLog<Integer> log) {
        CsvUserFbFriendStatisticsLog targetLog = (CsvUserFbFriendStatisticsLog) log;
        this.setId(targetLog.getId());
        this.setInviteFriendCount(this.getInviteFriendCount() + targetLog.getInviteFriendCount());
        this.setShareFbCount(this.getShareFbCount() + targetLog.getShareFbCount());
    }

}
