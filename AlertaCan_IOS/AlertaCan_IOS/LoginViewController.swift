//
//  LoginViewController.swift
//  IOS_AlertaCan
//
//  Created by Josué Taboada Elvira on 09/10/22.
//

import UIKit
import FirebaseAuth


class LoginViewController: UIViewController {
    
    @IBOutlet weak var emailTextField: UITextField!
    
    @IBOutlet weak var passwordTextField: UITextField!
    
    @IBOutlet weak var loginButton: UIButton!
    
    @IBOutlet weak var errorLabel: UILabel!
    
    @IBOutlet weak var privacyPolicyButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setUpElements()
        // Do any additional setup after loading the view.
    }
    
    func setUpElements(){
        //hide error label
        errorLabel.alpha = 0
    }
    

    @IBAction func loginTapped(_ sender: Any) {
        //create cleaned version
        let email = emailTextField.text!.trimmingCharacters(in: .whitespacesAndNewlines)
        let password = passwordTextField.text!.trimmingCharacters(in: .whitespacesAndNewlines)
        
        //singin the user
        Auth.auth().signIn(withEmail: email, password: password) {(result, error) in
            if error != nil{
                //couldn't sing in
                self.errorLabel.text = error!.localizedDescription
                self.errorLabel.alpha = 1
            }
            else{
                self.performSegue(withIdentifier: "loginToHome", sender: nil)
//                let formViewController =
//                self.storyboard?.instantiateViewController(identifier: Constants.Storyboard.formViewController) as?
//                    FormViewController
//
//                self.view.window?.rootViewController = formViewController; self.view.window?.makeKeyAndVisible()
            }
            
            
        }
        
        
    }
    
}
