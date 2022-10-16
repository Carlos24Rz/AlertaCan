//
//  HomeViewController.swift
//  IOS_AlertaCan
//
//  Created by Carlos Vega on 10/10/22.
//

import UIKit
import FirebaseAuth
import FirebaseCore
import Firebase
import FirebaseFirestore
import DropDown

class HomeViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource {

    // Collection View
    @IBOutlet weak var dogsCollectionView: UICollectionView!
    
    // Nav buttons
    @IBOutlet weak var registerButton: UIButton!
    @IBOutlet weak var mapButton: UIButton!
    
    
    // Filter buttons
    @IBOutlet weak var sexButton: UIButton!
    @IBOutlet weak var raceButton: UIButton!
    @IBOutlet weak var colorButton: UIButton!
    @IBOutlet weak var sizeButton: UIButton!
        
    // Status buttons
    @IBOutlet weak var perdidosButton: UIButton!
    @IBOutlet weak var avistadosButton: UIButton!
    // To use for filters (Perdido || Encontrado)
    var currentStatusButton: UIButton? = nil
    var status = "Perdido"
    
    // Filter options
    let sexOptions : [String] = ["Todos", "Macho", "Hembra"]
    let raceOptions : [String] = ["Todos", "Mestizo", "Husky", "Labrador", "Chihuahua", "Pastor Alemán", "Dálmata"]
    let colorOptions : [String] = ["Todos", "Amarillo", "Café", "Blanco", "Negro", "Gris"]
    let sizeOptions : [String] = ["Todos", "Pequeño", "Mediano", "Grande"]
    
    // Dropdown menus
    let dropDown = DropDown()
    
    // General database
    var dogsCollection : [Dog] = []
    // TODO: Create a copy to filter according to the properties
    // Collection to display on screen after filters:
    var filteredCollection : [Dog] = []
    
    // ---------------------------------------------------
    // ---------------- MOVE BETWEEN PAGES ---------------
    // ---------------------------------------------------
    @IBAction func changeScreen(_ sender: UIButton) {
        if sender == mapButton {
            performSegue(withIdentifier: "homeToMap", sender: nil)
        } else if sender == registerButton {
            performSegue(withIdentifier: "homeToForm", sender: nil)
        }
    }
    
    
    
    // ---------------------------------------------------
    // ------------------ VIEW DID LOAD ------------------
    // ---------------------------------------------------
    override func viewDidLoad() {
        currentStatusButton = self.perdidosButton
        // ----- Initialize button's format ------
        let buttonCollection : [UIButton] = [sexButton, raceButton, colorButton, sizeButton]
        for button in buttonCollection {
            initializeButtonFormat(button: button)
        }
        // ------- DATABASE ------
        // Connect to the database
        let db = Firestore.firestore()
        // Retrieve all dogs
        db.collection("dogs").getDocuments() { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            } else {
                for document in querySnapshot!.documents {
                    // for each dog create a dog object and add it to dogs collection
                    let newDog = Dog(dog: document.data())
                    self.dogsCollection.append(newDog)
                }
            }
            // Filter dogs with Perdido status and show those
            // Perdido status is the default
            for dog in self.dogsCollection {
                if (dog.state == "Perdido") {
                    self.filteredCollection.append(dog)
                }
            }
            // Update data after fetching and filter is done
            self.dogsCollectionView.reloadData()
            super.viewDidLoad()
        }

