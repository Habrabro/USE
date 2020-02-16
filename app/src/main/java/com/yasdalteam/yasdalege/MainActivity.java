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
import com.yasdalteam.yasdalege.Networking.IResponseReceivable;
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
import com.yasdalteam.yasdalege.Payments.PaymentResponse;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements SubjectMenuFragment.Listener,
        SubjectsListFragment.Listener, TopicsListFragment.Listener, VKAuthCallback
{
    private FragmentManager fragmentManager;

    BottomSheetFragment bottomSheet;

    @BindView(R.id.btnProfile) Button profileButton;
    @BindView(R.id.btnShop) Button shopButton;
    private Snackbar snackbar;
    private AdView adView;

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
                        NetworkService.getInstance(new IResponseReceivable()
                        {
                            @Override
                            public void onResponse(BaseResponse response)
                            {
                                User user = ((UserResponse)response).getData();
                                App.shared().getUser().authorize(user);
                                bottomSheet.reset();
                            }
                            @Override public void onFailure(Throwable t){}@Override public void onError(String error){}@Override public void onDisconnected(){}
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
            case RESULT_OK:
                // successful tokenization
                TokenizationResult result = Checkout.createTokenizationResult(data);
                NetworkService.getInstance(new ResponseHandler() {
                    @Override
                    public void onResponse(BaseResponse response)
                    {
                        super.onResponse(response);

                        Payment payment = ((PaymentResponse)response).getData();
                        Log.i("Payment", "received");
                        switch (payment.getPaymentMethod().getType())
                        {
                            case "bank_card":
//                                        Intent intent = Checkout.create3dsIntent(
//                                                MainActivity.this, payment);
                        }
                    }
                }).createPayment(result.getPaymentToken(), "Hello! I'm description!");
                break;
            case RESULT_CANCELED:
                // user canceled tokenization

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

    @Override
    public void onSubjectsListFragmentInteraction(Subject subject)
    {
        if (fragmentManager.findFragmentByTag("subjectsListFragment") != null &&
        fragmentManager.findFragmentByTag("subjectMenuFragment") == null)
        {
            SubjectMenuFragment subjectMenuFragment = SubjectMenuFragment.newInstance(subject);
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.animator.fragment_transition_slide_in,
                            android.R.animator.fade_out)
                    .replace(R.id.fragmentContainer, subjectMenuFragment, "subjectMenuFragment")
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onDirectoryFragmentDisplay(Subject subject)
    {
        if (fragmentManager.findFragmentByTag("subjectMenuFragment") != null &&
                fragmentManager.findFragmentByTag("directoryFragment") == null)
        {
            DirectoryFragment directoryFragment = DirectoryFragment.newInstance(subject);
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.animator.fragment_transition_slide_in,
                            android.R.animator.fade_out)
                    .replace(R.id.fragmentContainer, directoryFragment, "directoryFragment")
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onTopicsListFragmentDisplay(long subjectId)
    {
        if (fragmentManager.findFragmentByTag("subjectMenuFragment") != null &&
                fragmentManager.findFragmentByTag("topicsListFragment") == null)
        {
            TopicsListFragment topicsListFragment = TopicsListFragment.newInstance(subjectId);
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.animator.fragment_transition_slide_in,
                            android.R.animator.fade_out)
                    .replace(R.id.fragmentContainer, topicsListFragment, "topicsListFragment")
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onTopicsListFragmentInteraction(long topicId, long number)
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
            ft.replace(R.id.fragmentContainer, newFragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    public void onLoad()
    {
        snackbar = Snackbar.make(
                findViewById(R.id.fragmentContainer),
                "Загрузка",
                Snackbar.LENGTH_INDEFINITE);
        snackbar.getView().setTranslationZ(130);
        snackbar.show();
    }

    public void onLoaded()
    {
        if (snackbar != null)
        {
            snackbar.dismiss();
        }
    }

    public void onDisconnected()
    {
        snackbar = Snackbar.make(
                findViewById(R.id.fragmentContainer),
                "Нет подключения к сети",
                Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
