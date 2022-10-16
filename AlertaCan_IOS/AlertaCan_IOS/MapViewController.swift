//
//  MapViewController.swift
//  IOS_AlertaCan
//
//  Created by Carlos Vega on 10/16/22.
//

import UIKit

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
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */
    
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
