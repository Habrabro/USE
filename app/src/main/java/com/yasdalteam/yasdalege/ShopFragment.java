package com.yasdalteam.yasdalege;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.yasdalteam.yasdalege.Networking.BaseResponse;
import com.yasdalteam.yasdalege.Networking.NetworkService;
import com.yasdalteam.yasdalege.Networking.ShopItem;
import com.yasdalteam.yasdalege.Networking.PriceListResponse;
import com.yasdalteam.yasdalege.Networking.ResponseHandler;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import butterknife.OnClick;
import ru.yandex.money.android.sdk.Amount;
import ru.yandex.money.android.sdk.Checkout;
import ru.yandex.money.android.sdk.MockConfiguration;
import ru.yandex.money.android.sdk.PaymentParameters;
import ru.yandex.money.android.sdk.SavePaymentMethod;
import ru.yandex.money.android.sdk.TestParameters;

public class ShopFragment extends BaseFragment
{
    final static String API_KEY = "test_NjcyMTk0WLhKiHlVgWILsSsULlz2wxHSG8axSYO8wCs";
    final static int REQUEST_CODE_TOKENIZE = 755;

    @BindView(R.id.btnCloseShop)
    Button btnCloseShop;

    private List<ShopItem> priceList;

    public ShopFragment() {}

    public static ShopFragment newInstance()
    {
        ShopFragment fragment = new ShopFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.shop_fragment_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        ResponseHandler responseHandler = new ResponseHandler()
        {
            @Override
            public void onResponse(BaseResponse response)
            {
                super.onResponse(response);
                Loader.hide();
                setupPriceListView(((PriceListResponse)response).getData());
            }
        };
        Loader.show();
        NetworkService.getInstance(responseHandler).getPriceList();
    }

    void setupPriceListView(List<ShopItem> priceList)
    {
        this.priceList = priceList;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout tableView = getView().findViewById(R.id.llMainContainer);

        for (ShopItem item : priceList)
        {
            View rowView = inflater.inflate(R.layout.price_list_table_row, null);
            tableView.addView(rowView);

            TextView countOfChecksCol = rowView.findViewById(R.id.tvCountOfChecksCol);
            TextView priceCol = rowView.findViewById(R.id.tvPriceCol);
            TextView disablesAdsCol = rowView.findViewById(R.id.tvDisablesAdsCol);
            Button buyButton = rowView.findViewById(R.id.btnBuy);

            countOfChecksCol.setText(item.getCountOfChecks());
            priceCol.setText(item.getPrice());
            String isAdsDisabledLocalizedString = item.getDisablesAds() ? "Да" : "Нет";
            disablesAdsCol.setText(isAdsDisabledLocalizedString);
            buyButton.setOnClickListener(view ->
            {
                buySomething(item.getId());
            });
        }
    }

    private void buySomething(int id)
    {
//        NavigableSet<PaymentMethodType> methodsSet = new TreeSet<>();
//        methodsSet.add(PaymentMethodType.BANK_CARD);
//        methodsSet.add(PaymentMethodType.SBERBANK);
//        methodsSet.add(PaymentMethodType.GOOGLE_PAY);

        PaymentParameters paymentParameters = new PaymentParameters(
                new Amount(BigDecimal.ONE, Currency.getInstance("RUB")),
                "1 проверка",
                "",
                API_KEY,
                "",
                SavePaymentMethod.USER_SELECTS
        );
        TestParameters testParameters = new TestParameters(true, true, new MockConfiguration(false, true, 5));
        Intent intent = Checkout.createTokenizeIntent(getActivity(), paymentParameters);
        startActivityForResult(intent, REQUEST_CODE_TOKENIZE);
    }

    @OnClick(R.id.btnCloseShop)
    public void onBtnCloseShop()
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }
}
