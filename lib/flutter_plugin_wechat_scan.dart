import 'dart:async';

import 'package:flutter/services.dart';

class FlutterPluginWechatScan {
  static const MethodChannel _channel =
      const MethodChannel('me.starainx/flutter_plugin_wechat_scan');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String> scan({Map<String, dynamic> params}) async {
    Map<String,dynamic> m = Map();
    final String result = await _channel.invokeMethod('scan', m);
    return result;
  }
}
