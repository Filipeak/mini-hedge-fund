package com.psio.trading.strategies;

import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;

public class MLTradingStrategy implements TradingStrategy {

    private static final int ML_WINDOW = 24;
    private static final Logger logger = LogManager.getLogger(MLTradingStrategy.class);

    private final float[] prices = new float[ML_WINDOW];
    private final float[] volumes = new float[ML_WINDOW];
    private int currentIndex = 0;

    @Override
    public TradingAction decide(MarketDataPayload marketDataPayload) {
        TradingAction tradingAction = TradingAction.HOLD;

        prices[currentIndex] = marketDataPayload.close;
        volumes[currentIndex] = (float) marketDataPayload.volume;
        currentIndex++;

        if (currentIndex == ML_WINDOW) {
            String result = fetch();

            if (!Objects.equals(result, "")) {
                JSONObject obj = new JSONObject(result);

                double up = obj.getDouble("UP");
                double down = obj.getDouble("DOWN");
                double stay = obj.getDouble("STAY");

                if (up > down && up > stay) {
                    tradingAction = TradingAction.BUY;

                    logger.debug("ML Agent decision: BUY with probability: {}", up);
                } else if (down > up && down > stay) {
                    tradingAction = TradingAction.SELL;

                    logger.debug("ML Agent decision: SELL with probability: {}", down);
                }
            }

            currentIndex = 0;
        }

        return tradingAction;
    }

    @Override
    public void reset() {
        currentIndex = 0;
    }

    private String fetch() {
        try {
            String url = "http://localhost:8080/predict";
            String jsonPayload = getJSON();

            HttpResponse<String> response;

            try (HttpClient client = HttpClient.newHttpClient()) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .build();

                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            }

            logger.debug("Successfully sent request and got code: {}", response.statusCode());

            return response.body();
        } catch (Exception e) {
            logger.error(e.getMessage());

            return "";
        }
    }

    private String getJSON() {
        JSONArray pricesArray = new JSONArray();

        for (float p : prices) {
            pricesArray.put(p);
        }

        JSONArray volumesArray = new JSONArray();

        for (float v : volumes) {
            volumesArray.put(v);
        }

        JSONObject json = new JSONObject();
        json.put("prices", pricesArray);
        json.put("volumes", volumesArray);

        return json.toString();
    }
}
