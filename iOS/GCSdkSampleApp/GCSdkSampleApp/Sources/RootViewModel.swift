//
//  RootViewModel.swift
//  sdksample
//
//  Created by Anuta Cosmin on 01.08.2024.
//

import Foundation

protocol RootViewModelDelegate {
    func didReceiveResponse(jsonResponse: String)
}

class RootViewModel {
    var actionHandler: ((String) async -> Void)? = nil
    var delegate: RootViewModelDelegate? = nil
    
    func didReceiveResponse(jsonResponse: String) {
        delegate?.didReceiveResponse(jsonResponse: jsonResponse)
    }
}
