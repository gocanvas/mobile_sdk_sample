//
//  RootViewModel.swift
//  sdksample
//
//  Created by Anuta Cosmin on 01.08.2024.
//

import Foundation
import UIKit.UIInterface

protocol RootViewModelDelegate {
    func didReceiveResponse(jsonResponse: String)
}

class RootViewModel {
    var actionHandler: ((String) async -> Void)? = nil
    var delegate: RootViewModelDelegate? = nil
    var licenseKey: String?
    var referenceDataJson: String? = nil
    var prefilledDataJson: String? = nil
    var userInterfaceStyle: UIUserInterfaceStyle = .light

    func didReceiveResponse(jsonResponse: String) {
        delegate?.didReceiveResponse(jsonResponse: jsonResponse)
    }
}
