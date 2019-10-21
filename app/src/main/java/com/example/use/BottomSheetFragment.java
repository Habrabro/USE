package com.example.use;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;

import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.IResponseReceivable;
import com.example.use.Networking.NetworkService;
import com.example.use.Networking.UserResponse;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BottomSheetFragment extends BottomSheetDialogFragment implements IResponseReceivable
{
    private Dialog dialog;
    private BottomSheetBehavior bottomSheetBehavior;

    private View contentView;

    SignIn signIn;
    Registration registration;
    Profile profile;

    class SignIn
    {
        @BindView(R.id.btnLogin)
        Button loginButton;
        @BindView(R.id.etLogin)
        EditText etLogin;
        @BindView(R.id.etPassword)
        EditText etPassword;
        @BindView(R.id.tvGoToRegistration)
        TextView tvGoToRegistration;

        public View getSignInLayout()
        {
            return signInLayout;
        }

        View signInLayout;

        public SignIn()
        {
            signInLayout = View.inflate(getContext(), R.layout.layout_bottom_sheet, null);
            ButterKnife.bind(this, signInLayout);
        }

        @OnClick(R.id.tvGoToRegistration)
        public void onGoToRegClick()
        {
            ViewGroupUtils.replaceView(
                    signIn.getSignInLayout(),
                    registration.getRegistrationLayout());
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

        @OnClick(R.id.btnLogin)
        public void onLoginButtonClick()
        {
            NetworkService networkService = NetworkService.getInstance(new IResponseReceivable()
            {
                @Override
                public void onResponse(BaseResponse response)
                {
                    UserResponse user = (UserResponse)response;
                    App.getInstance().getUser().authorize(user.getSessionId());

                    Snackbar snackbar = Snackbar.make(
                            contentView,
                            "Authorized",
                            Snackbar.LENGTH_SHORT);
                    snackbar.getView().setTranslationZ(30);
                    snackbar.show();
                    reset();
                }

                @Override
                public void onDisconnected()
                {
                    BottomSheetFragment.this.onDisconnected();
                }

                @Override
                public void onFailure(Throwable t)
                {

                }

                @Override
                public void onError(String error)
                {
                    switch (error)
                    {
                        case "401":
                            Snackbar snackbar = Snackbar.make(
                                    contentView,
                                    "Incorrect login/password",
                                    Snackbar.LENGTH_SHORT);
                            snackbar.getView().setTranslationZ(30);
                            snackbar.show();
                    }
                }
            });
            networkService.login(etLogin.getText().toString(), etPassword.getText().toString());
        }
    }

    class Registration
    {
        @BindView(R.id.btnBackToSignIn) ImageButton btnBackToSignIn;
        @BindView(R.id.etLogin) EditText etLogin;
        @BindView(R.id.etPassword) EditText etPassword;
        @BindView(R.id.etRepeatPassword) EditText etRepeatPassword;
        @BindView(R.id.etEmail) EditText etEmail;
        @BindView(R.id.tvEmptyFieldsError) TextView tvEmptyFieldsError;
        @BindView(R.id.tvLoginError) TextView tvLoginError;
        @BindView(R.id.tvPasswordError) TextView tvPasswordError;
        @BindView(R.id.tvPasswordComparisonError) TextView tvPasswordComparisonError;
        @BindView(R.id.tvEmailError) TextView tvEmailError;
        @BindView(R.id.btnRegister) TextView btnRegister;

        private String login, password, email, repeatPassword;

        public View getRegistrationLayout()
        {
            return registrationLayout;
        }

        View registrationLayout;

        public Registration()
        {
            registrationLayout = View.inflate(getContext(), R.layout.layout_bottom_sheet_register, null);
            ButterKnife.bind(this, registrationLayout);
        }

        @OnClick(R.id.btnBackToSignIn)
        public void onBackToSignInClick()
        {
            ViewGroupUtils.replaceView(
                    registration.getRegistrationLayout(),
                    signIn.getSignInLayout());
        }

        @OnClick(R.id.btnRegister)
        public void onBtnRegisterClick()
        {
            login = etLogin.getText().toString();
            password = etPassword.getText().toString();
            repeatPassword = etRepeatPassword.getText().toString();
            email = etEmail.getText().toString();

            LinearLayout.LayoutParams hiddenLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0);
            LinearLayout.LayoutParams shownLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            tvLoginError.setLayoutParams(hiddenLayoutParams);
            tvPasswordError.setLayoutParams(hiddenLayoutParams);
            tvPasswordComparisonError.setLayoutParams(hiddenLayoutParams);
            tvEmailError.setLayoutParams(hiddenLayoutParams);

            NetworkService networkService = NetworkService.getInstance(new IResponseReceivable()
            {
                @Override
                public void onResponse(BaseResponse response)
                {
                    UserResponse userResponse = (UserResponse)response;
                    Map<String, String> errorData = userResponse.getData();
                    if (errorData.size() == 0)
                    {
                        Toast.makeText(getContext(), "wadawd", Toast.LENGTH_SHORT);
                    }
                    else
                    {
                        if (errorData.get("login") != null)
                        {
                            tvLoginError.setLayoutParams(shownLayoutParams);
                        }
                    }
                }

                @Override
                public void onDisconnected()
                {
                    BottomSheetFragment.this.onDisconnected();
                }

                @Override public void onFailure(Throwable t) { }
                @Override public void onError(String error) {
                    //
                }
            });

            if (!login.isEmpty() && !password.isEmpty() && !email.isEmpty())
            {
                if (password.equals(repeatPassword))
                {
                    networkService.register(login, password, email);
                }
                else
                {
                    tvPasswordComparisonError.setLayoutParams(shownLayoutParams);
                }
            }
            else
            {
                tvEmptyFieldsError.setLayoutParams(shownLayoutParams);
            }
        }
    }

    class Profile
    {
        @BindView(R.id.btnLogout)
        Button logoutButton;

        public View getProfileLayout()
        {
            return profileLayout;
        }

        View profileLayout;

        public Profile()
        {
            profileLayout = View.inflate(getContext(), R.layout.profile_layout, null);
            ButterKnife.bind(this, profileLayout);
        }

        @OnClick (R.id.btnLogout)
        public void onLogoutButtonClick()
        {
            NetworkService.getInstance(new IResponseReceivable()
            {
                @Override
                public void onResponse(BaseResponse response)
                {
                    App.getInstance().getUser().logout();
                    Snackbar snackbar = Snackbar.make(
                            contentView,
                            "Logged out",
                            Snackbar.LENGTH_SHORT);
                    snackbar.getView().setTranslationZ(30);
                    snackbar.show();
                    reset();
                }

                @Override
                public void onDisconnected()
                {
                    BottomSheetFragment.this.onDisconnected();
                }

                @Override
                public void onFailure(Throwable t)
                {

                }

                @Override
                public void onError(String error)
                {

                }
            }).logout();
        }
    }

    public BottomSheetFragment()
    {

    }

    public void reset()
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        BottomSheetFragment.this.dismiss();
        BottomSheetFragment.this.show(fragmentManager, "bottomSheet");
    }

    public static BottomSheetFragment newInstance()
    {
        BottomSheetFragment fragment = new BottomSheetFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style)
    {
        super.setupDialog(dialog, style);
        this.dialog = dialog;
        signIn = new SignIn();
        registration = new Registration();
        profile = new Profile();
        if (App.getInstance().getUser().isAuthorized())
        {
            contentView = profile.getProfileLayout();
        }
        else
        {
            contentView = signIn.getSignInLayout();
        }
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams layoutParams = ((CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams());
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        bottomSheetBehavior = (BottomSheetBehavior) behavior;
    }

    @Override
    public void onResponse(BaseResponse response)
    {

    }

    @Override
    public void onDisconnected()
    {
        Snackbar snackbar = Snackbar.make(
                contentView,
                "Disconnected",
                Snackbar.LENGTH_INDEFINITE);
        snackbar.getView().setTranslationZ(130);
        snackbar.show();
    }

    @Override
    public void onFailure(Throwable t)
    {

    }

    @Override
    public void onError(String error)
    {

    }
}
