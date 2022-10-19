//
//  InfoViewController.swift
//  IOS_AlertaCan
//
//  Created by Josué Taboada Elvira on 13/10/22.
//

import UIKit

class InfoViewController: UIViewController {

    // Outlets labels
    @IBOutlet weak var statusLabel: UILabel!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var raceLabel: UILabel!
    @IBOutlet weak var sexLabel: UILabel!
    @IBOutlet weak var sizeLabel: UILabel!
    @IBOutlet weak var colorLabel: UILabel!
    @IBOutlet weak var lastSeenLabel: UILabel!
    @IBOutlet weak var descriptionLabel: UILabel!
    @IBOutlet weak var dogImage: UIImageView!
    @IBOutlet weak var numberLabel: UILabel!
    
    // Outlets Nav
    @IBOutlet weak var mapButton: UIButton!
    @IBOutlet weak var homeButton: UIButton!
    @IBOutlet weak var registerButton: UIButton!
    
    
    // Dog Manager
    var dogManager = DogManager()

    // User info
    var user : String? = nil
    
    // Collection to display on screen after filters:
    var filteredCollection : [Dog] = []
    
    // Dog info
    var dog : Dog? = nil
    
    // ---------------------------------------------------
    // ---------------- MOVE BETWEEN PAGES ---------------
    // ---------------------------------------------------

    @IBAction func changeScreen(_ sender: UIButton) {
        if sender == mapButton {
            performSegue(withIdentifier: "infoToMap", sender: nil)
        } else if sender == registerButton {
            performSegue(withIdentifier: "infoToForm", sender: nil)
        } else if (sender == homeButton) {
            performSegue(withIdentifier: "infoToHome", sender: nil)
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if (segue.identifier == "infoToMap") {
            let destinationVC = segue.destination as! MapViewController
            destinationVC.dogManager = self.dogManager
            destinationVC.filteredCollection = self.filteredCollection
            destinationVC.user = self.user
        } else if (segue.identifier == "infoToForm") {
            let destinationVC = segue.destination as! FormViewController
            destinationVC.dogManager = self.dogManager
            destinationVC.filteredCollection = self.filteredCollection
            destinationVC.user = self.user
        } else if (segue.identifier == "infoToHome") {
            let destinationVC = segue.destination as! HomeViewController
            destinationVC.dogManager = self.dogManager
            destinationVC.filteredCollection = self.filteredCollection
            destinationVC.user = self.user!
        }
    }
    
    
    // ---------------------------------------------------
    // ------------------ VIEW DID LOAD ------------------
    // ---------------------------------------------------
    override func viewDidLoad() {
        super.viewDidLoad()
        print(dog ?? "Not dog found")
        // Do any additional setup after loading the view.
        initializeDogInfo(dog!)
    }
    
    func initializeDogInfo(_ dog : Dog) {
        statusLabel.text = dog.state
        nameLabel.text = dog.name
        raceLabel.text = dog.breed
        sexLabel.text = dog.sex
        sizeLabel.text = dog.size
        colorLabel.text = dog.color
        lastSeenLabel.text = dog.last_time_location
        descriptionLabel.text = dog.description
        numberLabel.text = dog.owner_phone
        dogImage.loadFrom(URLAddres: dog.imageUrl ?? "")
        dogImage.layer.cornerRadius = dogImage.frame.width/8.5
        }
}
