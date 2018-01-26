package com.ng.ganhuoapi.activity;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ng.ganhuoapi.R;
import com.ng.ganhuoapi.base.BaseActivity;
import com.ng.ganhuoapi.constant.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GanWebDetailsActivity extends BaseActivity {

    private Unbinder unbinder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Fade fade = new Fade();
        fade.setDuration(500);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
        setContentView(R.layout.activity_gan_web_details);
        unbinder = ButterKnife.bind(this);
        initUI();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.finishAfterTransition(GanWebDetailsActivity.this);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progress_bar.setVisibility(View.INVISIBLE);
                } else {
                    if (progress_bar.getVisibility() != View.VISIBLE) {
                        progress_bar.setVisibility(View.VISIBLE);
                    }
                    progress_bar.setProgress(newProgress);
                }
            }

        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                toolbar.setTitle(getIntent().getStringExtra(Constant.WEB_VIEW_LOAD_TITLE));
            }
        });
    }

    private void initUI() {
        toolbar.setTitle("跳转中...");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        String webUrl = getIntent().getStringExtra(Constant.WEB_VIEW_LOAD_URL);
        webView.loadUrl(webUrl);

    }
}
