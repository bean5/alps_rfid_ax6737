import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:alps_rfid_ax6737/alps_rfid_ax6737.dart';

void main() {
  const MethodChannel channel = MethodChannel('alps_rfid_ax6737');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await AlpsRfidAx6737.platformVersion, '42');
  });
}
