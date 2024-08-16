# Usage

### SDK Api

The Sdk provides the following api interface:

```kotlin
interface CanvasSdkApi {

    /**
     * Displays the form into another activity.
     *
     * @param formJson the form in JSON string format
     * @param activity the parent activity
     * @param formLauncher the result launcher that receives the response
     */
    fun showForm(formJson: String, activity: Activity, formLauncher: ActivityResultLauncher<Intent>)

    /**
     * Returns the submission response in JSON string format.
     */
    fun getResponse(): String
}
```
It can be accessed using the `CanvasSdk` instance.

### Display Form

1. Before displaying the form, make sure you have first registered for `ActivityResult`:

```java
ActivityResultLauncher<Intent> formLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
    new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            // Handle the result
        }
});
```
2. Then you can start displaying the form by calling the `showForm` method on the SDK Api entry point instance as follows:

```java
CanvasSdk.INSTANCE.showForm(formJson, activity, formLauncher);
```

### Receive Form Response

The response is being returned through the `ActivityResultCallback` passed to `formLauncher` on the `showForm` method.

Result code info:
- `Activity.RESULT_OK` - when form flow has been completed successfully
- `Activity.RESULT_CANCELED` - when form flow has been canceled due to user cancellation or through an error

Intent extras key access:
- `CanvasSdkKey.RESPONSE_KEY` - contains the info if the form response exists as `Boolean`
- `CanvasSdkKey.ERROR_NUMBER_KEY` - contains the error code in `String` format
- `CanvasSdkKey.ERROR_MESSAGE_KEY` - contains the error message in `String` format

Reading the response:

You can access the form response by calling the `CanvasSdk.getResponse()` method after the result is received from the `formLauncher`.

Example:

```java
ActivityResultLauncher<Intent> formLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                int resultCode = result.getResultCode();
                Intent data = result.getData();

                Log.i("ActivityResult", "resultCode: " + resultCode);

                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // form completed successfully
                        if (data != null && data.hasExtra(RESPONSE_KEY)) {
                            Log.i("ActivityResult", "form has response: " + data.getExtras().get(RESPONSE_KEY));
                            Log.i("ActivityResult", "form response: " + CanvasSdk.INSTANCE.getResponse());
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        // user canceled or an error occurred
                        if (data != null && data.hasExtra(ERROR_NUMBER_KEY) && data.hasExtra(ERROR_MESSAGE_KEY)) {
                            Log.i("ActivityResult", "error code: " + data.getExtras().get(ERROR_NUMBER_KEY));
                            Log.i("ActivityResult", "error message: " + data.getExtras().get(ERROR_MESSAGE_KEY));
                        }
                        break;
                }
            }
        });
```

### Resume Form Response

The SDK provides the ability to resume the form response in case of an app crash or of a partially form completion.

In order to do that, you just have to call the `CanvasSdk.showForm` method again by passing the same `formJson` input. The User will be prompted when there is a partially saved form response and he/she will be able to choose between resuming the form or starting a new submission of it.

### Errors

The SDK supports the following error types:
- `INVALID_JSON` - when the `formJson` cannot pe parsed to `Form`
- `INVALID_FORM_DEFINITION` - when the `Form` has no sections, sheets or entries
- `INVALID_SAVED_RESPONSE` - when the `Response` cannot be restored after partially form saving

Each error has associated an error code and a message as follows:

```kotlin
enum class CanvasSdkErrorType(val statusCode: Int, val errorDescription: String) {
    INVALID_JSON(90000, "Unable to parse form definition."),
    INVALID_FORM_DEFINITION(90001, "Form definition is invalid."),
    INVALID_SAVED_RESPONSE(90002, "Unable to resume response.")
}
```
