//
//  RootViewController.swift
//  sdksample
//
//  Created by Anuta Cosmin on 18.07.2024.
//

import UIKit

class RootViewController: UIViewController {
    
    @IBOutlet weak var companyGuidTextField: UITextField!
    @IBOutlet weak var inputTextView: UITextView!
    @IBOutlet weak var referenceDataTextView: UITextView!
    @IBOutlet weak var prefilledDataTextView: UITextView!
    @IBOutlet weak var outputTextView: UITextView!
    
    public let viewModel = RootViewModel()

    private let textViewCornerRadius = 8.0
    private let textViewBorderWidth = 1.0
    
    public override func viewDidLoad() {
        super.viewDidLoad()
        
        viewModel.delegate = self
        
        [inputTextView, outputTextView, referenceDataTextView, prefilledDataTextView].forEach {
            $0.layer.borderWidth = textViewBorderWidth
            $0.layer.cornerRadius = textViewCornerRadius
        }
    }
    
    @IBAction func clearTextTapped(_ sender: Any) {
        inputTextView.text = ""
    }
    
    @IBAction func launchFormTapped(_ sender: Any) {
        viewModel.companyGuid = companyGuidTextField.text ?? ""
        
        let referenceDataJson = referenceDataTextView.text ?? ""
        let prefilledDataJson = prefilledDataTextView.text ?? ""
        
        if !referenceDataJson.isEmpty {
            viewModel.referenceDataJson = referenceDataJson
        } else {
            viewModel.referenceDataJson = nil
        }
        
        if !prefilledDataJson.isEmpty {
            viewModel.prefilledDataJson = prefilledDataJson
        } else {
            viewModel.prefilledDataJson = nil
        }
        
        if let text = inputTextView.text {
            Task { @MainActor in
                await viewModel.actionHandler?(text)
            }
        }
    }
}

extension RootViewController: RootViewModelDelegate {
    func didReceiveResponse(jsonResponse: String) {
        outputTextView.text = jsonResponse
    }
}
