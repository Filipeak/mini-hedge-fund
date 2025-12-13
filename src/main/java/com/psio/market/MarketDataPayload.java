package com.psio.market;

public class MarketDataPayload {
    public long timestamp;
    public float open;
    public float close;
    public float high;
    public float low;
    public double volume;

    public MarketDataPayload(long timestamp, float open, float close, float high, float low, double volume) {
        this.timestamp = timestamp;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "MarketDataPayload{" +
                "timestamp=" + timestamp +
                ", open=" + open +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
                ", volume=" + volume +
                '}';
    }
}
