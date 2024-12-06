# Android GoCanvas Sdk Sample Application

## Description

The Android project in this repo serves as demonstration on how to integrate and use the GoCanvas Sdk, the library provided by Go Canvas to allow you to add the form flow into an android mobile application.

The project contains a simple `Activity` that is able to call the `showForm()` method. It provides an input JSON that contains the details of the form, and handles the response submission on the `ActivityResultCallback` as well as possible errors.


## Integration
The project integrates the GoCanvas Sdk library through GitHub Packages. The package can be found at https://github.com/gocanvas/mobile_sdk_android

## UI
The sample app contains the following UI components:
- `Show Form Button` - attempts to launch the form using the inputted JSON
- `Form JSON Input EditText` - used as a JSON source for launching the forms
- `Submission JSON Output EditText` - populated with the submission returned after the form flow is finished