//
//  MapViewController.swift
//  IOS_AlertaCan
//
//  Created by Carlos Vega on 10/16/22.
//

import UIKit
import MapKit
import DropDown

class MapViewController: UIViewController, MKMapViewDelegate, CLLocationManagerDelegate {
    
    // Nav buttons
    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var homeButton: UIButton!
    @IBOutlet weak var registerButton: UIButton!
    
    // Filter buttons
    @IBOutlet weak var sexButton: UIButton!
    @IBOutlet weak var raceButton: UIButton!
    @IBOutlet weak var colorButton: UIButton!
    @IBOutlet weak var sizeButton: UIButton!
    
    // Map
    @IBOutlet weak var map: MKMapView!
    
    // Filter options
    let sexOptions : [String] = ["Todos", "Macho", "Hembra"]
    let raceOptions : [String] = ["Todos", "Golder retriever", "Mestizo", "Husky", "Labrador", "Chihuahua", "Pastor alemán", "Dálmata", "Schnauzer", "Pastor belga", "Beagle"]
    let colorOptions : [String] = ["Todos", "Amarillo", "Café", "Blanco", "Negro", "Gris"]
    let sizeOptions : [String] = ["Todos", "Pequeño", "Mediano", "Grande"]
    
    // Dropdown menus
    let dropDown = DropDown()
    
    // Dog Manager
    var dogManager : DogManager? = nil
    
    // Collection to display on screen after filters:
    var filteredCollection : [Dog]? = nil
    
    // Location Manager
    var locationManager = CLLocationManager()
    
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
    // --------------------- FILTERS ---------------------
    // ---------------------------------------------------
    @IBAction func pressedFilter(_ sender: UIButton) {
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
        dropDown.direction = .top
        dropDown.topOffset = CGPoint(x: 0, y:-(dropDown.anchorView?.plainView.bounds.height)!)
        dropDown.show()
        dropDown.selectionAction = {
            [weak self] (index: Int, item: String) in
            guard let _ = self else { return }
            sender.setTitle(item, for: .normal)
            self!.applyFilters(status: "All", sex: self!.sexButton.currentTitle!, size: self!.sizeButton.currentTitle!, race: self!.raceButton.currentTitle!, color: self!.colorButton.currentTitle!)
        }
    }
    func applyFilters(status : String, sex : String, size : String, race : String, color : String) {
        // Remove existing filters
        let allAnnotations = self.map.annotations
        self.map.removeAnnotations(allAnnotations)
        // Change filters
        dogManager!.changeFilter(key: "status", value: status)
        dogManager!.changeFilter(key: "sex", value: sex)
        dogManager!.changeFilter(key: "size", value: size)
        dogManager!.changeFilter(key: "race", value: race)
        dogManager!.changeFilter(key: "color", value: color)
        dogManager!.applyFilters()
        filteredCollection = dogManager!.getCollection()
        print("New filters:")
        for dog in filteredCollection! {
            print(dog.name!)
            getCoordinates(placeID: dog.placeID!)
        }
    }
    
    
    // ---------------------------------------------------
    // ------------------ VIEW DID LOAD ------------------
    // ---------------------------------------------------
    override func viewDidLoad() {
        super.viewDidLoad()
        map.delegate = self
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.requestWhenInUseAuthorization()
        locationManager.startUpdatingLocation()
        let buttonCollection : [UIButton] = [sexButton, raceButton, colorButton, sizeButton]
        for button in buttonCollection {
            initializeButtonFormat(button: button)
        }
        dogManager!.changeFilter(key: "status", value: "All")
        dogManager!.applyFilters()
        filteredCollection = dogManager!.getCollection()
        for dog in filteredCollection! {
            getCoordinates(placeID: dog.placeID!)
        }
        dismiss(animated: false, completion: nil)
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
    
    // ---------------------------------------------------
    // ----------------- INITIALIZE MAP ------------------
    // ---------------------------------------------------
    
    // Find user's location
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        // The manager starts storing them in the array locations and once the first one is found, it stops
        if let location = locations.first {
            locationManager.stopUpdatingLocation()
            render(location)
        }
    }
    
    
    // ------ Render at user's location ------
    func render(_ location: CLLocation) {
        // Get the coordinates according to the location
        let coordinate = CLLocationCoordinate2D(latitude: location.coordinate.latitude, longitude: location.coordinate.longitude)
        // By how much zoom the span will render it, the smaller the more zoom
        let span = MKCoordinateSpan(latitudeDelta: 0.50, longitudeDelta: 0.50)
        // Creating the region that will be displayed
        let region = MKCoordinateRegion(center: coordinate, span: span)
        
        map.setRegion(region, animated: true)

    }
    
    // Create a PIN
    func createPIN(latitude : Double, longitude : Double) {
        let coordinate = CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
        // Create the object
        let pin = MKPointAnnotation()
        // Assign its coordinates
        pin.coordinate = coordinate
        // Add it to the mapView
        map.addAnnotation(pin)
    }
    
    func getCoordinates(placeID : String) {
        let googleURL = URL(string: "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + placeID + "&key=AIzaSyAc9EcgUw9i8qLE7nHtxhBj6C6rbAT7Uo0")!
        
        let task = URLSession.shared.dataTask(with: googleURL) {(data, response, error) in
            guard let data = data else { return }
            do {
                let res = try! JSONDecoder().decode(Response.self, from: data)
                self.createPIN(latitude: res.location["lat"]!, longitude: res.location["lng"]!)
            }
        }
        
        task.resume()
    }
    
    
    func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
        if (annotation is MKUserLocation) {return nil}
        
        var annotationView = mapView.dequeueReusableAnnotationView(withIdentifier: "custom")
        
        if (annotationView == nil) {
            annotationView = MKAnnotationView(annotation: annotation, reuseIdentifier: "custom")
        } else {
            annotationView?.annotation = annotation
        }
        
        annotationView?.image = UIImage(named: "On_home")
        return annotationView
    }
    
}
