//
//  SingUpViewController.swift
//  IOS_AlertaCan
//
//  Created by Josué Taboada Elvira on 09/10/22.
//

import UIKit
import FirebaseAuth
import FirebaseCore
import Firebase


class SingUpViewController: UIViewController {

    @IBOutlet weak var emailTextField: UITextField!
    
    @IBOutlet weak var passwordTextField: UITextField!
    
    @IBOutlet weak var repeatPasswordTextField: UITextField!
    
    @IBOutlet weak var singUpButton: UIButton!
    
    @IBOutlet weak var errorLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setUpElements()
    }
    
    func setUpElements(){
        //hide error label
        errorLabel.alpha = 0
    }
    
    //check the fileds and validate the data is correct
    func validateFields() -> String?{
        //check all fields are filled in
        if emailTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines) == "" || passwordTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines) == ""{
            return "por favor llene todos los espacios"
        }
        //check password is secure
        let cleanedPassword = passwordTextField.text!.trimmingCharacters(in: .whitespacesAndNewlines)
        if Utilities.isPasswordValid(cleanedPassword) == false {
            //password isn't secure enough
            return "Asegurate que la contraseña sea de minimo 8 caracteres, tenga un numero y simbolo"
        }
        return nil
    }
    

    @IBAction func singUpTapped(_ sender: Any) {
        
    //validate the fields
    let error = validateFields()
        if error != nil{
            //there's somthing wrong with fields
            showError(error!)
        }
        else{
            //create clened version data
            //this conects to the Auth
            let email = emailTextField.text!.trimmingCharacters(in: .whitespacesAndNewlines)
            let password = passwordTextField.text!.trimmingCharacters(in: .whitespacesAndNewlines)
            
            //create the users
            Auth.auth().createUser(withEmail: email, password: password) { (result, err) in
                //check for errors
                if  err != nil{
                    //there is an error creating the user
                    self.showError("Error al crear el usuario")
                }
                else{
                    self.performSegue(withIdentifier: "singUpToHome", sender: nil)
                    //if there's no error, user was created succesfully
                    let db = Firestore.firestore()
                    
                    //transsition to form screen
                    self.trasitionToForm()
                }
                
            }
                
            
            
        }
    }
    func showError(_ message:String){
        errorLabel.text = message
        errorLabel.alpha = 1
    }
    func trasitionToForm(){
        let formViewController =
        storyboard?.instantiateViewController(identifier: Constants.Storyboard.formViewController) as?
            FormViewController
        
        view.window?.rootViewController = formViewController; view.window?.makeKeyAndVisible()
    }
    
}
