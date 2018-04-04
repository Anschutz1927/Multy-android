/*
 * Copyright 2018 Idealnaya rabota LLC
 * Licensed under Multy.io license.
 * See LICENSE for details
 */

package io.multy.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateHelper {

    public static final SimpleDateFormat DATE_FORMAT_AUTH = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.getDefault());
    public static final SimpleDateFormat DATE_FORMAT_HISTORY = new SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault());
    public static final SimpleDateFormat DATE_FORMAT_ADDRESSES = new SimpleDateFormat("dd.MM.yyyy ∙  HH:mm", Locale.getDefault());
    public static final SimpleDateFormat DATE_FORMAT_TRANSACTION_INFO = new SimpleDateFormat("HH:mm, dd MMMM yyyy", Locale.getDefault());
}
