//
//  RootViewController.swift
//  sdksample
//
//  Created by Anuta Cosmin on 18.07.2024.
//

import UIKit

class RootViewController: UIViewController {
    
    @IBOutlet weak var inputTextView: UITextView!
    @IBOutlet weak var outputTextView: UITextView!
    
    public let viewModel = RootViewModel()

    private let textViewCornerRadius = 8.0
    private let textViewBorderWidth = 1.0
    
    public override func viewDidLoad() {
        super.viewDidLoad()
        
        viewModel.delegate = self
        
        inputTextView.layer.borderWidth = textViewBorderWidth
        inputTextView.layer.cornerRadius = textViewCornerRadius
        
        outputTextView.layer.borderWidth = textViewBorderWidth
        outputTextView.layer.cornerRadius = textViewCornerRadius
        
        let path = Bundle.main.path(forResource: "input", ofType: "json")
        let fileUrl = URL(filePath: path ?? "")
        let input = try? String(contentsOf: fileUrl, encoding: .utf8)
        
        inputTextView.text = input
    }
    
    @IBAction func clearTextTapped(_ sender: Any) {
        inputTextView.text = ""
    }
    
    @IBAction func launchFormTapped(_ sender: Any) {
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
