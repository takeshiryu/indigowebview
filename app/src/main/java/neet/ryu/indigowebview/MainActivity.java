package neet.ryu.indigowebview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Context context;

    WebView indigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        indigo = (WebView) findViewById(R.id.indigo);
        indigo.clearCache(true);
//        indigo.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                //Required functionality here
//                return super.onJsAlert(view, url, message, result);
//            }
//
//        });
        indigo.getSettings().setJavaScriptEnabled(true);
        indigo.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        indigo.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedHttpAuthRequest(WebView view,
                                                  final HttpAuthHandler handler, String host, String realm) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                LayoutInflater inflater = LayoutInflater.from(context);
                final View root = inflater.inflate(R.layout.dialog_auth, (ViewGroup) findViewById(android.R.id.content), false);

                builder.setView(root).setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String username = ((EditText) root.findViewById(R.id.username)).getText().toString();
                        String password = ((EditText) root.findViewById(R.id.password)).getText().toString();
                        dialogInterface.dismiss();
                        handler.proceed(username, password);
                    }
                });
                Dialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (errorCode == ERROR_AUTHENTICATION) {
                    indigo.clearCache(true);
                    indigo.reload();
                }
            }

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        indigo.loadUrl("http://indigo.ntex.vn");
    }

    @Override
    public void onBackPressed() {
        indigo.loadUrl("http://indigo.ntex.vn");
    }
}
