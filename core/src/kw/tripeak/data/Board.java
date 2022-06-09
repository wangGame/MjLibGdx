package kw.tripeak.data;

import java.util.Arrays;

public class Board {
    private int id;
    private float posX;
    private float poxY;
    private float rotation;
    private int suit;
    private int rank;
    private int[] covers;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPoxY() {
        return poxY;
    }

    public void setPoxY(float poxY) {
        this.poxY = poxY;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public int getSuit() {
        return suit;
    }

    public void setSuit(int suit) {
        this.suit = suit;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int[] getCovers() {
        return covers;
    }

    public void setCovers(int[] covers) {
        this.covers = covers;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Board{" +
                "id='" + id + '\'' +
                ", posX=" + posX +
                ", poxY=" + poxY +
                ", rotation=" + rotation +
                ", suit='" + suit + '\'' +
                ", rank='" + rank + '\'' +
                ", covers=" + Arrays.toString(covers) +
                ", type=" + type +
                '}';
    }
}
