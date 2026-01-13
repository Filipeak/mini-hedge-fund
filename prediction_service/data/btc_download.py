import requests
import pandas as pd
import time
from datetime import datetime


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


def download_and_save(start, end, name):
    symbol = "BTCUSDT"
    interval = "1h"

    raw = get_binance_klines(symbol, interval, start, end)

    df = pd.DataFrame(raw, columns=[
        "timestamp", "open", "high", "low", "close", "volume",
        "close_time", "quote_volume", "trades",
        "taker_buy_base", "taker_buy_quote", "ignore"
    ])
    df = df[["timestamp", "open", "close", "high", "low", "volume"]]
    df["timestamp"] = df["timestamp"].astype("int64")
    df["open"] = df["open"].astype(float)
    df["close"] = df["close"].astype(float)
    df["high"] = df["high"].astype(float)
    df["low"] = df["low"].astype(float)
    df["volume"] = df["volume"].astype(float)

    df.to_csv(name, index=False)
    print(f"Saved to {name}")


download_and_save(int(datetime(2015, 1, 1).timestamp() * 1000), int(datetime(2024, 12, 31).timestamp() * 1000), "train_btc_2017_2024.csv")
download_and_save(int(datetime(2025, 1, 1).timestamp() * 1000), int(datetime(2025, 12, 31).timestamp() * 1000), "test_btc_2025.csv")
