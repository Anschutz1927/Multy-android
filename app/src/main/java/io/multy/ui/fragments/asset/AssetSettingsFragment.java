/*
 * Copyright 2017 Idealnaya rabota LLC
 * Licensed under Multy.io license.
 * See LICENSE for details
 */

package io.multy.ui.fragments.asset;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.multy.R;
import io.multy.model.entities.wallet.WalletRealmObject;
import io.multy.ui.fragments.BaseFragment;
import io.multy.ui.fragments.dialogs.ListDialogFragment;
import io.multy.ui.fragments.dialogs.SimpleDialogFragment;
import io.multy.util.Constants;
import io.multy.util.CurrencyType;
import io.multy.viewmodels.WalletViewModel;
import io.realm.Realm;

/**
 * Created by anschutz1927@gmail.com on 07.12.17.
 */

public class AssetSettingsFragment extends BaseFragment {

    public static final String TAG = AssetSettingsFragment.class.getSimpleName();

    @BindView(R.id.edit_name)
    TextInputEditText editName;

    @BindView(R.id.text_currency)
    TextView textCurrency;

    @BindView(R.id.button_save)
    View buttonSave;

    private WalletViewModel walletViewModel;
    private String name;

    public static AssetSettingsFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AssetSettingsFragment fragment = new AssetSettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public AssetSettingsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: remove next line
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_asset_settings, container, false);
        this.walletViewModel = ViewModelProviders.of(getActivity()).get(WalletViewModel.class);
        ButterKnife.bind(this, v);
        initialize();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        super.showPinDialog();
    }

    private void initialize() {
        /**
         * Next lines create test wallet
         */
        Realm realm = Realm.getDefaultInstance();
        if (realm.where(WalletRealmObject.class).findAll().size() == 0) {
            realm.beginTransaction();
            WalletRealmObject wallet = new WalletRealmObject("My first wallet", "address", 1);
            realm.copyToRealm(wallet);
            realm.commitTransaction();
        }
        //*******************************
        if (walletViewModel != null) {
            walletViewModel.getWalletLive().observe(this, walletRealmObject -> {
                if (walletRealmObject == null){
                    return;
                }
                if (walletRealmObject.getName() != null) {
                    editName.setText(walletRealmObject.getName());
                    editName.setSelection(editName.length());
                    if (name == null) {
                        name = walletRealmObject.getName();
                    }
                }
                if (walletRealmObject.getFiatCurrency() != null) {
                    textCurrency.setText(walletRealmObject.getFiatCurrency());
                }
            });
            walletViewModel.getWallet();
            walletViewModel.fiatCurrency.observe(this, s -> {
                if (s != null) {
                    textCurrency.setText(s);
                }
            });
        }
        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (name != null && !name.equals(editable.toString())) {
                    buttonSave.setVisibility(View.VISIBLE);
                }
            }
        });
        buttonSave.setVisibility(View.INVISIBLE);
    }

    private void showMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void saveSettings() {
        if (editName.getText() != null && !editName.getText().toString().isEmpty()) {
            walletViewModel.saveWallet(editName.getText().toString(),
                    aBoolean -> {
                if (aBoolean) {
                    getActivity().onBackPressed();
                }
                else {
                    showMessage("Error while save wallet!");
                }
                    },
                    throwable -> {
                throwable.printStackTrace();
                showMessage("Error while save wallet!");
                    });
        }
    }

    private void choiceCurrencyToConvert() {
        buttonSave.setVisibility(View.VISIBLE);
        ArrayList<String> chains = new ArrayList<>(3);
        chains.add(Constants.USD);
        chains.add(Constants.EUR);
        ListDialogFragment.newInstance(chains, CurrencyType.FIAT).show(getFragmentManager(), "");
    }

    private void deleteWallet() {
        if (walletViewModel != null) {
            walletViewModel.removeWalletLive(aBoolean -> {
                        if (aBoolean) {
                            showMessage("The wallet was been deleted!");
                            //TODO:close fragment and show main screen
                        }
                        else {
                            showMessage("Error while deleting wallet!");
                        }
                    },
                    throwable -> {
                throwable.printStackTrace();
                showMessage("Error while deleting wallet!");
                    });
        }
    }

    @OnClick(R.id.button_cancel)
    void onCancelClick() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.button_save)
    void onSaveClick() {
        saveSettings();
    }

    @OnClick(R.id.button_currency)
    void onCurrencyClick() {
        choiceCurrencyToConvert();
    }

    @OnClick(R.id.button_key)
    void onKeyClick() {
        walletViewModel.showPrivateKey(key -> {
                    Toast.makeText(getContext(), key, Toast.LENGTH_SHORT).show();
                },
                throwable -> {
                    throwable.printStackTrace();
                    Toast.makeText(getContext(),
                            "Error while loading private key!",
                            Toast.LENGTH_SHORT).show();
                });
    }

    @OnClick(R.id.button_delete)
    void onDeleteClick() {
        SimpleDialogFragment dialogConfirmation = SimpleDialogFragment
                .newInstance(R.string.delete_wallet, R.string.delete_confirm, v -> deleteWallet());
        dialogConfirmation.show(getChildFragmentManager(),SimpleDialogFragment.class.getSimpleName());
    }
}
