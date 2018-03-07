/*
 * Copyright 2017 Idealnaya rabota LLC
 * Licensed under Multy.io license.
 * See LICENSE for details
 */

package io.multy.model.entities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by anschutz1927@gmail.com on 07.03.18.
 */

public class DonateInfoEntity extends RealmObject {

    @PrimaryKey
    private int featureCode;
    private String featureDescription;
    private String donationAddress;

    public DonateInfoEntity() {}

    public DonateInfoEntity(int featureCode) {
        this.featureCode = featureCode;
    }

    public int getFeatureCode() {
        return featureCode;
    }

    public String getFeatureDescription() {
        return featureDescription;
    }

    public void setFeatureDescription(String featureDescription) {
        this.featureDescription = featureDescription;
    }

    public String getDonationAddress() {
        return donationAddress;
    }

    public void setDonationAddress(String donationAddress) {
        this.donationAddress = donationAddress;
    }
}
