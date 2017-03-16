/*
 * Copyright (c) 2015 [1076559197@qq.com | tchen0707@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liujc.zxinglib.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.liujc.zxinglib.R;
import com.liujc.zxinglib.decode.DecodeThread;

import static com.liujc.zxinglib.activity.CaptureActivity.SCAN_RESULT;

/**
 * 类名称：ResultActivity
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/3/16 12:52
 * 描述：扫描结果返回页
 */
public class ResultActivity extends Activity {
    ImageView resultImage;
    TextView resultType;
    TextView resultContent;
    private Button back_btn;

    private Bitmap mBitmap;
    private String mResultStr;
    private String mDecodeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initViews();
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            getBundleExtras(extras);
        }
        initViewsAndEvents();
    }

    private void initViews() {
        resultImage = (ImageView) findViewById(R.id.result_image);
        resultType = (TextView) findViewById(R.id.result_type);
        resultContent = (TextView) findViewById(R.id.result_content);
        back_btn = (Button) findViewById(R.id.back_btn);
    }

    protected void getBundleExtras(Bundle extras) {
        if (extras != null) {
            byte[] compressedBitmap = extras.getByteArray(DecodeThread.BARCODE_BITMAP);
            if (compressedBitmap != null) {
                mBitmap = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
                mBitmap = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
            }

            mResultStr = extras.getString(SCAN_RESULT);
            mDecodeTime = extras.getString(DecodeThread.DECODE_TIME);
        }
    }

    protected void initViewsAndEvents() {

        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(mDecodeTime)) {
            sb.append("\n扫描时间:\t\t");
            sb.append(mDecodeTime);
        }
        sb.append("\n\n扫描结果:");

        resultType.setText(sb.toString());
        resultContent.setText(mResultStr);

        if (null != mBitmap) {
            resultImage.setImageBitmap(mBitmap);
        }
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mBitmap && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

}
