package com.yasdalteam.yasdalege;

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

import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.yasdalteam.yasdalege.Networking.BaseResponse;
import com.yasdalteam.yasdalege.Networking.IResponseReceivable;
import com.yasdalteam.yasdalege.Networking.NetworkService;
import com.google.android.material.snackbar.Snackbar;
import com.yasdalteam.yasdalege.Networking.ResponseHandler;

public class RequestForm extends ViewHolder
{
    public static final int MAX_FILES_COUNT = 5;

    private BaseFragment fragment;
    private EditText etText;
    private Button btnSendRequest;
    private TextView tvRequestFormError;
    private TextView tvFilesPicked;
    private TextView tvAvailableChecks;
    private LinearLayout llCanCreateRequest;
    private LinearLayout llCantCreateRequest;
    private TextView tvPurchaseInstruction;
    private Button btnOpenStore;

    private ArrayList<MediaFile> data = new ArrayList<>();

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
        btnSendRequest.setOnClickListener(view1 ->
                submitDialogShow(RequestForm.this.data, etText.getText().toString()));
        tvFilesPicked = view.findViewById(R.id.tvFilesPicked);
        tvAvailableChecks = view.findViewById(R.id.tvAvailableChecks);
        llCanCreateRequest = view.findViewById(R.id.llCanCreateRequest);
        llCantCreateRequest = view.findViewById(R.id.llCantCreateRequest);
        btnOpenStore = view.findViewById(R.id.btnOpenStore);
        tvPurchaseInstruction = view.findViewById(R.id.tvPurchaseInstruction);

        btnOpenStore.setOnClickListener(button -> {
            BaseFragment shopFragment = ShopFragment.newInstance();
            ((MainActivity)fragment.getActivity()).replaceFragment(shopFragment, "shopFragment");
        });

        view.findViewById(R.id.btnSelectFiles).setOnClickListener(v -> {
            if(ContextCompat.checkSelfPermission(fragment.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(fragment.getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        2);
            }else{
                fragment.setFilepickCallback(RequestForm.this);
                Intent intent = new Intent(fragment.getContext(), FilePickerActivity.class);
                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                        .setCheckPermission(true)
                        .setShowImages(true)
                        .enableImageCapture(true)
                        .setMaxSelection(MAX_FILES_COUNT)
                        .setSkipZeroSizeFiles(true)
                        .build());
                fragment.getActivity().startActivityForResult(intent, App.FILE_REQUEST_CODE);
            }
        });
    }

    @Override
    public void bindExercise(Exercise exercise)
    {
        super.bindExercise(exercise);

        int availChecks = App.shared().getUser().getAvailableChecks();
        boolean authorized = App.shared().getUser().isAuthorized();
        if (authorized)
        {
            tvAvailableChecks.setText("Доступных проверок: " + availChecks);
            if (availChecks > 0)
            {
                llCanCreateRequest.setVisibility(View.VISIBLE);
                llCantCreateRequest.setVisibility(View.GONE);
            }
            else
            {
                llCantCreateRequest.setVisibility(View.VISIBLE);
                llCanCreateRequest.setVisibility(View.GONE);
                tvPurchaseInstruction.setText("Вы можете приобрести доступ к проверкам в разделе покупок");
                btnOpenStore.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            tvAvailableChecks.setText("Необходима авторизация");
            llCanCreateRequest.setVisibility(View.GONE);
            llCantCreateRequest.setVisibility(View.VISIBLE);
            tvPurchaseInstruction.setText("Авторизуйтесь, чтобы отправить своё выполненное задание.");
            btnOpenStore.setVisibility(View.GONE);
        }
    }

    public void selectFiles(ArrayList<MediaFile> files)
    {
        if (!files.isEmpty())
        {
            this.data = files;
            tvFilesPicked.setText("Выбрано файлов: " + files.size());
            tvFilesPicked.setVisibility(View.VISIBLE);
        }
        else
        {
            this.data.clear();
            tvFilesPicked.setVisibility(View.GONE);
        }
    }

    public void submitDialogShow(@Nullable ArrayList<MediaFile> files, String text)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
        builder.setMessage("Вы действительно хотите отправить заявку на проверку?");
        builder.setPositiveButton("OK", (dialog, id) ->
        {
            submitForm(files, text);
        });
        builder.setNegativeButton("Отмена", (dialog, id) ->
        {
            // User cancelled the dialog
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    enum ContentCombinations
    {

    }

    void submitForm(ArrayList<MediaFile> files, String _text)
    {
        tvRequestFormError.setLayoutParams(hiddenLayoutParams);
        List<MultipartBody.Part> parts = new ArrayList<>();

        if (!(files.isEmpty() && _text.isEmpty()))
        {
            if (!files.isEmpty())
            {
                for (MediaFile file : files)
                {
                    parts.add(createMultipartBodyPart(file.getUri()));
                }
            }
            RequestBody text = RequestBody.create(MediaType.parse("text/html"), _text);
            RequestBody exerciseId = RequestBody.create(MediaType.parse("text/html"), Long.toString(exercise.getId()));
            ProgressDialog dialog = ProgressDialog.show(fragment.getContext(), "",
                    "Отправка...", true);
            dialog.setCancelable(true);
            NetworkService networkService = NetworkService.getInstance(new ResponseHandler()
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

                    App.shared().getUser().decAvailableChecks();
                    if (fragment instanceof ExercisesListFragment)
                    {
                        ((ExercisesListFragment)fragment).exercisesListAdapter.notifyDataSetChanged();
                    }
                    if (fragment instanceof VariantFragment)
                    {
                        ((VariantFragment)fragment).exercisesListAdapter.notifyDataSetChanged();
                    }
                }
            });
            networkService.createRequest(exerciseId, parts, text);
        }
        else
        {
            tvRequestFormError.setText("Вы не можете отправить пустой запрос");
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
