import pandas as pd
import numpy as np
import torch


def triple_barrier_label(close, t, tp, sl, max_holding):
    start_price = close.iloc[t]
    end = min(t + max_holding, len(close) - 1)

    for i in range(t + 1, end + 1):
        ret = (close.iloc[i] - start_price) / start_price

        if ret >= tp:
            return 1
        if ret <= -sl:
            return -1

    return 0


def balance_dataset(X, Y):
    print("Balancing dataset...")

    n_classes = len(Y[0])
    counts = []

    for i in range(n_classes):
        counts.append(sum(Y[:, i]))

    min_samples = np.min(counts)

    print(f"Balancing for {min_samples} samples")

    all_indices = []

    for i in range(n_classes):
        indicies = np.where(Y[:, i] == 1)[0]
        selected_indices = np.random.choice(indicies, min_samples, replace=False)
        all_indices.extend(selected_indices)

    np.random.shuffle(all_indices)

    return X[all_indices], Y[all_indices]


def add_features(df: pd.DataFrame):
    eps = 1e-9

    df['return'] = df['close'].diff()
    df['pct_return'] = df['close'].pct_change()
    df['log_return'] = np.log((df['close'] + eps) / (df['close'].shift(1) + eps))

    volatility_period = 24
    df['volatility'] = df['pct_return'].rolling(volatility_period).std()
    df['log_volatility'] = df['log_return'].rolling(volatility_period).std()

    rsi_period = 14
    df['rsi'] = 1 - 1 / (1 + df['return'].clip(lower=0).rolling(rsi_period).mean() / -df['return'].clip(upper=0).rolling(rsi_period).mean())
    df['rsi_dev'] = df['rsi'] - 0.5

    vol_period = 24
    df['volume_log_dev'] = np.log((df['volume'] + eps) / (df['volume'].rolling(vol_period).mean() + eps))
    df['volume_return'] = df['volume'].pct_change()
    df['volume_log_return'] = np.log((df['volume'] + eps) / (df['volume'].shift(1) + eps))


def preprocess_data(df: pd.DataFrame, balance=False):
    # Add features
    add_features(df)

    # Label data
    df['TB_Label'] = [triple_barrier_label(df['close'], i, 0.01, 0.01, 8) for i in range(len(df))]
    df['label_tp'] = df['TB_Label'].apply(lambda x: 1 if x == 1 else 0)
    df['label_sl'] = df['TB_Label'].apply(lambda x: 1 if x == -1 else 0)
    df['label_to'] = df['TB_Label'].apply(lambda x: 1 if x == 0 else 0)

    # Remove invalid
    df.dropna(inplace=True)

    # Extract features and labels
    data = df[['pct_return', 'log_return', 'volatility', 'log_volatility', 'rsi', 'rsi_dev', 'volume_log_dev', 'volume_log_return']].values
    labels = df[['label_tp', 'label_sl', 'label_to']].values

    # Create rolling windows
    window_size = 24

    X = []
    Y = []

    for i in range(len(data) - window_size):
        window = data[i:(i + window_size)]
        label = labels[i + window_size - 1]

        X.append(window)
        Y.append(label)

    X = np.array(X)
    Y = np.array(Y)

    if balance:
        X, Y = balance_dataset(X, Y)

    # Print info
    print("Frequnecy of labels:")
    print(f"  TP: {sum(Y[:, 0]) / len(Y):.2f}")
    print(f"  SL: {sum(Y[:, 1]) / len(Y):.2f}")
    print(f"  TO: {sum(Y[:, 2]) / len(Y):.2f}")

    return X, Y


def gather_files_data(files, balance):
    X = []
    Y = []

    for n in files:
        df = pd.read_csv(n)
        X_temp, Y_temp = preprocess_data(df, balance)

        X.extend(X_temp)
        Y.extend(Y_temp)

    return X, Y


def prepare_data():
    # Gather all data
    X_train, Y_train = gather_files_data(["./data/train_btc_2017_2024.csv"], True)
    X_test, Y_test = gather_files_data(["./data/test_btc_2025.csv"], False)

    # Convert to tensors
    X_train = torch.tensor(np.array(X_train), dtype=torch.float32)
    X_test = torch.tensor(np.array(X_test), dtype=torch.float32)
    Y_train = torch.tensor(np.array(Y_train), dtype=torch.float32)
    Y_test = torch.tensor(np.array(Y_test), dtype=torch.float32)

    # Print info
    print("Data dimensions:")
    print(f"  Train Input: {list(X_train.shape)}")
    print(f"  Train Output: {list(Y_train.shape)}")
    print(f"  Test Input: {list(X_test.shape)}")
    print(f"  Test Output: {list(Y_test.shape)}")

    return X_train, X_test, Y_train, Y_test
