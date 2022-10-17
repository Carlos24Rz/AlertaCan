//
//  MapViewController.swift
//  IOS_AlertaCan
//
//  Created by Carlos Vega on 10/16/22.
//

import UIKit
import DropDown

class MapViewController: UIViewController {
    
    // Nav buttons
    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var homeButton: UIButton!
    @IBOutlet weak var registerButton: UIButton!
    
    // Filter buttons
    @IBOutlet weak var sexButton: UIButton!
    @IBOutlet weak var raceButton: UIButton!
    @IBOutlet weak var colorButton: UIButton!
    @IBOutlet weak var sizeButton: UIButton!
    
    // Filter options
    let sexOptions : [String] = ["Todos", "Macho", "Hembra"]
    let raceOptions : [String] = ["Todos", "Mestizo", "Husky", "Labrador", "Chihuahua", "Pastor Alemán", "Dálmata"]
    let colorOptions : [String] = ["Todos", "Amarillo", "Café", "Blanco", "Negro", "Gris"]
    let sizeOptions : [String] = ["Todos", "Pequeño", "Mediano", "Grande"]
    
    // Dropdown menus
    let dropDown = DropDown()
    
    // Dog Manager
    var dogManager : DogManager? = nil
    
    // Collection to display on screen after filters:
    var filteredCollection : [Dog]? = nil
    
    
    // ---------------------------------------------------
    // ---------------- MOVE BETWEEN PAGES ---------------
    // ---------------------------------------------------
    @IBAction func changeScreen(_ sender: UIButton) {
        if (sender == registerButton) {
            performSegue(withIdentifier: "mapToForm", sender: nil)
        } else {
            performSegue(withIdentifier: "mapToHome", sender: nil)
        }
    }
    
    
    
    // ---------------------------------------------------
    // ------------------ VIEW DID LOAD ------------------
    // ---------------------------------------------------
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let buttonCollection : [UIButton] = [sexButton, raceButton, colorButton, sizeButton]
        for button in buttonCollection {
            initializeButtonFormat(button: button)
        }
        print(filteredCollection!)
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
}
