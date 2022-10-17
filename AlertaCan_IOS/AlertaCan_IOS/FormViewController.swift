//
//  FormViewController.swift
//  IOS_AlertaCan
//
//  Created by Josué Taboada Elvira on 09/10/22.
//

import UIKit
import FirebaseStorage
import FirebaseFirestore

class FormViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    @IBOutlet var uploadImageView: UIImageView!
    @IBOutlet weak var homeButton: UIButton!
    @IBOutlet weak var mapButton: UIButton!
    
    @IBAction func changeScreen(_ sender: UIButton) {
        if (sender == homeButton) {
            performSegue(withIdentifier: "formToHome", sender: nil)
        }
        else {
            performSegue(withIdentifier: "formToMap", sender: nil)
        }
     
    }
    

    override func viewDidLoad() {
        super.viewDidLoad()

        
        // Do any additional setup after loading the view.
    }
    
    //action when we tapped the upload photo
    @IBAction func uploadTapped() {
        let picker = UIImagePickerController()
        picker.sourceType = .photoLibrary
        picker.delegate = self
        picker.allowsEditing = true
        present(picker, animated: true)
    }
    
    //User finish picking a image
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]){
        picker.dismiss(animated: true, completion: nil)
        guard let image = info[UIImagePickerController.InfoKey.editedImage] as? UIImage else{
            return
        }
        guard let imageData = image.pngData() else{
            return
        }
        //upload image data
        
    }
    //picker it's cancel
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController){
        picker.dismiss(animated: true, completion: nil)
    }

}
