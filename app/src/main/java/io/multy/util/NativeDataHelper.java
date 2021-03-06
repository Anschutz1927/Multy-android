/*
 *  Copyright 2017 Idealnaya rabota LLC
 *  Licensed under Multy.io license.
 *  See LICENSE for details
 */

package io.multy.util;


import io.multy.BuildConfig;

/**
 * Every method should throw JniException.
 * Also jni exception should be considered while typing new method inside JNI
 */
public class NativeDataHelper {

    static {
        if (BuildConfig.DEBUG) {
            System.loadLibrary("multy_core_jnid");
        } else {
            System.loadLibrary("multy_core_jni");
        }
    }

    public enum Currency {
        BTC(0),
        ETH(1);

        private final int value;

        Currency(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum AddressType {
        ADDRESS_EXTERNAL(0),
        ADDRESS_INTERNAL(1);

        private final int value;

        AddressType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static native String makeMnemonic() throws JniException;

    public static native byte[] makeSeed(String mnemonic) throws JniException;

    public static native String makeAccountAddress(byte[] seed, int walletIndex, int addressIndex, int currency) throws JniException;

    public static native String makeAccountId(byte[] seed) throws JniException;

    public static native int runTest();

    public static native byte[] makeTransaction(byte[] seed, int walletIndex, String amountToSpend, String feePerByte, String donationAmount, String destinationAddress, String changeAddress, String donationAddress, boolean payFee) throws JniException;

    public static native byte[] digestSha3256(byte[] s) throws JniException;

    public static native String getMyPrivateKey(byte[] seed, int walletIndex, int addressIndex, int currency) throws JniException;
}
