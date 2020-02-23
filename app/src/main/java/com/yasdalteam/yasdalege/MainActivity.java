package com.yasdalteam.yasdalege;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.security.ProviderInstaller;
import com.yasdalteam.yasdalege.Networking.BaseResponse;
import com.yasdalteam.yasdalege.Networking.NetworkService;
import com.yasdalteam.yasdalege.Networking.ResponseHandler;
import com.yasdalteam.yasdalege.Networking.UserResponse;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.yandex.money.android.sdk.Checkout;
import ru.yandex.money.android.sdk.TokenizationResult;

import com.vk.api.sdk.*;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.yasdalteam.yasdalege.Payments.Payment;
import com.yasdalteam.yasdalege.Payments.PaymentCache;
import com.yasdalteam.yasdalege.Payments.PaymentCancellations;
import com.yasdalteam.yasdalege.Payments.PaymentResponse;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements VKAuthCallback
{
    private FragmentManager fragmentManager;

    BottomSheetFragment bottomSheet;

    @BindView(R.id.btnProfile) Button profileButton;
    @BindView(R.id.btnShop) Button shopButton;
    private AdView adView;

    ResponseHandler seccessPurchaseResponseHandler = new ResponseHandler() {
        @Override
        public void onResponse(BaseResponse response)
        {
            super.onResponse(response);

            User user = App.shared().getUser();
            PaymentCache paymentCache = App.shared().getPaymentCache();
            user.setAvailableChecks(user.getAvailableChecks() + paymentCache.getCountOfChecks());
            if (user.isAdsEnabled())
            {
                user.setAdsEnabled(!paymentCache.doesDisableAds());
            }
            App.shared().getUser().authorize(user);
            Messager.message("Покупка совершена!");
        }

        @Override
        public void onError(String error)
        {
            super.onError(error);
            displayError(error);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case 282:
                if (!VK.onActivityResult(requestCode, resultCode, data, new VKAuthCallback()
                {
                    @Override
                    public void onLogin(@NotNull VKAccessToken vkAccessToken)
                    {
                        NetworkService.getInstance(new ResponseHandler()
                        {
                            @Override
                            public void onResponse(BaseResponse response)
                            {
                                User user = ((UserResponse)response).getData();
                                App.shared().getUser().authorize(user);
                                bottomSheet.reset();
                            }
                        }).vkAuth(vkAccessToken.getAccessToken());
                    }
                    @Override
                    public void onLoginFailed(int i)
                    {

                    }
                }))
                {
                    super.onActivityResult(requestCode, resultCode, data);
                }
                break;
            case ShopFragment.REQUEST_CODE_TOKENIZE:
                switch (resultCode) {
                    case RESULT_OK:
                        Loader.showOverlay("", "Соединяемся с платёжной системой", this);
                        TokenizationResult result = Checkout.createTokenizationResult(data);
                        ResponseHandler responseHandler = new ResponseHandler()
                        {
                            @Override
                            public void onResponse(BaseResponse response)
                            {
                                super.onResponse(response);

                                Payment payment = ((PaymentResponse) response).getData();
                                App.shared().getPaymentCache().setPayment(payment);
                                switch (payment.getPaymentMethod().getType()) {
                                    case "bank_card":
                                        Intent intent3ds = Checkout.create3dsIntent(
                                                MainActivity.this,
                                                payment.getConfirmation().getConfirmationUrl()
                                        );
                                        startActivityForResult(intent3ds, ShopFragment.REQUEST_CODE_3DS);
                                        break;
                                    case "yandex_money":
                                        NetworkService.getInstance(seccessPurchaseResponseHandler).acceptPayment(
                                                App.shared().getPaymentCache().getPayment().getId()
                                        );
                                        break;
                                }
                            }

                            @Override
                            public void onError(String error)
                            {
                                super.onError(error);
                                displayError(error);
                            }
                        };
                        PaymentCache paymentCache = App.shared().getPaymentCache();
                        NetworkService.getInstance(responseHandler).createPayment(
                                result.getPaymentToken(),
                                paymentCache.getAmount().intValue(),
                                paymentCache.getDescription(),
                                paymentCache.getShopItemId());
                        break;
                    case RESULT_CANCELED:
                        break;
                }
                break;
            case ShopFragment.REQUEST_CODE_3DS:
                switch (resultCode)
                {
                    case RESULT_OK:
                        NetworkService.getInstance(seccessPurchaseResponseHandler).acceptPayment(App.shared().getPaymentCache().getPayment().getId());
                        break;
                    case Checkout.RESULT_ERROR:
                        Messager.message("Ошибка: " + data.getStringExtra(Checkout.EXTRA_ERROR_DESCRIPTION));
                        break;
                    case RESULT_CANCELED:
                        break;
                }
                break;
            case App.FILE_REQUEST_CODE:
                App.shared().getCurrentFragment().onActivityResult(requestCode, resultCode, data);
                switch (resultCode)
                {
                    case RESULT_OK:
                        break;
                    case RESULT_CANCELED:
                        break;
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        App.shared().configureUser(user -> {
            adView = findViewById(R.id.adView);
            App.shared().getAdsService().setAdView(adView);
            if (user == null) {
                App.shared().getUser().logout();
                App.shared().getAdsService().enableAds();
            } else {
                App.shared().getUser().authorize(user);
            }
        });

        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.action_bar);

        ButterKnife.bind(this, actionBar.getCustomView());

        fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag("subjectsListFragment") == null)
        {
            SubjectsListFragment subjectsListFragment = SubjectsListFragment.newInstance();
            AdsFragment adsFragment = AdsFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, subjectsListFragment, "subjectsListFragment")
                    .commit();
        }

        ProviderInstaller.installIfNeededAsync(this, new ProviderInstaller.ProviderInstallListener()
        {
            @Override
            public void onProviderInstalled() {}

            @Override
            public void onProviderInstallFailed(int i, Intent intent) {}
        });
    }

    @Override
    public void onLogin(@NotNull VKAccessToken vkAccessToken)
    {
        Log.i("vkauth", vkAccessToken.toString());
    }

    @Override
    public void onLoginFailed(int i)
    {

    }

    @OnClick(R.id.btnShop)
    public void onShopButtonClick()
    {
        BaseFragment fragment = ShopFragment.newInstance();
        replaceFragment(fragment, "shopFragment");
    }

    @OnClick(R.id.btnProfile)
    public void onProfileButtonClick()
    {
//        View bottomSheet = App.shared().getCurrentFragment().getView().findViewById(R.id.bottomSheet);
//        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
//        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        if (fragmentManager.findFragmentByTag("bottomSheet") == null)
        {
            bottomSheet = BottomSheetFragment.newInstance();
            bottomSheet.show(fragmentManager, "bottomSheet");
        }
    }

    public void replaceFragment (Fragment newFragment, String newFragmentTag)
    {
        String backStateName =  newFragmentTag;
        String fragmentTag = backStateName;

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.setCustomAnimations(R.animator.fragment_transition_slide_in,
                    android.R.animator.fade_out);
            ft.replace(R.id.fragmentContainer, newFragment, fragmentTag);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    public void onLoad()
    {
        Loader.show();
    }

    public void onLoaded()
    {
        Loader.hide();
    }

    public void onDisconnected()
    {
        Loader.show("Нет подключения к сети", Snackbar.LENGTH_LONG);
    }

    private void displayError(String error)
    {
        String errorMessage = "Что-то пошло не так...";
        if (error != "Response is null")
        {
            try {
                errorMessage = PaymentCancellations.getMap().get(Integer.parseInt(error));
            } catch (NumberFormatException exception) {}
        }
        Messager.message(errorMessage);
    }
}
