package com.yasdalteam.yasdalege;

import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DirectoryParser
{
    private String regex = "\\[img].*?\\[\\/img]";
    LinearLayout linearLayout;

    public DirectoryParser(LinearLayout linearLayout)
    {
        this.linearLayout = linearLayout;
    }

    public void parseContent(String content)
    {
        int stringStart = 0;
        int stringEnd;

        Pattern imgMatchPattern = Pattern.compile(regex);
        Matcher imgMatcher = imgMatchPattern.matcher(content);
        while (imgMatcher.find()) {
            stringEnd = imgMatcher.start();
            String string = content.substring(stringStart, stringEnd);
            String imgSrc = imgMatcher.group().substring(5, imgMatcher.group().length() - 6).trim();
            stringStart = imgMatcher.end();
            linearLayout.addView(getTextContentView(linearLayout, string));
            linearLayout.addView(getImageContentView(linearLayout, imgSrc));
        }
        stringEnd = content.length();
        String string = content.substring(stringStart, stringEnd);
        linearLayout.addView(getTextContentView(linearLayout, string));
    }

    TextView getTextContentView(LinearLayout linearLayout, String string)
    {
        TextView textView = new TextView(linearLayout.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,
                (int)linearLayout.getContext().getResources().getDimension(R.dimen.textContentMarginTop), 0, 0);
        textView.setLayoutParams(layoutParams);
        textView.setTextSize(linearLayout.getContext().getResources().getDimension(R.dimen.textContentSize));
        textView.setTextColor(linearLayout.getContext().getResources().getColor(R.color.textContentColor));
        textView.setText(string);
        return textView;
    }

    ImageView getImageContentView(LinearLayout linearLayout, String imgSrc)
    {
        ImageView imageView = new ImageView(linearLayout.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,
                (int)linearLayout.getContext().getResources().getDimension(R.dimen.textContentMarginTop), 0, 0);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setAdjustViewBounds(true);
        Glide
                .with(App.shared())
                .load(imgSrc)
                .placeholder(new ColorDrawable(
                        App.shared().getResources().getColor(R.color.glidePlaceholderColor)))
                .error(R.drawable.ic_broken_image)
                .fallback(R.drawable.ic_broken_image)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageView);
        return imageView;
    }
}
