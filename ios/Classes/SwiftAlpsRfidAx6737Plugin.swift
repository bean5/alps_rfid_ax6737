import Flutter
import UIKit

public class SwiftAlpsRfidAx6737Plugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "alps_rfid_ax6737", binaryMessenger: registrar.messenger())
    let instance = SwiftAlpsRfidAx6737Plugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }
}
