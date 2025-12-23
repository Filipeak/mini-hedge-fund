package com.psio.portfolio;

public interface PortfolioObserver {

    void onChange();

    void onBegin();

    void onEnd();
}
