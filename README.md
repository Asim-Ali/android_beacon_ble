/**
 * @author Asim Ali Khan
 * @version 1.0
 */

# Beacon Capture
An android application that can detect nearby BLE enabled devices (also known as BLE Beacons/ iBeacons), and show its RSSI, MAC address, major, minor, and UUIDs.

What is BLE?
Bluetooth Low Energy is a wireless personal area network technology which intends to provide considerably reduced power consumption and cost.

Things to remember:
-> The app can work only on those devices which supports bluetooth technology.
-> One needs to open its mobile location and bluetooth for the app to function it properly.
-> The app will itself ask your permission to access location and turn on the bluetooth.

How does it work:
-> Clone the repository in your local directory.
-> Run it on a local device rather than an emulator as Android Studio AVDs doesn't support BLE Technology.
-> Once the app is installed on your android smartphone, it will ask to access your location.
-> Allow it to access your location and also turn on your location feature.
-> Click on the start scan button.
-> It may ask to turn on the bluetooth.
-> Once you have turned on the bluetooth it will start scanning the nearby BLE enabled devices also known as beacons.
-> It will detect only those beacons/ bluetooth devices which have a name associated with it.
-> It will detect the devices and show it in a list with the device's RSSI, UUID, Name, MAC address, major, and minor.
-> The app will not stop scanning until you press the button again.
-> Signal Strength is fixed at -100
