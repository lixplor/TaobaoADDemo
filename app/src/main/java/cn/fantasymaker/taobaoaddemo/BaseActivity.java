/*
 *     Copyright © 2016 Fantasymaker
 *
 *     Permission is hereby granted, free of charge, to any person obtaining a copy
 *     of this software and associated documentation files (the "Software"), to deal
 *     in the Software without restriction, including without limitation the rights
 *     to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *     copies of the Software, and to permit persons to whom the Software is
 *     furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in all
 *     copies or substantial portions of the Software.
 *
 *     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *     IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *     FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *     AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *     LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *     OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *     SOFTWARE.
 */

package cn.fantasymaker.taobaoaddemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

/**
 * Created :  2016-10-12
 * Author  :  Fantasymaker
 * Web     :  http://blog.fantasymaker.cn
 * Email   :  me@fantasymaker.cn
 */
public class BaseActivity extends Activity {

    // 广告显示时间
    private long mADDuration = 1000 * 4;
    // 广告显示间隔时间, 间隔内不出现广告
    private long mADInterval = 1000 * 10;
    // 上次显示广告的时间
    private long mLastADTime = 0;

    private View mAdView;
    private ImageView mIvAd;
    private TextView mTvTimer;
    private CountDownTimer mTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdView = View.inflate(this, R.layout.view_ad, null);
        mIvAd = (ImageView) mAdView.findViewById(R.id.iv_ad);
        mTvTimer = (TextView) mAdView.findViewById(R.id.tv_timer);
        mTimer = new CountDownTimer(mADDuration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("aa", millisUntilFinished + "");
                String text = (millisUntilFinished / 1000) + "s";
                mTvTimer.setText(text);
            }

            @Override
            public void onFinish() {
                if (mAdView != null) {
                    getWindowManager().removeViewImmediate(mAdView);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 判断是否从后台恢复, 且时间间隔符合要求, 显示广告页面
        boolean isFromBackToFront = BaseApplication.sAppState == BaseApplication.STATE_BACK_TO_FRONT;
        Log.d("aa", "b2f=" + BaseApplication.sAppState);
        if (isFromBackToFront && canShowAD()) {
            showAD();
        }
    }

    /**
     * 显示广告
     */
    private void showAD() {
        // 显示广告页面
        createADView();
        // 随机显示一个广告页
        showRandomAD();
        // 开始倒计时
        mTimer.cancel();
        mTimer.start();
        // 记录显示广告的时间, 以便后续比对
        mLastADTime = System.currentTimeMillis();
    }

    /**
     * 判断两次时间是否大于规定的间隔
     *
     * @return true大于间隔, 否则false
     */
    private boolean canShowAD() {
        return System.currentTimeMillis() - mLastADTime > mADInterval;
    }

    private void createADView() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.x = 0;
        params.y = 0;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindowManager().addView(mAdView, params);
    }

    private void showRandomAD() {
        switch (new Random().nextInt(3)) {
            case 0:
                mIvAd.setImageResource(R.mipmap.ad1);
                break;
            case 1:
                mIvAd.setImageResource(R.mipmap.ad2);
                break;
            case 2:
                mIvAd.setImageResource(R.mipmap.ad3);
                break;
        }
    }
}
