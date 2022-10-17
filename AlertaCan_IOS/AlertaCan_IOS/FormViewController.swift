//
//  FormViewController.swift
//  IOS_AlertaCan
//
//  Created by Josué Taboada Elvira on 09/10/22.
//

import UIKit
import FirebaseStorage
import FirebaseFirestore
import DropDown

class FormViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate /*UIPickerViewDelegate, UIPickerViewDataSource*/{
    
    @IBOutlet weak var vwDropDown: UIView!
    @IBOutlet weak var raceButton: UIButton!
    @IBOutlet var uploadImageView: UIImageView!
    @IBOutlet weak var homeButton: UIButton!
    @IBOutlet weak var mapButton: UIButton!
    
    private let storage = Storage.storage().reference()
    
    @IBAction func changeScreen(_ sender: UIButton) {
        if (sender == homeButton) {
            performSegue(withIdentifier: "formToHome", sender: nil)
        }
        else {
            performSegue(withIdentifier: "formToMap", sender: nil)
        }
     
    }
    
    let dropDown = DropDown()
    let raceOptions : [String] = ["Todos", "Mestizo", "Husky", "Labrador", "Chihuahua", "Pastor Alemán", "Dálmata"]
    
    var pickerView = UIPickerView()
    
    //filters
    override func viewDidLoad() {
        super.viewDidLoad()
        dropDown.anchorView = vwDropDown
        dropDown.dataSource = raceOptions
        dropDown.bottomOffset = CGPoint(x: 0, y:(dropDown.anchorView?.plainView.bounds.height)!)
        dropDown.topOffset = CGPoint(x: 0, y:-(dropDown.anchorView?.plainView.bounds.height)!)
        dropDown.direction = .bottom
        dropDown.selectionAction = { [weak self] (index: Int, item: String) in //8
             guard let _ = self else { return }
            self?.raceButton.setTitle(item, for: .normal)
           }
    }
    
    @IBAction func showRaceOptions(_ sender:Any) {
        dropDown.show()
    }
    
    
    //action when we tapped the upload photo
    @IBAction func uploadTapped() {
        let picker = UIImagePickerController()
        picker.sourceType = .photoLibrary
        picker.delegate = self
        picker.allowsEditing = true
        present(picker, animated: true)
    }
    
    //User finish picking an image
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]){
        
        picker.dismiss(animated: true, completion: nil)
        
        guard let image = info[UIImagePickerController.InfoKey.editedImage] as? UIImage else{
            return
        }
        //show the image that the user select
        DispatchQueue.main.async {
            self.uploadImageView.image = image
        }
        
        guard let imageData = image.pngData() else{
            return
        }
        //Specify the file and path name
        let path = "images/\(UUID().uuidString).png"
        let fileRef = storage.child(path)
        //upload the data
        fileRef.putData(imageData, metadata: nil, completion: { _, error in
            guard error == nil else{
                return
            }
            //upload image to firestore data
            let db = Firestore.firestore()
            db.collection("images").document().setData(["url" : path])
        })
    
    }
    
    
    //picker it's cancel
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController){
        picker.dismiss(animated: true, completion: nil)
    }

}

