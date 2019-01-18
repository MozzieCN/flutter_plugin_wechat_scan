#import "FlutterPluginWechatScanPlugin.h"
#import <flutter_plugin_wechat_scan/flutter_plugin_wechat_scan-Swift.h>

@implementation FlutterPluginWechatScanPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterPluginWechatScanPlugin registerWithRegistrar:registrar];
}
@end
