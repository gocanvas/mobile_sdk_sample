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

    private var rootViewController = RootViewController()
    
    public func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
    
    }

    public func makeUIViewController(context: Context) -> some UIViewController {
        let navigationController = UINavigationController(rootViewController: rootViewController)
        
        rootViewController.viewModel.actionHandler = { json in
            let config = GCSdkConfig(companyGuid: rootViewController.viewModel.companyGuid)
            let formLauncher = GoCanvasFormLauncher(config: config)
        
            do {
                let formConfig = GCSdkFormConfig(jsonInput: json)
                let controller = try await formLauncher.formFlowController(config: formConfig,
                                                                           messagingDelegate: self) { jsonResponse in
                    rootViewController.viewModel.didReceiveResponse(jsonResponse: jsonResponse)
                }
                navigationController.present(controller, animated: true)
            } catch {
                if let error = error as? FormLauncherError {
                    showAlert(message: error.message)
                } else {
                    showAlert(message: "Unknown error")
                }
            }
        }
        return navigationController
     }
    
    private func showAlert(message: String) {
        let alert = UIAlertController(title: "Error", message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
        rootViewController.present(alert, animated: true)
    }
}

extension GoCanvasView: MessagingDelegate {
    public func showResumeMessage(withTitle title: String, body: String, discardAction: MessagingAction, continueAction: MessagingAction) {
        let alert = UIAlertController(title: title, message: body, preferredStyle: UIAlertController.Style.alert)
        
        alert.addAction(UIAlertAction(title: discardAction.actionTitle, style: UIAlertAction.Style.default, handler: { _ in
            discardAction.actionHandler()
        }))
        alert.addAction(UIAlertAction(title: continueAction.actionTitle, style: UIAlertAction.Style.default, handler: { _ in
            continueAction.actionHandler()
        }))
        
        rootViewController.present(alert, animated: true)
    }
}
