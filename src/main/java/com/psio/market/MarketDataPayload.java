package com.psio.market;

public class MarketDataPayload {
    public int timestamp;
    public float open;
    public float close;
    public float high;
    public float low;
    public double volume;

    public MarketDataPayload(int timestamp, float open, float close, float high, float low, double volume) {
        this.timestamp = timestamp;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.volume = volume;
    }
}
