//
//  RootViewModel.swift
//  sdksample
//
//  Created by Anuta Cosmin on 01.08.2024.
//

import Foundation
import GCSdk

protocol RootViewModelDelegate {
    func didReceiveResponse(jsonResponse: String)
}

class RootViewModel {
    var actionHandler: ((String) async -> Void)? = nil
    var delegate: RootViewModelDelegate? = nil
    var formLauncher: GoCanvasFormLauncher? = nil
    
    //TODO: replace with your company GUID
    let companyGuid = "DemoCompanyGUID"
    
    func didReceiveResponse(jsonResponse: String) {
        delegate?.didReceiveResponse(jsonResponse: jsonResponse)
    }
}
