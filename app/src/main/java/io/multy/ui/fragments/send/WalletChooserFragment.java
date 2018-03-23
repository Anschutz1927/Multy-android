/*
 * Copyright 2017 Idealnaya rabota LLC
 * Licensed under Multy.io license.
 * See LICENSE for details
 */

package io.multy.ui.fragments.send;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.multy.R;
import io.multy.api.MultyApi;
import io.multy.model.entities.wallet.Wallet;
import io.multy.model.responses.SingleWalletResponse;
import io.multy.storage.RealmManager;
import io.multy.ui.activities.AssetSendActivity;
import io.multy.ui.adapters.MyWalletsAdapter;
import io.multy.ui.fragments.BaseFragment;
import io.multy.util.Constants;
import io.multy.util.CryptoFormatUtils;
import io.multy.util.NativeDataHelper;
import io.multy.util.analytics.Analytics;
import io.multy.viewmodels.AssetSendViewModel;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WalletChooserFragment extends BaseFragment implements MyWalletsAdapter.OnWalletClickListener {

    public static WalletChooserFragment newInstance() {
        return new WalletChooserFragment();
    }

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private AssetSendViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_chooser, container, false);
        ButterKnife.bind(this, view);
        viewModel = ViewModelProviders.of(getActivity()).get(AssetSendViewModel.class);
        setupAdapter();
        if (getActivity() != null && !getActivity().getIntent().hasExtra(Constants.EXTRA_WALLET_ID)) {
            Analytics.getInstance(getActivity()).logSendFromLaunch();
        }
        return view;
    }

    @Override
    public void onWalletClick(Wallet wallet) {
        viewModel.isLoading.setValue(true);
        MultyApi.INSTANCE.getWalletVerbose(wallet.getIndex(), wallet.getCurrencyId(), wallet.getNetworkId())
                .enqueue(new Callback<SingleWalletResponse>() {
            @Override
            public void onResponse(Call<SingleWalletResponse> call, Response<SingleWalletResponse> response) {
                viewModel.isLoading.setValue(false);
                if (response.isSuccessful() && response.body().getWallets() != null && response.body().getWallets().size() > 0) {
                    Wallet wallet = response.body().getWallets().get(0);
                    RealmManager.getAssetsDao().saveWallet(wallet);
                    viewModel.setWallet(wallet);
                    proceed(wallet);
                } else {
                    proceed(wallet);
                }

                viewModel.isLoading.postValue(false);
            }

            @Override
            public void onFailure(Call<SingleWalletResponse> call, Throwable t) {
                viewModel.isLoading.postValue(false);
                proceed(wallet);
            }
        });
    }

    private void proceed(Wallet wallet) {
        if (viewModel.isAmountScanned()) {
            if (Double.parseDouble(CryptoFormatUtils
                    .satoshiToBtc(wallet.getPendingBalance().longValue())) >= viewModel.getAmount()) {
                launchTransactionFee(wallet);
            } else {
                Toast.makeText(getContext(), getString(R.string.no_balance), Toast.LENGTH_SHORT).show();
            }
        } else {
            launchTransactionFee(wallet);
        }
    }

    private void setupAdapter() {
        RealmResults<Wallet> wallets = RealmManager.getAssetsDao()
                .getWallets(viewModel.getWallet().getCurrencyId(), viewModel.getWallet().getNetworkId());
        recyclerView.setAdapter(new MyWalletsAdapter(this, wallets));
    }

    private void launchTransactionFee(Wallet wallet) {
        viewModel.setWallet(wallet);
        if (wallet.getCurrencyId() == NativeDataHelper.Blockchain.BTC.getValue()) {
            ((AssetSendActivity) getActivity()).setFragment(R.string.transaction_fee, R.id.container,
                    TransactionFeeFragment.newInstance());
        } else if (wallet.getCurrencyId() == NativeDataHelper.Blockchain.ETH.getValue()) {
            ((AssetSendActivity) getActivity()).setFragment(R.string.transaction_fee, R.id.container,
                    EthTransactionFeeFragment.newInstance());
        }
    }

}