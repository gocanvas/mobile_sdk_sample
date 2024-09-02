# iOS GCSdk Sample Application

## Description

The XCode project in this repo serves as demonstration on how to integrate and use the GCSdk, the framework provided by Go Canvas to allow you to add the form flow into an iOS mobile application.

The project contains a simple SwiftUI `View` that wraps a `UIViewController`. This controller calls the `launchForm()` method from the `GCSdk`, provides it with an input JSON that contains the details of the form, and handles the response submission on the completion block as well as possible errors.
```swift
do {
try await formLauncher.launchForm(withJSONinput: json,
                                  inNavigationController: navigationController) { jsonResponse in
    rootViewController.viewModel.didReceiveResponse(jsonResponse: jsonResponse)
    }
} catch {
    if let error = error as? FormLauncherError {
        showAlert(message: error.message)
    }
}
```
## Integration

The project integrates the GCSdk library through Swift Package Manager.
The package can be found at https://github.com/gocanvas/ios_sdk_package 

## UI

The sample app contains the following UI components:

* `Clear Input Text` button - used to clear the input Text View below it
* `Launch Form` button - attempts to launch the form using the inputted JSON
* `Form JSON Input` TextView - used as a JSON source for launching the forms
* `SDK JSON Output` TextView - populated with the submission returned after the form flow is finished