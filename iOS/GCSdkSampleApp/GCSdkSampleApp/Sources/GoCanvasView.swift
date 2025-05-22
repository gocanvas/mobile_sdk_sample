//
//  GoCanvasView.swift
//  sdksample
//
//  Created by Anuta Cosmin on 17.07.2024.
//

import Foundation
import SwiftUI
import GCSdk

public struct GoCanvasView : UIViewControllerRepresentable {
    enum SDKConfigKeys: String {
        case UserInterfaceStyle = "MOBILE_INTERFACE_THEME"
    }

    private var rootViewController = RootViewController()
    
    public func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
        
    }
    
    public func makeUIViewController(context: Context) -> some UIViewController {
        let navigationController = UINavigationController(rootViewController: rootViewController)
        
        rootViewController.viewModel.actionHandler = { json in
            let licenseKey = rootViewController.viewModel.licenseKey ?? ""
            let config = Config(licenseKey: licenseKey)
            let formLauncher = FormLauncher(config: config)
                        
            formLauncher.addConfigValue(key: SDKConfigKeys.UserInterfaceStyle.rawValue, value: rootViewController.viewModel.userInterfaceStyle)
            
            do {
                let formConfig = FormConfig(jsonInput: json,
                                            referenceDataJson: rootViewController.viewModel.referenceDataJson,
                                            prefilledDataJson: rootViewController.viewModel.prefilledDataJson)
                let controller = try await formLauncher.formFlowController(config: formConfig,
                                                                           messagingDelegate: self) { jsonResponse in
                    rootViewController.viewModel.didReceiveResponse(jsonResponse: jsonResponse)
                }
                navigationController.present(controller, animated: true)
            } catch {
                if let error = error as? GoCanvasError {
                    showAlert(message: error.message, userInterfaceStyle: rootViewController.viewModel.userInterfaceStyle)
                } else if let error = error as? GoCanvasLicenseError {
                    showAlert(message: error.message, userInterfaceStyle: rootViewController.viewModel.userInterfaceStyle)
                } else {
                    showAlert(message: "Unknown error", userInterfaceStyle: rootViewController.viewModel.userInterfaceStyle)
                }
            }
        }
        return navigationController
    }
    
    private func showAlert(message: String, userInterfaceStyle: UIUserInterfaceStyle = .light) {
        let alert = UIAlertController(title: "Error", message: message, preferredStyle: .alert)
        alert.overrideUserInterfaceStyle = userInterfaceStyle
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
        rootViewController.present(alert, animated: true)
    }
}

extension GoCanvasView: CanvasSdkMessagingDelegate {
    public func showResumeMessage(withTitle title: String, body: String, discardAction: CanvasSdkMessagingAction, continueAction: CanvasSdkMessagingAction, userInterfaceStyle: UIUserInterfaceStyle = .light) {
        let alert = UIAlertController(title: title, message: body, preferredStyle: UIAlertController.Style.alert)
        alert.overrideUserInterfaceStyle = userInterfaceStyle
        alert.addAction(UIAlertAction(title: discardAction.actionTitle, style: UIAlertAction.Style.default, handler: { _ in
            discardAction.actionHandler()
        }))
        alert.addAction(UIAlertAction(title: continueAction.actionTitle, style: UIAlertAction.Style.default, handler: { _ in
            continueAction.actionHandler()
        }))
        
        rootViewController.present(alert, animated: true)
    }
}
