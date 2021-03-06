from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice

device = MonkeyRunner.waitForConnection(15, '.*')

if device.installPackage('D:\\School\\Spring2015\\RealCube\\app\\build\outputs\\apk\\app-debug.apk'):
	print("install successful")
else:
	print("install failed")
package = 'atwatanabe.realcube'

activity = 'atwatanabe.realcube.RealCube.MainActivity'

runComponent = package + '\\' + activity

device.startActivity(component=runComponent)

#On my phone, I think my antivirus app stops RealCube from launching immediately,
#so these three commands wait for the antivirus app to scan RealCube, then pull
#down the notification bar and tap on the notification, launcing the app.
#MonkeyRunner.sleep(15)
#device.drag((280, 10), (280, 600), 1, 10)
#device.touch(280, 135, MonkeyDevice.DOWN_AND_UP)

MonkeyRunner.sleep(2)

#open the options activity, reset the settings to their default values,
#then adjust them a bit
device.touch(460, 170, MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(2)
device.touch(130, 450, MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(2)
device.drag((280, 220), (230, 220), 1, 10)
MonkeyRunner.sleep(2)
device.drag((280, 360), (330, 360), 1, 10)
MonkeyRunner.sleep(2)
#press the back button
device.touch(125, 930, MonkeyDevice.DOWN_AND_UP)

MonkeyRunner.sleep(2)

#open the game activity
device.touch(140, 400, MonkeyDevice.DOWN_AND_UP)

#after this, there's not much that can be done with MonkeyRunner,
#as the app is centered on the use of the Rotation Vector Sensor
