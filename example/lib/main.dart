import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:alps_rfid_ax6737/alps_rfid_ax6737.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await AlpsRfidAx6737.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }
    AlpsRfidAx6737.dataStream
        .receiveBroadcastStream()
        .listen(rdfidDataListener);

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  void rdfidDataListener(dynamic event) {
    print('dart $event');
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: <Widget>[
              RaisedButton(
                child: Text('startRead'),
                onPressed: () {
                  AlpsRfidAx6737.startRead();
                },
              ),
              RaisedButton(
                child: Text('stopRead'),
                onPressed: () {
                  AlpsRfidAx6737.stopRead();
                },
              ),
              RaisedButton(
                child: Text('clear'),
                onPressed: () {
                  AlpsRfidAx6737.clear();
                },
              ),
              RaisedButton(
                child: Text('close'),
                onPressed: () {
                  AlpsRfidAx6737.close();
                },
              ),
              RaisedButton(
                child: Text('Set Power Level to 10'),
                onPressed: () {
                  AlpsRfidAx6737.setPowerLevel(level: 10);
                },
              ),
              Text('Running on: $_platformVersion\n'),
            ],
          ),
        ),
      ),
    );
  }
}
