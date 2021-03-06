/*
 * Copyright 2017 Idealnaya rabota LLC
 * Licensed under Multy.io license.
 * See LICENSE for details
 */

package io.multy.storage;

import io.multy.api.socket.CurrenciesRate;
import io.multy.model.entities.ByteSeed;
import io.multy.model.entities.DeviceId;
import io.multy.model.entities.ExchangePrice;
import io.multy.model.entities.Mnemonic;
import io.multy.model.entities.RootKey;
import io.multy.model.entities.Token;
import io.multy.model.entities.UserId;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.realm.Realm;

public class SettingsDao {

    private Realm realm;

    public SettingsDao(@NonNull Realm realm) {
        this.realm = realm;
    }

    public void saveRootKey(RootKey key) {
        realm.executeTransaction(realm -> realm.insertOrUpdate(key));
    }

    public RootKey getRootKey() {
        return realm.where(RootKey.class).findFirst();
    }

    public void saveToken(Token token) {
        realm.executeTransaction(realm -> realm.insertOrUpdate(token));
    }

    public Token getToken() {
        return realm.where(Token.class).findFirst();
    }

    public void saveUserId(UserId userId) {
        realm.executeTransaction(realm -> realm.insertOrUpdate(userId));
    }

    public UserId getUserId() {
        return realm != null ? realm.where(UserId.class).findFirst() : null;
    }

    public void saveSeed(ByteSeed seed) {
        realm.executeTransaction(realm -> realm.insertOrUpdate(seed));
    }

    public ByteSeed getSeed() {
        return realm.where(ByteSeed.class).findFirst();
    }

    public void setMnemonic(Mnemonic mnemonic) {
        realm.executeTransaction(realm -> realm.insertOrUpdate(mnemonic));
    }

    public Mnemonic getMnemonic() {
        return realm.where(Mnemonic.class).findFirst();
    }

    public void setDeviceId(DeviceId deviceId) {
        realm.executeTransaction(realm -> realm.insertOrUpdate(deviceId));
    }

    public DeviceId getDeviceId() {
        return realm.where(DeviceId.class).findFirst();
    }

    public void saveExchangePrice(final ExchangePrice exchangePrice) {
        realm.executeTransaction(realm -> realm.insertOrUpdate(exchangePrice));
    }

    public void setUserId(UserId userId) {
        realm.executeTransaction(realm -> realm.insertOrUpdate(userId));
    }

    public void setByteSeed(ByteSeed byteSeed) {
        realm.executeTransaction(realm -> realm.insertOrUpdate(byteSeed));
    }

    public ByteSeed getByteSeed() {
        return realm.where(ByteSeed.class).findFirst();
    }

    public ExchangePrice getExchangePrice() {
        ExchangePrice exchangePrice = realm.where(ExchangePrice.class).findFirst();
        if (exchangePrice == null) {
            exchangePrice = new ExchangePrice(15000.00);
        }
        return exchangePrice;
    }

    public void saveCurrenciesRate(CurrenciesRate currenciesRate) {
        realm.executeTransaction(realm -> realm.insertOrUpdate(currenciesRate));
    }

    @Nullable
    public CurrenciesRate getCurrenciesRate() {
        CurrenciesRate currenciesRate = realm.where(CurrenciesRate.class).findFirst();
        if (currenciesRate == null) {
            currenciesRate = new CurrenciesRate();
            currenciesRate.setBtcToUsd(0);
        }
        return currenciesRate;
    }
}
