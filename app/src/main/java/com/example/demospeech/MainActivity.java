package com.example.demospeech;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.provider.SyncStateContract;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codesgood.views.JustifiedTextView;
import com.example.demospeech.googlecloudtts.GoogleCloudTTS;

import org.sufficientlysecure.htmltextview.HtmlFormatter;
import org.sufficientlysecure.htmltextview.HtmlFormatterBuilder;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import static android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD;


public class MainActivity extends AppCompatActivity implements MainContract.IView {
    private static final int TEXT_TO_SPEECH_CODE = 0x100;

    private MainContract.IPresenter mPresenter;
    private TextView mEditText;
    private Button mButtonSpeak;
    private Button mButtonSpeakPause;
    private Button mButtonSpeakStop;
    private String languageCode, voiceName;
    public ProgressDialog progressDialog;

    private BroadcastReceiver hideLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getBundle();
        buildViews();
        mPresenter = new MainPresenter(MainActivity.this, languageCode, voiceName);
        mPresenter.onCreate();
        initAndroidTTS();

        // nhận broadcastReceiver từ GoogleCloudTTS để tắt loading
        hideLoading = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                progressDialog.dismiss();
            }
        };
        registerReceiver(hideLoading, new IntentFilter("hideLoading"));
    }

    private void getBundle() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        languageCode = bundle.getString("languageCode");
        voiceName = bundle.getString("voiceName");

    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
        if (hideLoading != null) {
            unregisterReceiver(hideLoading);
        }
    }

    @SuppressLint("WrongConstant")
    private void buildViews() {


        mEditText = findViewById(R.id.ettIdText);
        mButtonSpeak = findViewById(R.id.btnIdSpeak);
        mButtonSpeakPause = findViewById(R.id.btnIdPauseAndResume);
        mButtonSpeakStop = findViewById(R.id.btnIdStop);

        Spanned spanned = HtmlFormatter.formatHtml(new HtmlFormatterBuilder().setHtml("<body style='text-align:justify;color:gray;'><p><b>Ngũ Hành Sơn</b> được hình thành bởi quần thể năm ngọn núi <b>Kim - Mộc - Thuỷ - Hoả - Thổ</b> được “bao bọc” bởi rất nhiều huyền thoại khác nhau. Đây là một tuyệt tác về cảnh quan thiên nhiên “sơn kỳ thủy tú”, huyền ảo thơ mộng mà tạo hóa đã ban tặng cho Đà Nẵng. Nơi đây, các dấu ấn văn hoá - lịch sử còn in đậm trên mỗi công trình chùa, tháp đầu thế kỷ XIX, trên mỗi tác phẩm điêu khắc Chăm của thế kỷ XIV, XV. </p> <p> Những bút tích thi ca thời Lê, Trần còn in dấu trên các vách đá rêu phong trong các hang động. Những di tích văn hoá - lịch sử như mộ mẹ tướng quân Trần Quang Diệu, đền thờ công chúa Ngọc Lan (em gái vua Minh Mạng), bút tích sắc phong quốc tự còn lưu giữ tại chùa Tam Thai của triều Nguyễn, đến các di tích lịch sử đấu tranh cách mạng như: Địa đạo núi đá Chồng, hang Bà Tho, núi Kim Sơn, hang Âm Phủ,… Tất cả chứng minh hùng hồn về một Ngũ Hành Sơn huyền thoại, về một vùng đất địa linh nhân kiệt đầy chất sử thi.</p><p> Chùa Tam Thai tọa lạc trên ngọn Thủy Sơn - một trong năm ngọn Ngũ Hành Sơn  thuộc quần thể danh thắng Non Nước - Ngũ Hành Sơn, thành phố Đà Nẵng. Là một ngôi chùa cổ (xây dựng năm 1930), được xem là quốc tự và di tích Phật giáo. Năm 1825, Minh Mạng trong chuyến tuần du Ngũ Hành Sơn đã cho xây dựng lại chùa, năm 1827 cho đúc 9 tượng và 3 chuông lớn. Thời vua Minh Mạng có một công chúa (con vua Gia Long) đến xin xuất gia. Tương truyền vua đã thiết lập du cung ở đây để nghỉ ngơi và tham quan thắng cảnh Ngũ Hành Sơn.</p> <p> Chánh điện thờ Phật Di Lặc bằng đồng lớn ngồi trên tòa sen, hai bên thờ tượng Quan Thánh và Bồ Tát. Chùa là nơi từng được quốc sư Hưng Liên trụ trì và đã truyền từ lúc khai sơn đến nay được 18 đời. Chùa cũng là nơi có nhiều khách hàng hương thăm viếng, cầu Phật, đặc biệt là vào dịp lễ, Tết.  Hệ thống hang động trong quần thể Ngũ Hành Sơn là cả một thế giới kỳ bí.</p> <p> Với sự kiến tạo độc đáo của thiên nhiên, động Âm Phủ được xem là một trong những hang động lớn và huyền bí nhất trong quần thể hang động ở khu danh thắng Ngũ Hành Sơn. Động Âm Phủ có tên từ thời vua Minh Mạng (đầu thế kỷ 19), khi nhà vua vi hành đến ngọn núi này. Theo thuyết âm dương, trong đời sống con người và vạn vật luôn tồn tại hai mặt đối lập: có ngày phải có đêm, có sinh phải có tử. Vì thế, trên ngọn Thủy Sơn có đường lên trời thì dưới chân có động xuống Âm Phủ. Trong động Âm Phủ có nhiều truyền thuyết vừa thực, vừa ảo. Thực ở đây là con người ai cũng có một lần sinh và một lần tử, còn ảo ở đây là sự phân xử của tạo hóa về cái thiện và cái ác của kiếp con người. Bởi thế, trong động Âm Phủ được chia làm hai ngách, đó là ngách lên trời và ngách xuống âm phủ. Âm phủ là thế giới của người chết. Theo giáo lý của đạo Phật, chết không phải là hết mà là sự chuyển tiếp để đầu thai về cảnh giới khác. Người tích thiện nhiều sẽ được siêu thoát, kẻ gây nên tội ác sẽ bị đọa đày. Thiện và ác đến đây sẽ được phân minh. Theo định luật âm ty, con người trước khi chết, các linh hồn phải qua chiếc cầu Âm Dương trên sông Nại Hà định mệnh.</p></body>"));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mEditText.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);

        }
        // dùng khi sử dụng textview để hiển thị
        mEditText.setText(spanned);

        // dùng khi sử dụng org.sufficientlysecure.htmltextview.HtmlTextView để hiển thị
        //mEditText.setHtml("<p><b>Ngũ Hành Sơn</b> được hình thành bởi quần thể năm ngọn núi <b>Kim - Mộc - Thuỷ - Hoả - Thổ</b> được “bao bọc” bởi rất nhiều huyền thoại khác nhau. Đây là một tuyệt tác về cảnh quan thiên nhiên “sơn kỳ thủy tú”, huyền ảo thơ mộng mà tạo hóa đã ban tặng cho Đà Nẵng. Nơi đây, các dấu ấn văn hoá - lịch sử còn in đậm trên mỗi công trình chùa, tháp đầu thế kỷ XIX, trên mỗi tác phẩm điêu khắc Chăm của thế kỷ XIV, XV. </p> <p> Những bút tích thi ca thời Lê, Trần còn in dấu trên các vách đá rêu phong trong các hang động. Những di tích văn hoá - lịch sử như mộ mẹ tướng quân Trần Quang Diệu, đền thờ công chúa Ngọc Lan (em gái vua Minh Mạng), bút tích sắc phong quốc tự còn lưu giữ tại chùa Tam Thai của triều Nguyễn, đến các di tích lịch sử đấu tranh cách mạng như: Địa đạo núi đá Chồng, hang Bà Tho, núi Kim Sơn, hang Âm Phủ,… Tất cả chứng minh hùng hồn về một Ngũ Hành Sơn huyền thoại, về một vùng đất địa linh nhân kiệt đầy chất sử thi.</p><p> Chùa Tam Thai tọa lạc trên ngọn Thủy Sơn - một trong năm ngọn Ngũ Hành Sơn  thuộc quần thể danh thắng Non Nước - Ngũ Hành Sơn, thành phố Đà Nẵng. Là một ngôi chùa cổ (xây dựng năm 1930), được xem là quốc tự và di tích Phật giáo. Năm 1825, Minh Mạng trong chuyến tuần du Ngũ Hành Sơn đã cho xây dựng lại chùa, năm 1827 cho đúc 9 tượng và 3 chuông lớn. Thời vua Minh Mạng có một công chúa (con vua Gia Long) đến xin xuất gia. Tương truyền vua đã thiết lập du cung ở đây để nghỉ ngơi và tham quan thắng cảnh Ngũ Hành Sơn.</p> <p> Chánh điện thờ Phật Di Lặc bằng đồng lớn ngồi trên tòa sen, hai bên thờ tượng Quan Thánh và Bồ Tát. Chùa là nơi từng được quốc sư Hưng Liên trụ trì và đã truyền từ lúc khai sơn đến nay được 18 đời. Chùa cũng là nơi có nhiều khách hàng hương thăm viếng, cầu Phật, đặc biệt là vào dịp lễ, Tết.  Hệ thống hang động trong quần thể Ngũ Hành Sơn là cả một thế giới kỳ bí.</p> <p> Với sự kiến tạo độc đáo của thiên nhiên, động Âm Phủ được xem là một trong những hang động lớn và huyền bí nhất trong quần thể hang động ở khu danh thắng Ngũ Hành Sơn. Động Âm Phủ có tên từ thời vua Minh Mạng (đầu thế kỷ 19), khi nhà vua vi hành đến ngọn núi này. Theo thuyết âm dương, trong đời sống con người và vạn vật luôn tồn tại hai mặt đối lập: có ngày phải có đêm, có sinh phải có tử. Vì thế, trên ngọn Thủy Sơn có đường lên trời thì dưới chân có động xuống Âm Phủ. Trong động Âm Phủ có nhiều truyền thuyết vừa thực, vừa ảo. Thực ở đây là con người ai cũng có một lần sinh và một lần tử, còn ảo ở đây là sự phân xử của tạo hóa về cái thiện và cái ác của kiếp con người. Bởi thế, trong động Âm Phủ được chia làm hai ngách, đó là ngách lên trời và ngách xuống âm phủ. Âm phủ là thế giới của người chết. Theo giáo lý của đạo Phật, chết không phải là hết mà là sự chuyển tiếp để đầu thai về cảnh giới khác. Người tích thiện nhiều sẽ được siêu thoát, kẻ gây nên tội ác sẽ bị đọa đày. Thiện và ác đến đây sẽ được phân minh. Theo định luật âm ty, con người trước khi chết, các linh hồn phải qua chiếc cầu Âm Dương trên sông Nại Hà định mệnh.</p>");

        mButtonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.startSpeak(mEditText.getText().toString());
                progressDialog = new ProgressDialog(getContext());
                progressDialog.show();
            }
        });


        mButtonSpeakPause.setOnClickListener((view) -> {
            if (mButtonSpeakPause.getText().toString().compareTo(getResources().getString(R.string.btnPtSpeechPause)) == 0) {
                mPresenter.pauseSpeak();
                mButtonSpeakPause.setText(getResources().getString(R.string.btnPtSpeechResume));
            } else {
                mPresenter.resumeSpeak();
                mButtonSpeakPause.setText(getResources().getString(R.string.btnPtSpeechPause));
            }
        });

        mButtonSpeakStop.setOnClickListener((view) -> mPresenter.stopSpeak());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // init android tts
        if (requestCode == TEXT_TO_SPEECH_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                mPresenter.initAndroidTTS();
                return;
            }
            Intent installIntent = new Intent();
            installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            startActivity(installIntent);
        }
    }


    @Override
    public int getProgressPitch() {
        return 1500;
    }

    @Override
    public int getProgressSpeakRate() {
        return 75;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.pauseSpeak();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mPresenter.resumeSpeak();
    }

    @Override
    public void invoke(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    @Override
    public void setPresenter(MainContract.IPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void initAndroidTTS() {
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, TEXT_TO_SPEECH_CODE);
    }


}

