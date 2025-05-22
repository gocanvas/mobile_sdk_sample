//
//  RootViewController.swift
//  sdksample
//
//  Created by Anuta Cosmin on 18.07.2024.
//

import UIKit

class RootViewController: UIViewController {
    
    @IBOutlet weak var licenseKeyTextView: UITextView!
    @IBOutlet weak var inputTextView: UITextView!
    @IBOutlet weak var referenceDataTextView: UITextView!
    @IBOutlet weak var prefilledDataTextView: UITextView!
    @IBOutlet weak var outputTextView: UITextView!
    @IBOutlet weak var userInterfaceStyleSegment: UISegmentedControl!

    public let viewModel = RootViewModel()

    private let textViewCornerRadius = 8.0
    private let textViewBorderWidth = 1.0
    
    public override func viewDidLoad() {
        super.viewDidLoad()
        
        // force light mode for the app main view controller
        overrideUserInterfaceStyle = .light

        viewModel.delegate = self
        
        [licenseKeyTextView,
         inputTextView,
         outputTextView,
         referenceDataTextView,
         prefilledDataTextView
        ].forEach {
            $0.layer.borderWidth = textViewBorderWidth
            $0.layer.cornerRadius = textViewCornerRadius
        }
    }
    
    @IBAction func clearTextTapped(_ sender: Any) {
        inputTextView.text = ""
    }
    
    @IBAction func launchFormTapped(_ sender: Any) {
        viewModel.licenseKey = licenseKeyTextView.text ?? ""

        viewModel.referenceDataJson = referenceDataTextView.text
        viewModel.prefilledDataJson = prefilledDataTextView.text
        
        if let text = inputTextView.text {
            Task { @MainActor in
                await viewModel.actionHandler?(text)
            }
        }
    }
    
    @IBAction func userInterfaceStyleChanged(_ sender: UISegmentedControl) {
        switch sender.selectedSegmentIndex
        {
        case 0:
            overrideUserInterfaceStyle = .light
            viewModel.userInterfaceStyle = .light
            
        case 1:
            overrideUserInterfaceStyle = .dark
            viewModel.userInterfaceStyle = .dark
            
        default:
            overrideUserInterfaceStyle = .unspecified
            viewModel.userInterfaceStyle = .unspecified
        }
    }
}

extension RootViewController: RootViewModelDelegate {
    func didReceiveResponse(jsonResponse: String) {
        outputTextView.text = jsonResponse
    }
}
