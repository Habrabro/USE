package com.example.use;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.Nullable;

import java.util.List;

import okhttp3.MultipartBody;

public interface FilepickCallback
{
    void submitDialogShow(@Nullable Intent data);
}
