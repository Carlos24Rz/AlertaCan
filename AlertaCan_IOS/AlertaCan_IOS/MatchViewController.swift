//
//  MatchViewController.swift
//  IOS_AlertaCan
//
//  Created by Carlos Vega on 10/19/22.
//

import UIKit

class MatchViewController: UIViewController, UITableViewDataSource {

    var matchingDogs : [Dog] = []
    
    @IBOutlet weak var originalDog: UIImageView!
    // ---------------------------------------------------
    // -------------- TRANSITION VARIABLES ---------------
    // ---------------------------------------------------
    
    // Dog Manager
    var dogManager = DogManager()

    // User info
    var user : String? = nil
    
    // Collection to display on screen after filters:
    var filteredCollection : [Dog] = []
    
    // Dog info
    var dog : Dog? = nil
    
    
    // ---------------------------------------------------
    // ------------------ VIEW DID LOAD ------------------
    // ---------------------------------------------------
    override func viewDidLoad() {
        super.viewDidLoad()
        originalDog.loadFrom(URLAddres: dog?.imageUrl ?? "")
        matchingDogs = dogManager.getSimilarDogs(givenDog: dog!)
        // Do any additional setup after loading the view.
    }
    
    
    
    // ---------------------------------------------------
    // ------------------ TABLE VIEW ---------------------
    // ---------------------------------------------------
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return matchingDogs.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let points = matchingDogs[indexPath.row].getMatchingScore(dog: dog!)
        let cell = tableView.dequeueReusableCell(withIdentifier: "dogCell", for: indexPath) as! DogMatchTableViewCell
        cell.dogImage.loadFrom(URLAddres: matchingDogs[indexPath.row].imageUrl ?? "")
        cell.dogImage.layer.cornerRadius = cell.dogImage.frame.width/15.0
        cell.dogImage.layer.masksToBounds = true
        cell.dogImage.layer.maskedCorners = [.layerMaxXMinYCorner, .layerMinXMinYCorner]
        cell.nameLabel.adjustsFontSizeToFitWidth = true
        cell.nameLabel.minimumScaleFactor = 0.2
        cell.nameLabel.numberOfLines = 0
        cell.raceLabel.text = matchingDogs[indexPath.row].breed
        cell.nameLabel.text = matchingDogs[indexPath.row].name
        cell.dogButton.setTitle(String(indexPath.row), for: .normal)
        cell.matchTag.setTitle("\(points*100/4)%", for: .normal)
        return cell
    }
    

}
