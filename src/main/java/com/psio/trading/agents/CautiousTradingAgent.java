package com.psio.trading.agents;

import com.psio.market.MarketDataPayload;
import com.psio.trading.TradingAction;
import com.psio.trading.Wallet;
import com.psio.trading.strategies.TradingStrategy;

public class CautiousTradingAgent extends TradingAgent {
    TradingAction lastAction;

    public CautiousTradingAgent(Wallet wallet, TradingStrategy currentStrategy) {
        super(wallet);
        this.currentStrategy = currentStrategy;
    }

    @Override
    public void update(MarketDataPayload marketDataPayload) {
        TradingAction decision = currentStrategy.decide(marketDataPayload);

        float currentPrice = marketDataPayload.close;
        this.wallet.updateCurrentPrice(currentPrice);

        if (decision == lastAction) {
            switch (decision) {
                case TradingAction.BUY:
                    wallet.tryBuyMaxAssets();
                    break;

                case TradingAction.SELL:
                    wallet.trySellAllAssets();
                    break;

                case TradingAction.HOLD:
                    break;
            }
        }
        lastAction = decision;
    }

    @Override
    public void begin() {
        super.begin();
        this.currentStrategy.reset();
    }
}
