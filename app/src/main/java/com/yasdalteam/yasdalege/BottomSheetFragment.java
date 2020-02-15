package com.yasdalteam.yasdalege;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;

import com.yasdalteam.yasdalege.Networking.BaseResponse;
import com.yasdalteam.yasdalege.Networking.IResponseReceivable;
import com.yasdalteam.yasdalege.Networking.NetworkService;
import com.yasdalteam.yasdalege.Networking.RegisterResponse;
import com.yasdalteam.yasdalege.Networking.UserResponse;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BottomSheetFragment extends BottomSheetDialogFragment implements IResponseReceivable
{
    private Dialog dialog;
    private BottomSheetBehavior bottomSheetBehavior;

    public View getContentView()
    {
        return contentView;
    }

    private View contentView;

    SignIn signIn;
    Registration registration;
    Profile profile;

    IResponseReceivable loginResponseHandler = new IResponseReceivable()
    {
        @Override
        public void onResponse(BaseResponse response)
        {
            User user = ((UserResponse)response).getData();
            App.shared().getUser().authorize(user);
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
                            "Неверный логин/пароль",
                            Snackbar.LENGTH_SHORT);
                    snackbar.getView().setTranslationZ(30);
                    snackbar.show();
            }
        }
    };

    class SignIn
    {
        private final String PASS_RESET_URL = App.shared().SERVER_BASE_URL + "password_reset.php";

        @BindView(R.id.btnLogin)
        Button loginButton;
        @BindView(R.id.etLogin)
        EditText etLogin;
        @BindView(R.id.etPassword)
        EditText etPassword;
        @BindView(R.id.tvGoToRegistration)
        TextView tvGoToRegistration;
        @BindView(R.id.tvPasswordResetButton)
        TextView tvPasswordResetButton;
        @BindView(R.id.btnVKAuth)
        ImageView btnVKAuth;

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

        @OnClick(R.id.tvPasswordResetButton)
        public void onPasswordResetButtonClick()
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PASS_RESET_URL));
            startActivity(browserIntent);
        }

        @OnClick(R.id.btnLogin)
        public void onLoginButtonClick()
        {
            loginButton.setEnabled(false);
            NetworkService networkService = NetworkService.getInstance(new AuthResponseReceiver(BottomSheetFragment.this)
            {
                @Override
                public void onResponse(BaseResponse response)
                {
                    super.onResponse(response);
                    loginButton.setEnabled(true);
                }

                @Override
                public void onError(String error)
                {
                    super.onError(error);
                    loginButton.setEnabled(true);
                }
            });
            networkService.login(etLogin.getText().toString(), etPassword.getText().toString());
        }

        @OnClick(R.id.btnVKAuth)
        public void onBtnVKAuthClick()
        {
            List<VKScope> scopes = new ArrayList<>();
            scopes.add(VKScope.OFFLINE);
            VK.login(getActivity(), scopes);
        }
    }

    class Registration
    {
        @BindView(R.id.btnBackToSignIn) ImageButton btnBackToSignIn;
        @BindView(R.id.etLogin) EditText etLogin;
        @BindView(R.id.etPassword) EditText etPassword;
        @BindView(R.id.etRepeatPassword) EditText etRepeatPassword;
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

        LinearLayout.LayoutParams hiddenLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0);
        LinearLayout.LayoutParams shownLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

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
            boolean validated = true;
            login = etLogin.getText().toString();
            password = etPassword.getText().toString();
            repeatPassword = etRepeatPassword.getText().toString();

            tvEmptyFieldsError.setLayoutParams(hiddenLayoutParams);
            tvLoginError.setLayoutParams(hiddenLayoutParams);
            tvPasswordError.setLayoutParams(hiddenLayoutParams);
            tvPasswordComparisonError.setLayoutParams(hiddenLayoutParams);

            NetworkService networkService = NetworkService.getInstance(new IResponseReceivable()
            {
                @Override
                public void onResponse(BaseResponse response)
                {
                    RegisterResponse registerResponse = (RegisterResponse)response;
                    Map<String, String> errorData = registerResponse.getData();
                    if (errorData.size() == 0)
                    {
                        NetworkService networkService = NetworkService.getInstance(loginResponseHandler);
                        networkService.login(login, password);
                    }
                    else
                    {
                        btnRegister.setEnabled(true);
                        if (errorData.get("login") != null)
                        {
                            tvLoginError.setText("Такой e-mail уже существует");
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

            if (login.isEmpty() || password.isEmpty())
            {
                tvEmptyFieldsError.setLayoutParams(shownLayoutParams);
                validated = false;
            }
            String regex = "[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
            Pattern emailMatchPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher emailMatcher = emailMatchPattern.matcher(login);
            if (!emailMatcher.find())
            {
                tvLoginError.setLayoutParams(shownLayoutParams);
                validated = false;
            }
            if (!password.equals(repeatPassword))
            {
                tvPasswordComparisonError.setLayoutParams(shownLayoutParams);
                validated = false;
            }
            if (validated)
            {
                btnRegister.setEnabled(false);
                networkService.register(login, password);
            }
        }
    }

    class Profile
    {
        private final String VK_GROUP_URL = "https://vk.com/";

        @BindView(R.id.btnLogout)
        Button logoutButton;
        @BindView(R.id.llFavoriteExercises)
        LinearLayout favoriteExercises;
        @BindView(R.id.llCompletedExercises)
        LinearLayout completedExercises;
        @BindView(R.id.llRequests)
        LinearLayout savedVariants;
        @BindView(R.id.tvProfileTitle)
        TextView tvProfileTitle;
        @BindView(R.id.tvAvailableChecks)
        TextView tvAvailableChecks;
        @BindView(R.id.btnWeAreInVk)
        ImageButton btnWeAreInVk;

        public View getProfileLayout()
        {
            return profileLayout;
        }

        View profileLayout;

        public Profile()
        {
            profileLayout = View.inflate(getContext(), R.layout.profile_layout, null);
            ButterKnife.bind(this, profileLayout);

            User user = App.shared().getUser();
            if (user.getName() == null)
            {
                tvProfileTitle.setText(user.getLogin());
            }
            else
            {
                tvProfileTitle.setText(user.getName());
            }
            tvAvailableChecks.setText("Доступных проверок: " + user.getAvailableChecks());
        }

        @OnClick(R.id.llFavoriteExercises)
        public void onFavoriteExercisesClick()
        {
            ((MainActivity)getActivity()).replaceFragment(
                    ExercisesListFragment.newInstance(listener ->
                    {
                        String limit = ((ExercisesListFragment)listener).getPage() *
                                ((ExercisesListFragment)listener).getItemsPerLoad() + "," +
                                ((ExercisesListFragment)listener).getItemsPerLoad();
                        NetworkService networkService = NetworkService.getInstance(listener);
                        networkService.getFavoriteExercises(
                                App.shared().getUser().getSessionId(), limit, true);
                    }),
                    "favoriteExercises");
            BottomSheetFragment.this.dismiss();
        }

        @OnClick(R.id.llCompletedExercises)
        public void onCompletedExercisesClick()
        {
            ((MainActivity)getActivity()).replaceFragment(
                    ExercisesListFragment.newInstance(listener ->
                    {
                        String limit = ((ExercisesListFragment)listener).getPage() *
                                ((ExercisesListFragment)listener).getItemsPerLoad() + "," +
                                ((ExercisesListFragment)listener).getItemsPerLoad();
                        NetworkService networkService = NetworkService.getInstance(listener);
                        networkService.getCompletedExercises(
                                App.shared().getUser().getSessionId(), limit, true);
                    }),
                    "completedExercises");
            BottomSheetFragment.this.dismiss();
        }

        @OnClick(R.id.llRequests)
        public void onRequestsClick()
        {
            ((MainActivity)getActivity()).replaceFragment(
                    RequestsListFragment.newInstance(),
                    "RequestsListFragment");
            BottomSheetFragment.this.dismiss();
        }

        @OnClick(R.id.btnWeAreInVk)
        public void onBtnWeAreInVkClick()
        {
            final Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(VK_GROUP_URL));
            startActivity(intent);
        }

        @OnClick (R.id.btnLogout)
        public void onLogoutButtonClick()
        {
            NetworkService.getInstance(new IResponseReceivable()
            {
                @Override
                public void onResponse(BaseResponse response)
                {
                    App.shared().getUser().logout();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    for(int i = 0; i < fm.getBackStackEntryCount(); ++i)
                    {
                        fm.popBackStack();
                    }
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
        if (App.shared().getUser().isAuthorized())
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
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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
