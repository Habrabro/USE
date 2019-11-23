package com.example.use;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.IResponseReceivable;
import com.example.use.Networking.NetworkService;
import com.google.android.material.snackbar.Snackbar;

public class RequestForm extends ViewHolder
{
    private BaseFragment fragment;
    private EditText etText;
    private Button btnSendRequest;
    private TextView tvRequestFormError;
    private TextView tvFilesPicked;
    private TextView tvAvailableChecks;
    private LinearLayout llCanCreateRequest;
    private LinearLayout llCantCreateRequest;

    private Intent data;

    LinearLayout.LayoutParams hiddenLayoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0);
    LinearLayout.LayoutParams shownLayoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    public RequestForm(BaseFragment fragment, View view, ExercisesListAdapter.Listener listener)
    {
        super(view, listener);
        this.fragment = fragment;

        etText = view.findViewById(R.id.etText);
        tvRequestFormError = view.findViewById(R.id.tvRequestFormError);
        btnSendRequest = view.findViewById(R.id.btnSendRequest);
        btnSendRequest.setOnClickListener(view1 -> submitDialogShow(RequestForm.this.data, etText.getText().toString()));
        tvFilesPicked = view.findViewById(R.id.tvFilesPicked);
        tvAvailableChecks = view.findViewById(R.id.tvAvailableChecks);
        llCanCreateRequest = view.findViewById(R.id.llCanCreateRequest);
        llCantCreateRequest = view.findViewById(R.id.llCantCreateRequest);

        view.findViewById(R.id.btnSelectFiles).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(fragment.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(fragment.getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            2);
                }else{
                    Intent intent = new Intent();
                    intent.setType("*/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    ((BaseFragment)fragment).setFilepickCallback(RequestForm.this);
                    fragment.startActivityForResult(Intent.createChooser(intent,"Выберите файл"), 1);
                }
            }
        });
    }

    @Override
    public void bindExercise(Exercise exercise)
    {
        super.bindExercise(exercise);

        int availChecks = App.getInstance().getUser().getAvailableChecks();
        tvAvailableChecks.setText("Доступных проверок: " + availChecks);
        if (availChecks > 0)
        {
            llCanCreateRequest.setLayoutParams(shownLayoutParams);
            llCantCreateRequest.setLayoutParams(hiddenLayoutParams);
        }
        else
        {
            llCantCreateRequest.setLayoutParams(shownLayoutParams);
            llCanCreateRequest.setLayoutParams(hiddenLayoutParams);
        }
    }

    public void selectFiles(@Nullable Intent data)
    {
        if (data != null)
        {
            this.data = new Intent(data);
            if (data.getClipData() != null)
            {
                tvFilesPicked.setText("Выбрано файлов: " + data.getClipData().getItemCount());
            }
            if (data.getData() != null)
            {
                tvFilesPicked.setText("Выбрано файлов: 1");
            }
            tvFilesPicked.setLayoutParams(shownLayoutParams);
        }
    }

    public void submitDialogShow(@Nullable Intent data, String text)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
        builder.setMessage("Вы действительно хотите отправить заявку на проверку?");
        builder.setPositiveButton("OK", (dialog, id) ->
        {
            submitForm(data, text);
        });
        builder.setNegativeButton("Отмена", (dialog, id) ->
        {
            // User cancelled the dialog
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void submitForm(@Nullable Intent data, String _text)
    {
        tvRequestFormError.setLayoutParams(hiddenLayoutParams);

        List<MultipartBody.Part> parts = new ArrayList<>();

        if (data != null || !_text.isEmpty())
        {
            if (data != null)
            {
                if (data.getClipData() != null)
                {
                    int clipDataCount = data.getClipData().getItemCount();
                    for (int i = 0; i < clipDataCount; i++)
                    {
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        parts.add(createMultipartBodyPart(uri));
                    }
                }
                else if (data.getData() != null)
                {
                    parts.add(createMultipartBodyPart(data.getData()));
                }
            }

            RequestBody exerciseId = RequestBody.create(MediaType.parse("text/html"), Long.toString(exercise.getId()));
            RequestBody text = RequestBody.create(MediaType.parse("text/html"), _text);
            ProgressDialog dialog = ProgressDialog.show(fragment.getContext(), "",
                    "Отправка...", true);
            dialog.setCancelable(true);
            NetworkService.getInstance(new IResponseReceivable()
            {
                @Override
                public void onResponse(BaseResponse response)
                {
                    dialog.dismiss();
                    Snackbar snackbar = Snackbar.make(
                            fragment.getView(),
                            "Вы успешно оставили запрос на проверку задания. " +
                                    "Посмотреть его статус вы можете в" +
                                    "профиле в разделе \"Ваши запросы на проверку\"",
                            Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("OK", view -> snackbar.dismiss());
                    View snackbarView = snackbar.getView();
                    TextView textView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
                    textView.setMaxLines(5);
                    snackbar.show();

                    App.getInstance().getUser().decAvailableChecks();
                    if (fragment instanceof ExercisesListFragment)
                    {
                        ((ExercisesListFragment)fragment).exercisesListAdapter.notifyDataSetChanged();
                    }
                    if (fragment instanceof VariantFragment)
                    {
                        ((VariantFragment)fragment).exercisesListAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Throwable t)
                {
                    dialog.dismiss();
                }

                @Override
                public void onError(String error)
                {
                    dialog.dismiss();
                }

                @Override
                public void onDisconnected()
                {
                    dialog.dismiss();
                }
            }).createRequest(exerciseId, parts, text);
        }
        else
        {
            tvRequestFormError.setLayoutParams(shownLayoutParams);
        }
    }

    private MultipartBody.Part createMultipartBodyPart(Uri uri) throws NullPointerException
    {
        File file = new File(FileUtil.getPath(uri, fragment.getContext()));
        String fileName = FileUtil.RusToEngTranslit(file.getName());
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        return  MultipartBody.Part.createFormData("userfile[]", fileName, requestBody);
    }

    public interface FilepickCallback
    {
        void setFilepickCallback(RequestForm requestForm);
    }
}
