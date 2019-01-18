import 'dart:async';

import 'package:flutter/services.dart';

class FlutterPluginWechatScan {
  static const MethodChannel _channel =
      const MethodChannel('me.starainx/flutter_plugin_wechat_scan');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String> scan({
    String title,
    String tipText,
  }) async {
    Map<String, dynamic> m = Map();
    if (title != null) {
      m['title'] = title;
    }
    if (tipText != null) {
      m['tipText'] = tipText;
    }
    print('mmmm=>  $m, $title, $tipText');
    final String result = await _channel.invokeMethod('scan', m);
    return result;
  }
}
