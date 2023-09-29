<img src="https://github.com/ivanvc/android-grayscale-toggle/raw/main/app/src/main/ic_launcher-playstore.png"
     width="15%" align="right" alt="Logo" style="padding-left: 10px" />

# Android Grayscale Toggle

This is a minimal and straightforward application to toggle the grayscale color
correction setting on Android devices.

If the application doesn't have permission to change system settings, it will
show instructions on how to do it with ADB. Otherwise, launching the app toggles
the setting. This application is helpful with launchers such as Niagara
Launcher, where you can define an action for a home screen button, or with
devices with a designated function physical button, like Unihertz phones.

[Many](https://www.wired.com/story/grayscale-ios-android-smartphone-addiction/)
[articles](https://observer.com/2018/05/grayscale-can-cure-smartphone-addiction/)
[cover](https://www.theverge.com/23637672/grayscale-iphone-android-pixel-samsung-galaxy-how-to)
[why](https://lifehacker.com/change-your-screen-to-grayscale-to-combat-phone-addicti-1795821843)
[having](https://www.vice.com/en/article/xwm38k/grayscale-setting-phone-addiction)
your smartphone screen in grayscale is a good idea. However, sometimes it's
helpful to switch to full color in case you need to open a navigation app, or if
you want to see a picture you got from a message, or if you're showing your
phone screen to another person.

## Installation

Get the APK from the [releases page](https://github.com/ivanvc/android-grayscale-toggle/releases). Then, install it on your phone.

## Granting Permission to the App

1. Ensure you have ADB installed and configured on your machine. Please take a
   look at the [official ABD page](https://developer.android.com/tools/adb).
2. Connect your phone to your computer and allow USB debugging.
3. Open a terminal and run:
   ```sh
   adb shell pm grant vc.ivan.grayscaletoggle android.permission.WRITE_SECURE_SETTINGS
   ```

That's it. Opening the app will toggle the grayscale mode.

## License

See [LICENSE](LICENSE) © [Ivan Valdes](https://github.com/ivanvc/)
