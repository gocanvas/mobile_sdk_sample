package com.gocanvas.sdksample

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.gocanvas.sdk.api.CanvasSdk
import com.gocanvas.sdk.api.CanvasSdkFormConfig
import com.gocanvas.sdk.api.CanvasSdkInterfaceTheme
import com.gocanvas.sdk.api.CanvasSdkKey
import com.gocanvas.sdksample.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels<MainViewModel>()

    private val formLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        result.data?.let { data ->
            handleResult(result.resultCode, data)
            showResult(result.resultCode, data)
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collect { setupUiState(it) }
            }
        }

        binding.showForm.setOnClickListener {
            saveViewModelState()
            initSdk()
            showForm()
        }

        initFromAssets()
    }

    private fun initSdk() {
        val uiState = viewModel.uiState.value
        CanvasSdk.init(uiState.licenseKey ?: "")
        uiState.interfaceTheme?.let { CanvasSdk.addConfigValue("MOBILE_INTERFACE_THEME", it) }
    }

    private fun showForm() {
        val state = viewModel.uiState.value

        val formConfig = CanvasSdkFormConfig(
            state.formJson ?: "",
            state.referenceDataJson,
            state.prefilledEntriesJson
        )

        CanvasSdk.showForm(formConfig, this, formLauncher)
    }

    private fun handleResult(resultCode: Int, data: Intent) {
        val tag = "CanvasSdkResult"

        when (resultCode) {
            RESULT_OK ->
                // form completed successfully
                if (data.hasExtra(CanvasSdkKey.RESPONSE_KEY)) {
                    Log.i(tag, "form response: " + CanvasSdk.getResponse())
                }

            RESULT_CANCELED ->
                // user canceled or an error occurred
                if (data.hasExtra(CanvasSdkKey.ERROR_NUMBER_KEY) && data.hasExtra(CanvasSdkKey.ERROR_MESSAGE_KEY)) {
                    Log.i(tag, "error code: " + data.extras?.getString(CanvasSdkKey.ERROR_NUMBER_KEY))
                    Log.i(tag, "error message: " + data.extras?.getString(CanvasSdkKey.ERROR_MESSAGE_KEY))
                }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun showResult(resultCode: Int, intent: Intent) {
        var errorCode: String? = "None"
        var errorMessage: String? = "None"

        intent.extras?.let { extras ->
            if (extras.containsKey(CanvasSdkKey.ERROR_NUMBER_KEY)) {
                errorCode = extras.getString(CanvasSdkKey.ERROR_NUMBER_KEY)
            }

            if (extras.containsKey(CanvasSdkKey.ERROR_MESSAGE_KEY)) {
                errorMessage = extras.getString(CanvasSdkKey.ERROR_MESSAGE_KEY)
            }

            if (extras.containsKey(CanvasSdkKey.RESPONSE_KEY)) {
                viewModel.setResponseUiState(CanvasSdk.getResponse())
            }
        }

        val resultCodeString = String.format("Result Code: \n%d\n", resultCode)
        val errorCodeString = String.format("Error Number: \n%s\n", errorCode)
        val errorMessageString = String.format("Error Message: \n%s\n", errorMessage)
        val messageText =
            String.format("%s\n%s\n%s", resultCodeString, errorCodeString, errorMessageString)
        showMessage(this, messageText)
    }

    private fun showMessage(context: Context, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
            .setTitle("Result")
            .setCancelable(false)
            .setNeutralButton("OK", null)
        val alert = builder.create()
        alert.show()
    }

    override fun onPause() {
        saveViewModelState()
        binding.run {
            inputLicenseKey.setText("")
            inputJson.setText("")
            inputReferenceDataJson.setText("")
            inputPrefilledEntriesJson.setText("")
            submissionJson.setText("")
        }
        super.onPause()
    }

    private fun setupUiState(state: SdkApiUiState) {
        binding.run {
            inputLicenseKey.setText(state.licenseKey)
            inputJson.setText(state.formJson)
            inputReferenceDataJson.setText(state.referenceDataJson)
            inputPrefilledEntriesJson.setText(state.prefilledEntriesJson)
            submissionJson.setText(state.responseJson)
            state.interfaceTheme?.let { setSdkThemeUiState(it) }
        }
    }

    private fun saveViewModelState() {
        binding.run {
            viewModel.setSdkApiUiState(
                SdkApiUiState(
                    licenseKey = inputLicenseKey.text?.toString(),
                    formJson = inputJson.text?.toString(),
                    referenceDataJson = inputReferenceDataJson.text?.toString(),
                    prefilledEntriesJson = inputPrefilledEntriesJson.text?.toString(),
                    responseJson = submissionJson.text?.toString(),
                    interfaceTheme = getSdkTheme()
                )
            )
        }
    }

    private fun initFromAssets() {
        val licenseKey = getStringFromAssets("license_key.txt")
        val formInput = getStringFromAssets("form_input.json")
        val referenceDataInput = getStringFromAssets("reference_data_input.json")
        val prefilledEntriesInput = getStringFromAssets("prefilled_entries_input.json")

        binding.run {
            inputLicenseKey.setText(licenseKey)
            inputJson.setText(formInput)
            inputReferenceDataJson.setText(referenceDataInput)
            inputPrefilledEntriesJson.setText(prefilledEntriesInput)
        }

        saveViewModelState()
    }

    private fun getStringFromAssets(fileName: String): String {
        return application.assets.open(fileName)
            .bufferedReader()
            .use { it.readText() }
    }

    private fun getSdkTheme(): CanvasSdkInterfaceTheme? {
        val checkedChipId = binding.themeSelectionGroup.checkedChipId

        return when (checkedChipId) {
            R.id.interfaceThemeLight -> CanvasSdkInterfaceTheme.LIGHT
            R.id.interfaceThemeDark -> CanvasSdkInterfaceTheme.DARK
            R.id.interfaceThemeSystem -> CanvasSdkInterfaceTheme.SYSTEM
            else -> null
        }
    }

    private fun setSdkThemeUiState(theme: CanvasSdkInterfaceTheme) {
        return when (theme) {
            CanvasSdkInterfaceTheme.LIGHT -> binding.interfaceThemeLight.isChecked = true
            CanvasSdkInterfaceTheme.DARK -> binding.interfaceThemeDark.isChecked = true
            CanvasSdkInterfaceTheme.SYSTEM -> binding.interfaceThemeSystem.isChecked = true
        }
    }
}