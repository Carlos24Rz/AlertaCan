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
import Firebase



class FormViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate /*UIPickerViewDelegate, UIPickerViewDataSource*/{
    
    //bottons
//    @IBOutlet weak var vwRaceDropDown:UIView!
    @IBOutlet weak var colorButton:UIButton!
    @IBOutlet weak var raceButton:UIButton!
    @IBOutlet weak var sizeButton:UIButton!
    @IBOutlet weak var sexButton:UIButton!
    //Storage image
    @IBOutlet var uploadImageView: UIImageView!
    @IBOutlet weak var homeButton: UIButton!
    @IBOutlet weak var mapButton: UIButton!
    
        
    
    
    private let storage = Storage.storage().reference()
    
    // Dog Manager
    var dogManager : DogManager? = nil
    var status = "Perdido"
    // User info
    var user : String? = nil
    
    // Collection to display on screen after filters:
    var filteredCollection : [Dog]? = nil
    
    @IBAction func changeScreen(_ sender: UIButton) {
        if (sender == homeButton) {
            performSegue(withIdentifier: "formToHome", sender: nil)
        }
        else {
            performSegue(withIdentifier: "formToMap", sender: nil)
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if (segue.identifier == "formToMap") {
            let destinationVC = segue.destination as! MapViewController
            destinationVC.dogManager = self.dogManager
            destinationVC.filteredCollection = self.filteredCollection
            destinationVC.user = self.user
        } else if (segue.identifier == "formToHome") {
            let destinationVC = segue.destination as! HomeViewController
            destinationVC.user = self.user
        }
    }
    
    //Call Dropdown
    let dropDown = DropDown()

  let raceOptions : [String] = ["Todos", "Golder retriever", "Mestizo", "Husky", "Labrador", "Chihuahua", "Pastor alemán", "Dálmata", "Schnauzer", "Pastor belga", "Beagle"]
    let sizeOptions : [String] = ["Todos", "Pequeño", "Mediano", "Grande"]
    let colorOptions : [String] = ["Todos", "Amarillo", "Café", "Blanco", "Negro", "Gris"]
    let sexOptions : [String] = ["Todos", "Macho", "Hembra"]
    
    
    //var pickerView = UIPickerView()
    
    
    //filters
    @IBAction func optionTapped(_ sender: UIButton) {
        if sender == sexButton {
            dropDown.dataSource = sexOptions
        } else if sender == sizeButton {
            dropDown.dataSource = sizeOptions
        } else if sender == raceButton {
            dropDown.dataSource = raceOptions
        } else {
            dropDown.dataSource = colorOptions
        }
        dropDown.anchorView = sender
        //dropDown.dataSource = raceOptions
        dropDown.show()
        dropDown.bottomOffset = CGPoint(x: 0, y:(dropDown.anchorView?.plainView.bounds.height)!)
        dropDown.topOffset = CGPoint(x: 0, y:-(dropDown.anchorView?.plainView.bounds.height)!)
        dropDown.direction = .bottom
        dropDown.selectionAction = { [weak self] (index: Int, item: String) in
             guard let _ = self else { return }
            sender.setTitle(item, for: .normal)
            self!.raceButton.currentTitle;
            self!.sizeButton.currentTitle;
            self!.colorButton.currentTitle;
            self!.sexButton.currentTitle;
            
//            self!.raceButton.currentTitle!;
//            self!.sexButton.setTitle
//            self!.colorButton.setTitle
//            self!.sizeButton.setTitle
           }
    }
    
//    func applyFilters(status : String, sex : String, size : String, race : String, color : String) {
//        dogManager!.changeFilter(key: "status", value: status)
//        dogManager!.changeFilter(key: "sex", value: sex)
//        dogManager!.changeFilter(key: "size", value: size)
//        dogManager!.changeFilter(key: "race", value: race)
//        dogManager!.changeFilter(key: "color", value: color)
//        dogManager!.applyFilters()
//        filteredCollection = dogManager!.getCollection()
//        self.dogsCollectionView.reloadData()
    //}
    
        
    
    //I want to do it here in this funtions what is in the ViwDIDLOad
//    func raceTapped() {
//        dropDown.anchorView = vwRaceDropDown
//        dropDown.dataSource = raceOptions
//        dropDown.bottomOffset = CGPoint(x: 0, y:(dropDown.anchorView?.plainView.bounds.height)!)
//        dropDown.topOffset = CGPoint(x: 0, y:-(dropDown.anchorView?.plainView.bounds.height)!)
//        dropDown.direction = .bottom
//
//    }
    
//    @IBAction func showRaceOptions(_ sender:Any){
//        dropDown.show()
//    }
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let buttonCollection : [UIButton] = [sexButton,raceButton,colorButton,sizeButton]
       
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
        
        guard let imageData = image.jpegData(compressionQuality: 0.75) else{
            return
        }
        //Specify the file and path name
        let path = "images/\(UUID().uuidString).jpg"
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




