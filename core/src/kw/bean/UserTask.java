package kw.bean;

public class UserTask {
    private int currentUser;
    private int currentCardData;
    private int nextUser;
    private int currenType;
    private int actionMask;

    public int getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(int currentUser) {
        this.currentUser = currentUser;
    }

    public int getCurrentCardData() {
        return currentCardData;
    }

    public void setCurrentCardData(int currentCardData) {
        this.currentCardData = currentCardData;
    }

    public int getNextUser() {
        return nextUser;
    }

    public void setNextUser(int nextUser) {
        this.nextUser = nextUser;
    }

    public int getCurrenType() {
        return currenType;
    }

    public void setCurrenType(int currenType) {
        this.currenType = currenType;
    }
}
