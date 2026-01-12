from flask import Flask, request, jsonify
import pandas as pd
import numpy as np
import torch
import model
import data_prep


app = Flask(__name__)
last_data = None
mdl = model.load_model("./data/model.pth")


@app.route("/predict", methods=['POST'])
def predict():
    global last_data

    data = request.json

    if last_data is None:
        last_data = data

        return jsonify({
            "UP": 0,
            "DOWN": 0,
            "STAY": 0,
        })

    all_data = {
        'volume': last_data['volumes'] + data['volumes'],
        'close': last_data['prices'] + data['prices'],
    }
    last_data = data

    df = pd.DataFrame(all_data)
    data_prep.add_features(df)
    df.dropna(inplace=True)

    data = df[['pct_return', 'log_return', 'volatility', 'log_volatility', 'rsi', 'rsi_dev', 'volume_log_dev', 'volume_log_return']].values
    data = torch.tensor(np.array([data]), dtype=torch.float32)

    result = model.run_model(mdl, data).tolist()[0]

    print("Prediction result: ", result)

    return jsonify({
        "UP": int(result[0] * 100) / 100,
        "DOWN": float(result[1] * 100) / 100,
        "STAY": float(result[2] * 100) / 100,
    })


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080, threaded=True)
