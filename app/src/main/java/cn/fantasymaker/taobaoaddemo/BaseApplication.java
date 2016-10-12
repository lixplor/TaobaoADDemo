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
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

/**
 * Created :  2016-10-12
 * Author  :  Fantasymaker
 * Web     :  http://blog.fantasymaker.cn
 * Email   :  me@fantasymaker.cn
 */
public class BaseApplication extends Application {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_BACK_TO_FRONT = 1;
    public static final int STATE_FRONT_TO_BACK = 2;
    public static int sAppState = STATE_NORMAL;

    @Override
    public void onCreate() {
        super.onCreate();

        // 注册ActivityLifecycleCallbacks
        registerActivityLifecycleCallbacks(new AppLifeCycleCallback());

    }

    /**
     * 自定义ActivityLifecycleCallbacks, 实现前后台状态统计
     */
    private class AppLifeCycleCallback implements ActivityLifecycleCallbacks {

        private int mVisibleActivityCount = 0;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
            // 增加计数
            mVisibleActivityCount++;
            Log.d("aa", "count=" + mVisibleActivityCount);
            // 1限制了从0到1的改变, 后续再开启其他Activity都不会进行状态切换
            if (mVisibleActivityCount == 1) {
                // 从后台进入前台
                sAppState = STATE_BACK_TO_FRONT;
            } else {
                // 否则是正常状态
                sAppState = STATE_NORMAL;
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
            // 减少计数
            mVisibleActivityCount--;
            // 0限制了从有到无的变化
            if (mVisibleActivityCount == 0) {
                // 从前台进入后台
                sAppState = STATE_FRONT_TO_BACK;
            } else {
                // 否则是正常状态
                sAppState = STATE_NORMAL;
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    }
}
