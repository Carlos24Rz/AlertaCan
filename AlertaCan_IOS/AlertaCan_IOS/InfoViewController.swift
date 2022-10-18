//
//  InfoViewController.swift
//  IOS_AlertaCan
//
//  Created by Josué Taboada Elvira on 13/10/22.
//

import UIKit

class InfoViewController: UIViewController {

    // Outlets
    @IBOutlet weak var statusLabel: UILabel!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var raceLabel: UILabel!
    @IBOutlet weak var sexLabel: UILabel!
    @IBOutlet weak var sizeLabel: UILabel!
    @IBOutlet weak var colorLabel: UILabel!
    @IBOutlet weak var lastSeenLabel: UILabel!
    @IBOutlet weak var descriptionLabel: UILabel!
    @IBOutlet weak var numberLabel: UILabel!
    
    
    // Dog Manager
    var dogManager = DogManager()

    // User info
    var user : String? = nil
    
    // Collection to display on screen after filters:
    var filteredCollection : [Dog] = []
    
    // Dog info
    var dog : Dog? = nil
    
    
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
        
        
        
        
        // Y así con todos los labels...
    }
}
