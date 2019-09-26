package com.amorenew.orange_rfid.alps_rfid_ax6737

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log

import java.util.ArrayList

import cn.pda.serialport.Tools
import kotlin.experimental.and

class SerialPortHelper internal constructor(private val serialListener: SerialListener) {

    private val manager245: Manager245?
    var isRunning = true
    var isStartFlag = false
    var powerLevel = 30

    val tag: String?
        get() = manager245!!.tag

    var listBarcode: MutableList<TagDataMode> = ArrayList()

    private val handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            when (msg.what) {
                1 -> {
                    Log.i("RECV", msg.data.toString())
                    val tag = msg.data.getString("id")
                    val idb = Tools.HexString2Bytes(tag!!)
                    val id = ByteArray(5)
                    for (i in id.indices) {
                        id[i] = idb[4 - i]
                    }
                    val idint = Tools.bytesToInt(id)
                    var stateb = idb[0]
                    stateb = (stateb and 0x01.toByte()).toByte()

                    var stateString = "<Normal power>"
                    if (stateb.toInt() == 1) {
                        stateString = "<Low power>"
                    }
                    var idString = idint.toString() + ""
                    when (idString.length) {
                        7 -> idString = "0$idString"
                        6 -> idString = "00$idString"
                        5 -> idString = "000$idString"
                        4 -> idString = "0000$idString"
                        else -> {
                        }
                    }
                    sortAndadd(listBarcode, idString, stateString)
//                    Util.play(1, 0)
                    serialListener.onDataUpdated(idString)
                }
                //			case 100:
                //				addListView();
                //				break;
                else -> {
                }
            }
        }

    }

    init {
        manager245 = Manager245()//init rfid
        val thread = recivceThread()
        thread.start()
    }


    fun setPowerLevel(powerLevel: Int): Boolean {
        this.powerLevel = powerLevel
        return manager245!!.setPower(this.powerLevel)
    }

    fun stopRead() {
        isStartFlag=false;
        manager245!!.stopRead()

    }

    fun close() {
        isStartFlag=false;

        manager245?.close()
    }

    fun clear() {
        manager245!!.clear()
    }

    fun startRead() {
        if(isStartFlag)
            return;
        isStartFlag=true;
        manager245!!.startRead()
    }


    private fun sortAndadd(list: MutableList<TagDataMode>?, tagId: String, state: String): List<TagDataMode> {
        val goods = TagDataMode()
        goods.barcode = tagId
        goods.state = state
        var temp = 1
        if (list == null || list.size == 0) {
            goods.count = temp
            list!!.add(goods)
            return list
        }

        for (i in list.indices) {
            if (tagId == list[i].barcode) {
                temp = list[i].count + temp
                goods.count = temp
                for (j in i downTo 1) {
                    list[j] = list[j - 1]
                }
                list[0] = goods
                return list
            }
        }
        //
        val lastgoods = list[list.size - 1]
        for (j in list.indices.reversed()) {
            if (j == 0) {
                goods.count = temp
                list[j] = goods
            } else {
                list[j] = list[j - 1]
            }

        }
        list.add(lastgoods)
        return list
    }


    internal inner class recivceThread : Thread() {
        override fun run() {
            super.run()
            while (isRunning) {
                if (isStartFlag) {
                    val idstring = tag
                    if (idstring != null) {
                        val msg = Message()
                        msg.what = 1
                        val data = Bundle()
                        data.putString("id", idstring + "")
                        msg.data = data
                        handler.sendMessage(msg)
                    }
                }
            }
        }
    }

    companion object {

        var MaxPowerLevel = 30
    }
}
