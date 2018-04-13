package io.github.ylbfdev.slideshowview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * <h2>SlideShowView</h2>
 * <p>ViewPager实现的轮播图广告自定义视图；</p>
 * <p>既支持自动轮播页面也支持手势滑动切换页面；</p>
 * <p>动态设置图片的张数；</p>
 * <p>项目中依赖了universalimageloader 1.9.5来处理图片显示；</p>
 *
 * @author YLBF
 * @version 版本号 1.0.0.0
 */
@SuppressLint("HandlerLeak")
public class SlideShowView extends FrameLayout {

    private Drawable drawable;
    private Context context;
    private DisplayImageOptions options;

    private ImageLoader imageLoader;

    /**
     * 自动轮播的时间间隔
     */
    private static int TIME_INTERVAL = 4;

    private boolean isAutoPlay = true;
    /**
     * 自定义轮播图的资源链接
     */
    private List<String> imageUris;
    /**
     * 放轮播图片的ImageView
     */
    private List<ImageView> imageViewsList;
    /**
     * 焦点指示
     */
    private List<ImageView> dotViewsList;
    private LinearLayout mLinearLayout;

    private ViewPager mViewPager;
    private int currentItem = 0;
    private ScheduledExecutorService scheduledExecutorService;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mViewPager.setCurrentItem(currentItem);
        }

    };

    public SlideShowView(Context context) {
        this(context, null);
    }

    public SlideShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("NewApi")
	public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;

        if (isInEditMode())
            return;
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.slide_attrs);
        drawable = typedArray.getDrawable(R.styleable.slide_attrs_slide_onloading);
        initUI(context);
        typedArray.recycle();
    }

    private void initUI(Context context) {
        imageViewsList = new ArrayList<>();
        dotViewsList = new ArrayList<>();
        imageUris = new ArrayList<>();
        LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this,
                true);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    private void initViewPager() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 0, 0, 0);
        for (int i = 0; i < imageUris.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);// 铺满屏幕
            imageLoader.displayImage(imageUris.get(i), imageView, options);
            imageViewsList.add(imageView);
            ImageView viewDot = new ImageView(getContext());
            viewDot.setBackgroundResource(i == 0 ? R.drawable.main_dot_white : R.drawable.main_dot_light);
            viewDot.setLayoutParams(lp);
            dotViewsList.add(viewDot);
            mLinearLayout.addView(viewDot);
        }
        mViewPager.setFocusable(true);
        mViewPager.setAdapter(new MyPagerAdapter());
        mViewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    /**
     * 设置网络图片url到集合
     *
     * @param imageuris 网络图片url
     */
    public SlideShowView setImageUris(List<String> imageuris) {
        imageUris.addAll(imageuris);
        return this;
    }

    /**
     * 默认自动播放
     *
     * @return 是否自动播放轮播图
     */
    private boolean isAutoPlay() {
        return isAutoPlay;
    }

    /**
     * 设置自动播放
     *
     * @param autoPlay
     */
    public SlideShowView setAutoPlay(boolean autoPlay) {
        isAutoPlay = autoPlay;
        return this;
    }

    /**
     * 开始播放 默认 4秒 切换
     */
    public void startPlay() {
        if (imageLoader == null)
            imageLoader = initImageLoader(context);
        if (options == null)
            options = initImageOptions(drawable);

        if (imageUris == null && imageUris.size() == 0)
            return;
        initViewPager();

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, TIME_INTERVAL,
                TimeUnit.SECONDS);
    }


    /**
     * 开始轮播图切换
     *
     * @param perTime 时间秒数 默认 4秒
     */
    public void startPlay(int perTime) {
        TIME_INTERVAL = perTime;
        startPlay();
    }

    /**
     * 停止播放
     */
    public void stopPlay() {
        if (scheduledExecutorService != null)
            scheduledExecutorService.shutdown();
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    /**
     * 设置自己的UIL
     *
     * @param imageLoader
     */
    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public DisplayImageOptions getOptions() {
        return options;
    }

    /**
     * 设置UIL的 DisplayImageOptions属性
     *
     * @param options
     */
    public void setOptions(DisplayImageOptions options) {
        this.options = options;
    }

    /**
     * 设置选中的tip的背景
     *
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < dotViewsList.size(); i++) {
            if (i == selectItems) {
                dotViewsList.get(i).setBackgroundResource(
                        R.drawable.main_dot_white);
            } else {
                dotViewsList.get(i).setBackgroundResource(
                        R.drawable.main_dot_light);
            }
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewsList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViewsList.get(position));
            return imageViewsList.get(position);
        }

        @Override
        public int getCount() {
            return imageViewsList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(ViewGroup arg0) {

        }

        @Override
        public void finishUpdate(ViewGroup arg0) {

        }

    }

    private class MyPageChangeListener implements OnPageChangeListener {

        boolean isPause = false;

        @Override
        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case 1:
                    isPause = false;
                    break;
                case 2:
                    isPause = true;
                    break;
                case 0:
                    if (mViewPager.getCurrentItem() == mViewPager.getAdapter()
                            .getCount() - 1 && !isAutoPlay() && !isPause) {
                        mViewPager.setCurrentItem(0);
                    } else if (mViewPager.getCurrentItem() == 0 && !isAutoPlay() && !isPause) {
                        mViewPager.setCurrentItem(mViewPager.getAdapter()
                                .getCount() - 1);
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int pos) {
            setImageBackground(pos % imageUris.size());

            // currentItem = pos;
            // for(int i=0;i < dotViewsList.size();i++){
            // if(i == pos){
            // ((ImageView)dotViewsList.get(pos)).setBackgroundResource(R.drawable.dot_black);//R.drawable.main_dot_light
            // }else {
            // ((ImageView)dotViewsList.get(i)).setBackgroundResource(R.drawable.dot_white);
            // }
            // }
        }

    }

    private class SlideShowTask implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (mViewPager) {
                currentItem = (currentItem + 1) % imageViewsList.size();
                handler.obtainMessage().sendToTarget();
            }
        }

    }

    private void destoryBitmaps() {

        for (int i = 0; i < imageViewsList.size(); i++) {
            ImageView imageView = imageViewsList.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                drawable.setCallback(null);
            }
        }
    }

    @SuppressLint("NewApi")
	public DisplayImageOptions initImageOptions(Drawable loadingAndFail) {
        if (drawable == null) {
            if (Build.VERSION.SDK_INT>= 21) {
                drawable = getResources().getDrawable(R.drawable.loading_slideshowview);
            } else {
                drawable = getResources().getDrawable(R.drawable.loading_slideshowview);
            }
        }
        //显示图片的配置
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(loadingAndFail)
                .showImageOnFail(loadingAndFail)
                .build();
        return options;
    }

    /**
     * ImageLoader 图片组件初始化
     *
     * @param context
     */
    public ImageLoader initImageLoader(Context context) {
        File cacheDir = StorageUtils.getIndividualCacheDirectory(context, "imageloader");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)                
                .tasksProcessingOrder(QueueProcessingType.LIFO)               
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        return imageLoader;
    }

}
