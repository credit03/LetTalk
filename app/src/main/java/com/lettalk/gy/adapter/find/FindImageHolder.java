package com.lettalk.gy.adapter.find;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.lettalk.gy.R;
import com.lettalk.gy.adapter.BaseViewHolder;
import com.lettalk.gy.adapter.OnRecyclerViewListener;
import com.lettalk.gy.ui.view.animationui.BlurLayout;

import butterknife.Bind;

/**
 * Created by Administrator on 2016-07-02.
 */
public class FindImageHolder extends BaseViewHolder {


    @Bind(R.id.blur_layout)
    BlurLayout blurLayout;
    @Bind(R.id.blur_layout2)
    BlurLayout blurLayout2;
    @Bind(R.id.blur_layout3)
    BlurLayout blurLayout3;
    @Bind(R.id.blur_layout4)
    BlurLayout blurLayout4;

    public FindImageHolder(Context context, ViewGroup root, OnRecyclerViewListener listener) {
        super(context, root, R.layout.item_find_imageholder, listener);

    }


    @Override
    public void bindData(Object o) {
        Log.i(TAG, "bindData: FindImageHolder");

        View hover = LayoutInflater.from(context).inflate(R.layout.hover_sample, null);
        hover.findViewById(R.id.heart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Tada)
                        .duration(550)
                        .playOn(v);
                Toast.makeText(context, " R.id.heart:", Toast.LENGTH_SHORT).show();
            }
        });
        hover.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Swing)
                        .duration(550)
                        .playOn(v);
                Toast.makeText(context, " R.id.share:", Toast.LENGTH_SHORT).show();
            }
        });
        blurLayout.setHoverView(hover);
        blurLayout.setBlurDuration(550);
        blurLayout.addChildAppearAnimator(hover, R.id.heart, Techniques.FlipInX, 550, 0);
        blurLayout.addChildAppearAnimator(hover, R.id.share, Techniques.FlipInX, 550, 250);
        blurLayout.addChildAppearAnimator(hover, R.id.more, Techniques.FlipInX, 550, 500);

        blurLayout.addChildDisappearAnimator(hover, R.id.heart, Techniques.FlipOutX, 550, 500);
        blurLayout.addChildDisappearAnimator(hover, R.id.share, Techniques.FlipOutX, 550, 250);
        blurLayout.addChildDisappearAnimator(hover, R.id.more, Techniques.FlipOutX, 550, 0);

        blurLayout.addChildAppearAnimator(hover, R.id.description, Techniques.FadeInUp);
        blurLayout.addChildDisappearAnimator(hover, R.id.description, Techniques.FadeOutDown);


        //sample 2

        View hover2 = LayoutInflater.from(context).inflate(R.layout.hover_sample2, null);
        hover2.findViewById(R.id.avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Pretty Cool, Right?", Toast.LENGTH_SHORT).show();
            }
        });
        blurLayout2.setHoverView(hover2);

        blurLayout2.addChildAppearAnimator(hover2, R.id.description, Techniques.FadeInUp);
        blurLayout2.addChildDisappearAnimator(hover2, R.id.description, Techniques.FadeOutDown);
        blurLayout2.addChildAppearAnimator(hover2, R.id.avatar, Techniques.DropOut, 1200);
        blurLayout2.addChildDisappearAnimator(hover2, R.id.avatar, Techniques.FadeOutUp);
        blurLayout2.setBlurDuration(1000);

        //sample3
        View hover3 = LayoutInflater.from(context).inflate(R.layout.hover_sample3, null);
        blurLayout3.setHoverView(hover3);
        blurLayout3.addChildAppearAnimator(hover3, R.id.eye, Techniques.Landing);
        blurLayout3.addChildDisappearAnimator(hover3, R.id.eye, Techniques.TakingOff);
        blurLayout3.enableZoomBackground(true);
        blurLayout3.setBlurDuration(1200);

        //sample 4

        View hover4 = LayoutInflater.from(context).inflate(R.layout.hover_sample4, null);
        blurLayout4.setHoverView(hover4);
        blurLayout4.addChildAppearAnimator(hover4, R.id.cat, Techniques.SlideInLeft);
        blurLayout4.addChildAppearAnimator(hover4, R.id.mail, Techniques.SlideInRight);

        blurLayout4.addChildDisappearAnimator(hover4, R.id.cat, Techniques.SlideOutLeft);
        blurLayout4.addChildDisappearAnimator(hover4, R.id.mail, Techniques.SlideOutRight);

        blurLayout4.addChildAppearAnimator(hover4, R.id.content, Techniques.BounceIn);
        blurLayout4.addChildDisappearAnimator(hover4, R.id.content, Techniques.FadeOutUp);


        hover4.findViewById(R.id.cat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getWebPage = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/daimajia"));
                // startActivity(getWebPage);
            }
        });

        hover4.findViewById(R.id.mail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"daimajia@gmail.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "About AndroidViewHover");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I have a good idea about this project..");

                // startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });
    }
}
