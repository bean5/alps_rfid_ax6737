package com.amorenew.orange_rfid.alps_rfid_ax6737

import android.util.Log
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import io.reactivex.subjects.PublishSubject
import io.reactivex.disposables.Disposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.flutter.plugin.common.EventChannel
import io.reactivex.Observer


class AlpsRfidAx6737Plugin : MethodCallHandler {


    companion object {
        private val METHOD_SET_POWER_LEVLEL = "setPowerLevel"
        private val METHOD_START_READ = "startRead"
        private val METHOD_STOP_READ = "stopRead"
        private val METHOD_CLEAR = "clear"
        private val METHOD_CLOSE = "close"
        private val CHANNEL_DATA = "alps_rfid_ax6737/data"

        private lateinit var registrar: Registrar
        private val dataSubject = PublishSubject.create<String>()

        lateinit var serialPortHelper: SerialPortHelper;

        var serialListener = object : SerialListener {
            override fun onDataUpdated(idString: String) {
                dataSubject.onNext(idString)
            }
        };


        @JvmStatic
        fun registerWith(registrar: Registrar) {
            this.registrar = registrar;
            initDataChannel(registrar)
            serialPortHelper = SerialPortHelper(serialListener)
            val channel = MethodChannel(registrar.messenger(), "alps_rfid_ax6737")
            channel.setMethodCallHandler(AlpsRfidAx6737Plugin())
            Log.d("idString", "register with")

        }

        private fun initDataChannel(registrar: Registrar) {
            val dataEventChannel = EventChannel(registrar.messenger(),
                    CHANNEL_DATA)
            dataEventChannel.setStreamHandler(object : EventChannel.StreamHandler {
                override fun onListen(value: Any?, eventSink: EventChannel.EventSink) {
                    dataSubject
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<String> {
                                override fun onSubscribe(d: Disposable) {
                                    Log.d("idString", "onSubscribe")
                                }

                                override fun onNext(data: String) {
                                   // Log.d("idString", data)

                                    eventSink.success(data)
                                }

                                override fun onError(e: Throwable) {
                                    Log.d("idString", "onError")

                                }

                                override fun onComplete() {
                                    Log.d("idString", "onComplete")

                                }
                            })
                }

                override fun onCancel(o: Any?) {

                }
            })
        }

    }

    private lateinit var result: Result

    override fun onMethodCall(call: MethodCall, result: Result) {
        this.result = result
        when (call.method) {
            "getPlatformVersion" ->
                result.success("Android ${android.os.Build.VERSION.RELEASE}")
            METHOD_SET_POWER_LEVLEL ->
                setPowerLevel(call)
            METHOD_START_READ ->
                startRead()
            METHOD_STOP_READ ->
                stopRead()
            METHOD_CLEAR ->
                clear()
            METHOD_CLOSE ->
                close()
            else -> result.notImplemented()
        }

    }


    private fun setPowerLevel(call: MethodCall) {
        val level = call.argument<String>("level");
        var isSuccess = serialPortHelper.setPowerLevel(Integer.parseInt(level));
        result.success(isSuccess);
    }

    private fun startRead() {
        Log.d("idString", "start")

        serialPortHelper.startRead();
        result.success(true);
    }

    private fun stopRead() {
        serialPortHelper.stopRead();
        result.success(true);
    }

    private fun clear() {
        serialPortHelper.clear();
        result.success(true);
    }

    private fun close() {
        serialPortHelper.close();
        result.success(true);
    }
}
