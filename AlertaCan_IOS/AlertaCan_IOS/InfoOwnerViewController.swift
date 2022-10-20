//
//  InfoOwnerViewController.swift
//  IOS_AlertaCan
//
//  Created by Carlos Vega on 10/19/22.
//

import UIKit

class InfoOwnerViewController: UIViewController {

    
    @IBOutlet weak var mapButton: UIButton!
    @IBOutlet weak var registerButton: UIButton!
    @IBOutlet weak var homeButton: UIButton!
    
    // Dog Manager
    var dogManager = DogManager()

    // User info
    var user : String? = nil
    
    // Collection to display on screen after filters:
    var filteredCollection : [Dog] = []
    
    // Dog info
    var dog : Dog? = nil
    
    @IBOutlet weak var statusLabel: UILabel!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var raceLabel: UILabel!
    @IBOutlet weak var sexLabel: UILabel!
    @IBOutlet weak var colorLabel: UILabel!
    @IBOutlet weak var sizeLabel: UILabel!
    @IBOutlet weak var numberLabel: UILabel!
    @IBOutlet weak var descriptionLabel: UILabel!
    @IBOutlet weak var lastSeenLabel: UILabel!
    @IBOutlet weak var dogImage: UIImageView!
    
    // ---------------------------------------------------
    // ---------------- MOVE BETWEEN PAGES ---------------
    // ---------------------------------------------------
    
    @IBAction func changeScreen(_ sender: UIButton) {
        if (sender == mapButton) {
            performSegue(withIdentifier: "infoOwnerToMap", sender: nil)
        } else if (sender == homeButton) {
            performSegue(withIdentifier: "infoOwnerToHome", sender: nil)
        } else if (sender == registerButton) {
            performSegue(withIdentifier: "infoOwnerToForm", sender: nil)
        }
    }
    
    
    
    @IBAction func seeMatches(_ sender: UIButton) {
        performSegue(withIdentifier: "infoOwnerToMatch", sender: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if (segue.identifier == "infoOwnerToMatch") {
            let destinationVC = segue.destination as! MatchViewController
            destinationVC.dogManager = self.dogManager
            destinationVC.filteredCollection = self.filteredCollection
            destinationVC.user = self.user
            destinationVC.dog = self.dog
        } else if (segue.identifier == "infoOwnerToMap") {
                let destinationVC = segue.destination as! MapViewController
                destinationVC.dogManager = self.dogManager
                destinationVC.filteredCollection = self.filteredCollection
                destinationVC.user = self.user
        } else if (segue.identifier == "infoOwnerToForm") {
            let destinationVC = segue.destination as! FormViewController
            destinationVC.dogManager = self.dogManager
            destinationVC.filteredCollection = self.filteredCollection
            destinationVC.user = self.user
        } else if (segue.identifier == "infoOwnerToHome") {
            let destinationVC = segue.destination as! HomeViewController
            destinationVC.user = self.user
        }
    }
    
    
    // ---------------------------------------------------
    // ---------------- VIEW DID LOAD ---------------
    // ---------------------------------------------------
    
    override func viewDidLoad() {
        print(dog ?? "not dog")
        super.viewDidLoad()
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
