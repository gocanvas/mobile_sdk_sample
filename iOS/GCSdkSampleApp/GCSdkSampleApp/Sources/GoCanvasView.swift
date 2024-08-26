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
    
    private var formLauncher = GoCanvasFormLauncher()
    
    public func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
    
    }

    public func makeUIViewController(context: Context) -> some UIViewController {
        let navigationController = UINavigationController(rootViewController: rootViewController)
        
        rootViewController.viewModel.actionHandler = { json in
            do {
                try await formLauncher.launchForm(withJSONinput: json, inNavigationController: navigationController) { jsonResponse in
                    rootViewController.viewModel.didReceiveResponse(jsonResponse: jsonResponse)
                }
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
