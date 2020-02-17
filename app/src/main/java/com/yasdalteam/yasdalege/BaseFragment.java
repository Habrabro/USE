package com.yasdalteam.yasdalege;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.yasdalteam.yasdalege.Networking.BaseResponse;
import com.yasdalteam.yasdalege.Networking.IResponseReceivable;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class BaseFragment extends Fragment implements IResponseReceivable, RequestForm.FilepickCallback
{
    private RequestForm requestForm;

    public void setFilepickCallback(RequestForm requestForm)
    {
        this.requestForm = requestForm;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        ArrayList<MediaFile> files = new ArrayList<>();
        if (data != null) {
            if (data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES) != null) {
                files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
            }
        }
        requestForm.selectFiles(files);
    }

    public void setSnackbar(Snackbar snackbar)
    {
        this.snackbar = snackbar;
    }

    public Snackbar getSnackbar()
    {
        return snackbar;
    }

    private Snackbar snackbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        App.shared().setCurrentFragment(this);
        Log.i("frag", getTag());
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResponse(BaseResponse response)
    {
        if (getActivity() != null)
        {
            ((MainActivity)getActivity()).onLoaded();
        }
    }

    @Override
    public void onFailure(Throwable t)
    {
        Log.i("networking", t.getMessage());
    }

    @Override
    public void onDisconnected()
    {
        ((MainActivity)getActivity()).onDisconnected();
    }

    @Override
    public void onError(String error)
    {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).onLoaded();
        }
        Log.i("networking", "Error " + error);
    }
}
