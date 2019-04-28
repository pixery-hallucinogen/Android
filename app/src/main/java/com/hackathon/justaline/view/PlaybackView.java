// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.hackathon.justaline.view;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.google.firebase.storage.FirebaseStorage;
import com.hackathon.R;
import com.hackathon.justaline.PermissionHelper;
import com.hackathon.justaline.Uploader;
import com.hackathon.justaline.analytics.AnalyticsEvents;
import com.hackathon.justaline.analytics.Fa;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PlaybackView extends ConstraintLayout implements View.OnClickListener,
        TextureView.SurfaceTextureListener, MediaPlayer.OnPreparedListener {

    private static final String TAG = "PlaybackView";

    private static final String FILE_PROVIDER_AUTHORITY
            = "com.hackathon.fileprovider";

    private File mVideoFile;

    private Fa mAnalytics;

    private Uri mUri;

    private MediaPlayer mMediaPlayer;

    private Listener mListener;

    private Surface mSurface;

    private TextureView mVideoTextureView;

    private ImageView mImageView;

    private boolean mIsOpen;

    private boolean mPaused = true;

    public boolean isCapture = false;

    FirebaseStorage storage;

    private AudioAttributes mAudioAttributes;

    public PlaybackView(Context context) {
        super(context);
        init();
    }

    public PlaybackView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlaybackView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        inflate(getContext(), R.layout.view_playback, this);

        setBackgroundColor(Color.BLACK);
        storage = FirebaseStorage.getInstance();

        mAnalytics = Fa.get();

        mVideoTextureView = findViewById(R.id.video);
        mVideoTextureView.setSurfaceTextureListener(this);

        mImageView = findViewById(R.id.image);

        findViewById(R.id.close_button).setOnClickListener(this);
        findViewById(R.id.layout_share).setOnClickListener(this);
        findViewById(R.id.layout_save).setOnClickListener(this);

        // set margin of bottom icons to be appropriate size for screen
        View saveLayout = findViewById(R.id.layout_save);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) saveLayout
                .getLayoutParams();
        layoutParams.bottomMargin = getHeightOfNavigationBar();
        saveLayout.setLayoutParams(layoutParams);

        View shareLayout = findViewById(R.id.layout_share);
        layoutParams = (ConstraintLayout.LayoutParams) shareLayout.getLayoutParams();
        layoutParams.bottomMargin = getHeightOfNavigationBar();
        shareLayout.setLayoutParams(layoutParams);

        mAudioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                .build();

    }

    public void setFilePath(String absoluteFilePath) {

        mVideoFile = new File(absoluteFilePath);
        mUri = FileProvider.getUriForFile(getContext(), FILE_PROVIDER_AUTHORITY, mVideoFile);

        Log.d(TAG, mUri.getPath().substring(mUri.getPath().lastIndexOf('.')).toLowerCase() + ", " + mUri.getPath().substring(mUri.getPath().lastIndexOf('.')).toLowerCase().equals(".png"));

        if (mSurface != null) {
            if (mUri.getPath().substring(mUri.getPath().lastIndexOf('.')).toLowerCase().equals(".png")) {
                prepareImageViewer(mUri);
            } else {
                prepareMediaPlayer(mUri);
            }
        }

    }

    public void resume() {
        mPaused = false;
        if (mIsOpen && mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    public void pause() {
        Log.d(TAG, "pause: media player " + mMediaPlayer);
        mPaused = true;
        if (mMediaPlayer != null && (mIsOpen && mMediaPlayer.isPlaying())) {
            mMediaPlayer.pause();
        }
    }

    public void destroy() {
        // clean up temp video file
        if (mUri != null) {
            File file = new File(mUri.getPath());
            if (file.exists()) {
                file.delete();
            }
            mUri = null;
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
        }
    }

    private void save(File file) {

        if (PermissionHelper.hasStoragePermission(getContext())) {
            String directoryName = Environment.DIRECTORY_PICTURES;
            File picDirectory = Environment.getExternalStoragePublicDirectory(directoryName);

            File jalPicDirectory = new File(picDirectory, "hallucinogen");

            // make our directory if it doesn't exist
            if (!jalPicDirectory.exists()) {
                jalPicDirectory.mkdirs();
            }

            File newFile = new File(jalPicDirectory, file.getName());
            boolean success = copyFile(file, newFile);

            if (success) {
                Toast.makeText(getContext().getApplicationContext(),
                        getContext().getString(R.string.saved, directoryName), Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getContext().getApplicationContext(), R.string.could_not_save,
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Request permissions
            if (mListener != null) {
                mListener.requestStoragePermission();
            }
        }
    }

    private void upload(Boolean isImage) {
        if (isImage) {
            mImageView.setDrawingCacheEnabled(true);
            mImageView.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();

            new Uploader().upload(getContext(), bitmap);
        } else {
            new Uploader().upload(getContext(), mVideoFile);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        PermissionHelper.sendPermissionsAnalytics(mAnalytics, permissions, grantResults);

        if (PermissionHelper.hasStoragePermission(getContext())) {
            save(mVideoFile);
        } else {
            Toast.makeText(getContext().getApplicationContext(), R.string.action_unavailable,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean copyFile(File sourceLocation, File endLocation) {

        boolean success = true;

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(sourceLocation);
            outputStream = new FileOutputStream(endLocation);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to copy file to new location", e);
            success = false;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Failed to close input stream", e);
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Failed to close output stream", e);
            }
        }

        return success;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_button:
                close();
                break;
            case R.id.layout_save:
                save(mVideoFile);
                mAnalytics.send(AnalyticsEvents.EVENT_TAPPED_SAVE);
                break;
            case R.id.layout_share:
                upload(mVideoFile.getPath().substring(mVideoFile.getPath().lastIndexOf('.')).toLowerCase().equals(".png"));
                break;
        }
    }

    public boolean isOpen() {
        return mIsOpen;
    }

    public void open(File file) {

        mIsOpen = true;

        setFilePath(file.getAbsolutePath());

        // animate in
        if (getTranslationX() == 0) {
            setTranslationX(getWidth());
        }

        animate().translationX(0).setListener(null);
        setVisibility(View.VISIBLE);
    }

    public void close() {
        mIsOpen = false;

        animate().translationX(getWidth()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setVisibility(View.GONE);
                destroy();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        if (mListener != null) {
            mListener.onPlaybackClosed();
        }
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    private void prepareMediaPlayer(Uri uri) {
        if (isCapture)
            return;
        Log.d(TAG, "prepareMediaPlayer: " + uri);
        mImageView.setVisibility(View.GONE);
        mVideoTextureView.setVisibility(View.VISIBLE);
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setDataSource(getContext(), uri);
            mMediaPlayer.setSurface(mSurface);
            mMediaPlayer.setAudioAttributes(mAudioAttributes);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepare();
        } catch (IOException | IllegalStateException e) {
            Log.e(TAG, "prepareMediaPlayer: ", e);
        }
    }

    private void prepareImageViewer(Uri uri) {
        Log.d(TAG, "prepareMediaPlayer: " + uri);
        mImageView.setVisibility(View.VISIBLE);
        mVideoTextureView.setVisibility(View.GONE);
        Picasso.get().load(uri).into(mImageView);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        mSurface = new Surface(surfaceTexture);
        if (mUri != null) {
            if (mUri.getPath().substring(mUri.getPath().lastIndexOf('.')).toLowerCase().equals(".png")) {
                prepareImageViewer(mUri);
            } else {
                prepareMediaPlayer(mUri);
            }
        }

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        mSurface = null;
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public int getHeightOfNavigationBar() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (!mPaused) {
            mMediaPlayer.start();
        }
    }

    public interface Listener {

        void onPlaybackClosed();

        void requestStoragePermission();
    }
}
