//
//  InfoOwnerViewController.swift
//  IOS_AlertaCan
//
//  Created by Carlos Vega on 10/19/22.
//

import UIKit

class InfoOwnerViewController: UIViewController {

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
        }
    }
    
    
    // ---------------------------------------------------
    // ---------------- VIEW DID LOAD ---------------
    // ---------------------------------------------------
    
    override func viewDidLoad() {
        print(dog ?? "not dog")
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }


}
