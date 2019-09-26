#import "AlpsRfidAx6737Plugin.h"
#import <alps_rfid_ax6737/alps_rfid_ax6737-Swift.h>

@implementation AlpsRfidAx6737Plugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftAlpsRfidAx6737Plugin registerWithRegistrar:registrar];
}
@end
