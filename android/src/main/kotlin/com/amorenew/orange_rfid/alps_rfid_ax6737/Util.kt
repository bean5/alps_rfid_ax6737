package com.amorenew.orange_rfid.alps_rfid_ax6737

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool

import java.util.HashMap

object Util {


    lateinit var sp: SoundPool
    lateinit var suondMap: MutableMap<Int, Int>
    lateinit var context: Context


    fun initSoundPool(context: Context) {
        Util.context = context
        sp = SoundPool(1, AudioManager.STREAM_MUSIC, 1)
        suondMap = HashMap()
        suondMap[1] = sp.load(context, R.raw.msg, 1)
    }

    fun play(sound: Int, number: Int) {
        return;
        val am = Util.context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val audioMaxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()

        val audioCurrentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
        val volumnRatio = audioCurrentVolume / audioMaxVolume
        sp.play(
                suondMap[sound]!!, //
                audioCurrentVolume, //
                audioCurrentVolume, //
                1, //
                number, //
                1f)
    }

}
