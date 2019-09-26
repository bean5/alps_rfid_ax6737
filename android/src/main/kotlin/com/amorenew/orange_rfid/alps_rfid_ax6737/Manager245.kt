package com.amorenew.orange_rfid.alps_rfid_ax6737

import android.util.Log

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

import cn.pda.serialport.SerialPort
import cn.pda.serialport.Tools


class Manager245 {
    internal var serialPort: SerialPort
    internal lateinit var inputStream: InputStream
    internal lateinit var outputStream: OutputStream
    internal var stopcmd = "FEF1A300000000000000A3FF"//
    internal var startcmd = "FEF1A100000000000000A1FF"//
    internal var setcmd = "FEF1000000000030000030FF"//
    //					Log.e("id:", Tools.Bytes2HexString(bs, bs.length));
    //						Log.e("id:", Tools.Bytes2HexString(idbytes, 10));
    //						Log.e("id5:", idstring);
    //						int idint = Tools.bytesToInt(id);
    //						byte stateb = idb[0];
    // TODO Auto-generated catch block
    val tag: String?
        get() {
            try {
                val count = inputStream.available()
                if (count >= 15) {
                    val head = ByteArray(1)
                    inputStream.read(head)
                    if (head[0] == 0x02.toByte()) {
                        val bs = ByteArray(16)
                        val len = inputStream.read(bs)
                        if (bs[14] == 0x0D.toByte() && bs[15] == 0x0A.toByte()) {
                            val idbytes = ByteArray(10)
                            System.arraycopy(bs, 2, idbytes, 0, 10)
                            val idstring = String(idbytes, Charsets.US_ASCII)

                            val idb = Tools.HexString2Bytes(idstring)
                            val id = ByteArray(5)
                            for (i in id.indices) {
                                id[i] = idb[4 - i]
                            }
                            return idstring
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }

    init {
        serialPort = SerialPort()
        when (Power) {
            SerialPort.Power_Scaner -> serialPort.scaner_poweron()
            SerialPort.Power_3v3 -> serialPort.power3v3on()
            SerialPort.Power_5v -> serialPort.power_5Von()
            SerialPort.Power_Psam -> serialPort.psam_poweron()
            SerialPort.Power_Rfid -> serialPort.rfid_poweron()
        }
        if (Port == 0) serialPort.scaner_poweron()
        try {
            serialPort = SerialPort(Port, 115200, 0)
            inputStream = serialPort.inputStream
            outputStream = serialPort.outputStream

        } catch (e: SecurityException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

    }

    fun stopRead() {

        try {
            outputStream.write(Tools.HexString2Bytes(stopcmd))
            inputStream.read(ByteArray(4096))
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

    }

    fun setPower(set: Int): Boolean {
        try {
            inputStream.read(ByteArray(4096))
            //			Thread.sleep(10);
            val setcmdbytes = Tools.HexString2Bytes(setcmd)
            setcmdbytes[7] = (set + 32).toByte()
            setcmdbytes[10] = (set + 32).toByte()
            inputStream.read(ByteArray(4096))
            outputStream.write(setcmdbytes)

            Thread.sleep(50)
            val bs = ByteArray(1024)
            val len = inputStream.read(bs)
            Log.e("set : return data", Tools.Bytes2HexString(setcmdbytes, setcmdbytes.size) + ":" + Tools.Bytes2HexString(bs, len))
            return if (bs[0] == 0xfe.toByte() && bs[2] == 0x00.toByte()) {
                true
            } else {
                false
            }
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: InterruptedException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return true
    }

    fun startRead() {
        try {
            outputStream.write(Tools.HexString2Bytes(startcmd))
            //			Log.e("start", startcmd);
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

    }

    fun close() {
        when (Power) {
            SerialPort.Power_Scaner -> serialPort.scaner_poweroff()
            SerialPort.Power_3v3 -> serialPort.power_3v3off()
            SerialPort.Power_5v -> serialPort.power_5Voff()
            SerialPort.Power_Psam -> serialPort.psam_poweroff()
            SerialPort.Power_Rfid -> serialPort.rfid_poweroff()
        }
        if (Port == 0) serialPort.scaner_poweroff()
        serialPort.close(Port)

    }

    fun clear() {
        try {
            inputStream.read(ByteArray(4096))
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    companion object {
        var Port = 12
        var Power = SerialPort.Power_3v3
    }


}
