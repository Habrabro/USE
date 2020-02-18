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
import com.yasdalteam.yasdalege.Payments.PaymentCache;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;

import butterknife.OnClick;
import ru.yandex.money.android.sdk.Amount;
import ru.yandex.money.android.sdk.Checkout;
import ru.yandex.money.android.sdk.MockConfiguration;
import ru.yandex.money.android.sdk.PaymentMethodType;
import ru.yandex.money.android.sdk.PaymentParameters;
import ru.yandex.money.android.sdk.SavePaymentMethod;
import ru.yandex.money.android.sdk.TestParameters;

public class ShopFragment extends BaseFragment
{
    final static String API_KEY = "live_NjcxMTM1ZGh_I99DzD-wcokJ8foiseaKa6fMjXjdago";
    final static String SHOP_ID = "671135";
    final static int REQUEST_CODE_TOKENIZE = 755;
    final static int REQUEST_CODE_3DS = 765;

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

        Loader.show();
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
                buySomething(item);
            });
        }
    }

    private void buySomething(ShopItem item)
    {
        PaymentCache paymentCache = new PaymentCache(
                new BigDecimal(item.getPrice()),
                item.getId(),
                item.getDescription()
        );
        App.shared().setPaymentCache(paymentCache);

        NavigableSet<PaymentMethodType> methodsSet = new TreeSet<>();
        methodsSet.add(PaymentMethodType.BANK_CARD);
//        methodsSet.add(PaymentMethodType.YANDEX_MONEY);

        PaymentParameters paymentParameters = new PaymentParameters(
                new Amount(new BigDecimal(item.getPrice()), Currency.getInstance("RUB")),
                item.getName(),
                item.getDescription(),
                API_KEY,
                SHOP_ID,
                SavePaymentMethod.USER_SELECTS,
                methodsSet
        );
        TestParameters testParameters = new TestParameters(true, true);
        Intent intent = Checkout.createTokenizeIntent(getActivity(), paymentParameters, testParameters);
        getActivity().startActivityForResult(intent, REQUEST_CODE_TOKENIZE);
    }

    @OnClick(R.id.btnCloseShop)
    public void onBtnCloseShop()
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }
}
