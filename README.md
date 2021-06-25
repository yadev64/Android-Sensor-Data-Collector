# Android-Sensor-Data-Collector
v 0.1 beta

This is a simple, one-click Android application to continuously collect sensor data from the device in-built sensors like the GPS coordinates, Gyroscope & accelerometer data. All the collected data will be stored in a CSV file, which can later be used as a data source to train ML models, create systems that compute motion data to generate insights etc.

### Screenshots

![DC](https://user-images.githubusercontent.com/21107275/123445126-381c1380-d5f5-11eb-9107-11d9b91d1699.jpg)

### Tools required

1. Android Studio
2. Android SDK (Targeting Android 9)
3. AVD/Android device (To test the app)

### How to build?

To use/modify the app:

1. Clone the repo to your local machine
2. Open Android Studio and open this project

Now, you can edit the code and run it from here.
To run the project, just hit run on the specified device.

### How to use?

Once you open the app, just click the toggle button at the bottom. It will start the data capture process and write all the data into a CSV file.
To get the CSV file, just go to your local storage of your android device > Android > Data, in here, select the folder for DC application. In there, you'll find the CSV file.

### Known Bugs

1. Data Inconsistancy
