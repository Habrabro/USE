package com.example.use;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.IResponseReceivable;
import com.example.use.Networking.NetworkService;
import com.example.use.Networking.UserResponse;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BottomSheetFragment extends BottomSheetDialogFragment implements IResponseReceivable
{
    private BottomSheetBehavior behavior;
    @BindView(R.id.btnLogin) Button loginButton;
    @BindView(R.id.etLogin) EditText etLogin;
    @BindView(R.id.etPassword) EditText etPassword;

    View contentView;

    public BottomSheetFragment()
    {

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
        contentView = View.inflate(getContext(), R.layout.layout_bottom_sheet, null);
        dialog.setContentView(contentView);
        ButterKnife.bind(this, contentView);
        CoordinatorLayout.LayoutParams layoutParams = ((CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams());
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior)
        {

        }
    }

    @OnClick(R.id.btnLogin)
    public void onLoginButtonClick()
    {
        NetworkService networkService = NetworkService.getInstance(this);
        networkService.login(etLogin.getText().toString(), etPassword.getText().toString());
    }

    @Override
    public void onResponse(BaseResponse response)
    {
        UserResponse user = (UserResponse)response;
        App.getInstance().getUser().authorize(user.getData());

        Snackbar snackbar = Snackbar.make(
                contentView,
                "Authorized",
                Snackbar.LENGTH_SHORT);
        snackbar.getView().setTranslationZ(30);
        snackbar.show();
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
}