        //authenticateUserAndConfigureView()
    }
    
    // ---------------------------------------------------
    // ----------------- COLLECTION VIEW -----------------
    // ---------------------------------------------------
    
    // Set number of items in collectionView
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return filteredCollection.count
    }
    
    // Fill collection view with dogs data
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        // Modify template "dogCard"
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "dogCard", for: indexPath) as! DogCollectionViewCell
//        cell.dogImage.image = UIImage(named:  photos[indexPath.row])
        cell.dogImage.loadFrom(URLAddres: filteredCollection[indexPath.row].imageUrl ?? "")
        cell.nameLabel.text = filteredCollection[indexPath.row].name
        cell.nameLabel.adjustsFontSizeToFitWidth = true
        cell.nameLabel.minimumScaleFactor = 0.2
        cell.nameLabel.numberOfLines = 0
        cell.raceLabel.text = filteredCollection[indexPath.row].breed
        return cell
    }
    
    
    // ---------------------------------------------------
    // -------------- CHANGE DOGS STATUS -----------------
    // ---------------------------------------------------
    @IBAction func onClickStatus(_ sender: UIButton) {
        // Catch that you are clicking on a different button than your current Status
        if sender != currentStatusButton {
            currentStatusButton = sender
            if (status == "Perdido") {
                status = "Encontrado"
                avistadosButton.titleLabel?.font = UIFont.boldSystemFont(ofSize: 20)
                perdidosButton.titleLabel?.font = UIFont.systemFont(ofSize: 17)
            } else {
                status = "Perdido"
                perdidosButton.titleLabel?.font = UIFont.boldSystemFont(ofSize: 20)
                avistadosButton.titleLabel?.font = UIFont.systemFont(ofSize: 17)
            }
            self.applyFilters(status: self.status, sex: self.sexButton.currentTitle!, size: self.sizeButton.currentTitle!, race: self.raceButton.currentTitle!, color: self.colorButton.currentTitle!)
        }
    }
    
    
    
    // ---------------------------------------------------
    // ----------------- APPLY FILTERS -------------------
    // ---------------------------------------------------
    @IBAction func filterPressed(_ sender: UIButton) {
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
        dropDown.bottomOffset = CGPoint(x: 0, y: sender.frame.size.height) //6
        dropDown.show()
        dropDown.selectionAction = {
            [weak self] (index: Int, item: String) in
            guard let _ = self else { return }
            sender.setTitle(item, for: .normal)
            self!.applyFilters(status: self!.status, sex: self!.sexButton.currentTitle!, size: self!.sizeButton.currentTitle!, race: self!.raceButton.currentTitle!, color: self!.colorButton.currentTitle!)
        }
    }
    
    func applyFilters(status : String, sex : String, size : String, race : String, color : String) {
        filteredCollection = []
        for dog in dogsCollection {
            if (status == dog.state) {
                if (sex != "Todos") {
                    if (dog.sex != sex) {continue}
                }
                if (size != "Todos") {
                    if (dog.size != size) {continue}
                }
                if (color != "Todos") {
                    if (dog.color != color) {continue}
                }
                if (race != "Todos") {
                    if (dog.breed != race) {continue}
                }
                filteredCollection.append(dog)
            }
        }
        self.dogsCollectionView.reloadData()
    }
    
    
    // ---------------------------------------------------
    // ----------------- BUTTONS FORMAT ------------------
    // ---------------------------------------------------
    func initializeButtonFormat(button : UIButton) {
        button.backgroundColor = UIColor(red: 254.0/255.0, green: 250.0/255.0, blue: 224.0/255.0, alpha: 1.0)
        button.layer.cornerRadius = 10
        button.layer.borderWidth = 1
        button.layer.borderColor = CGColor(red: 1.0/255.0, green: 99.0/255.0, blue: 141.0/255.0, alpha: 1.0)
        button.titleLabel?.font = UIFont(name: "Helvetica Neue", size: 12)
        button.setTitle("Todos", for: .normal)
    }
    
    
    //Keep Login
//    func authenticateUserAndConfigureView() {
//
//        if Auth.auth().currentUser == nil{
//            DispatchQueue.main.sync {
//                let navController = UINavigationController(rootViewController: LoginViewController()); navController.navigationBar.barStyle = .black
//                self.present(navController, animated: true, completion: nil)
//            }
//        }
//    }
    
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}


// -------------------------------------------------------------
// ------------- LOAD IMAGES FROM THE INTERNET -----------------
// -------------------------------------------------------------

extension UIImageView {
    func loadFrom(URLAddres: String) {
        guard let url = URL(string: URLAddres) else {return}
        DispatchQueue.main.async { [weak self] in
            if let imageData = try? Data(contentsOf: url) {
                if let loadedImage = UIImage(data: imageData) {
                    self?.image = loadedImage
                } else {
                    self?.image = UIImage(named: "noPhoto")
                }
            } else {
                self?.image = UIImage(named: "noPhoto")
            }
        }

    }
    
}
