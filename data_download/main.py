import requests
import pandas as pd
import time


def get_binance_klines(symbol, interval, start_time, end_time):
    url = "https://api.binance.com/api/v3/klines"
    data = []
    limit = 1000

    while True:
        params = {
            "symbol": symbol,
            "interval": interval,
            "startTime": start_time,
            "endTime": end_time,
            "limit": limit
        }
        response = requests.get(url, params=params)
        chunk = response.json()

        if not chunk or "code" in chunk:
            break

        data.extend(chunk)

        start_time = chunk[-1][0] + 1

        if chunk[-1][0] >= end_time:
            break

        time.sleep(0.2)

    return data


symbol = "BTCUSDT"
interval = "1h"
end = int(time.time() * 1000)
start = end - 365 * 24 * 60 * 60 * 1000

raw = get_binance_klines(symbol, interval, start, end)

df = pd.DataFrame(raw, columns=[
    "timestamp", "open", "high", "low", "close", "volume",
    "close_time", "quote_volume", "trades",
    "taker_buy_base", "taker_buy_quote", "ignore"
])
df = df[["timestamp", "open", "close", "high", "low", "volume"]]
df["timestamp"] = pd.to_datetime(df["timestamp"], unit="ms")

df.to_csv("data.csv", index=False)

print("Saved to data.csv")
print(df.head())
