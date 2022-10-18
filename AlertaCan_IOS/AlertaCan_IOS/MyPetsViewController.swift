//
//  MyPetsViewController.swift
//  IOS_AlertaCan
//
//  Created by Carlos Vega on 10/18/22.
//

import UIKit

class MyPetsViewController: UIViewController, UITableViewDataSource {

    // Dog Manager
    var dogManager : DogManager? = nil
    
    // Collection to display on screen after filters:
    var filteredCollection : [Dog]? = nil
    var myPets: [Dog] = []
    var user: String = ""
    
    @IBOutlet weak var tableView: UITableView!
    
    // Nav buttons
    @IBOutlet weak var registerButton: UIButton!
    @IBOutlet weak var mapButton: UIButton!
    
    // Status buttons
    @IBOutlet weak var perdidosButton: UIButton!
    @IBOutlet weak var avistadosButton: UIButton!
    
    
    
    // ---------------------------------------------------
    // ---------------- MOVE BETWEEN PAGES ---------------
    // ---------------------------------------------------

    @IBAction func changeScreen(_ sender: UIButton) {
        if (sender == perdidosButton || sender == avistadosButton) {
            performSegue(withIdentifier: "myPetsToHome", sender: nil)
        } else if (sender == mapButton) {
            performSegue(withIdentifier: "myPetsToMap", sender: nil)
        } else if (sender == registerButton) {
            performSegue(withIdentifier: "myPetsToForm", sender: nil)
        }
        
    }
    
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if (segue.identifier == "myPetsToMap") {
            let destinationVC = segue.destination as! MapViewController
            destinationVC.dogManager = self.dogManager
            destinationVC.filteredCollection = self.filteredCollection
            destinationVC.user = self.user
        } else if (segue.identifier == "myPetsToForm") {
            let destinationVC = segue.destination as! FormViewController
            destinationVC.dogManager = self.dogManager
            destinationVC.filteredCollection = self.filteredCollection
            destinationVC.user = self.user
        } else if (segue.identifier == "myPetsToHome") {
            let destinationVC = segue.destination as! HomeViewController
            destinationVC.user = self.user
        }
    }
    
    
    // ---------------------------------------------------
    // ------------------ VIEW DID LOAD ------------------
    // ---------------------------------------------------
    override func viewDidLoad() {
        tableView.dataSource = self
        myPets = (dogManager?.getMyPets(user: self.user))!
        super.viewDidLoad()
    }
    
    
    // ---------------------------------------------------
    // ------------------ TABLE VIEW ---------------------
    // ---------------------------------------------------
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return myPets.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "dogCell", for: indexPath) as! DogTableViewCell
        cell.dogImage.loadFrom(URLAddres: myPets[indexPath.row].imageUrl ?? "")
        cell.dogImage.layer.cornerRadius = cell.dogImage.frame.width/8.5
        cell.dogImage.layer.masksToBounds = true
        cell.dogImage.layer.maskedCorners = [.layerMaxXMinYCorner, .layerMinXMinYCorner]
        cell.nameLabel.adjustsFontSizeToFitWidth = true
        cell.nameLabel.minimumScaleFactor = 0.2
        cell.nameLabel.numberOfLines = 0
        cell.raceLabel.text = myPets[indexPath.row].breed
        cell.nameLabel.text = myPets[indexPath.row].name
        return cell
    }
    
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}

