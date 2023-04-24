package com.raywenderlich.funtime.device.player

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.raywenderlich.funtime.R

class MediaPlayerImpl : MediaPlayer {
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var context: Context

    override fun getPlayerImpl(context: Context): ExoPlayer {
        this.context = context
        initializePlayer()
        return exoPlayer
    }

    private fun initializePlayer() {
        val trackSelector = DefaultTrackSelector()
        val loadControl = DefaultLoadControl()
        val renderersFactory = DefaultRenderersFactory(context)

        exoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl)
    }

    override fun play(url: String) {
        // 1
        val userAgent = Util.getUserAgent(context, context.getString(R.string.app_name))
        // 2
        val mediaSource = ExtractorMediaSource.Factory(DefaultDataSourceFactory(context, userAgent))
            .setExtractorsFactory(DefaultExtractorsFactory())
            .createMediaSource(Uri.parse(url))
        // 3
        exoPlayer.prepare(mediaSource)
        // 4
        exoPlayer.playWhenReady = true
    }

    override fun releasePlayer() {
        exoPlayer.stop()
        exoPlayer.release()
    }
}