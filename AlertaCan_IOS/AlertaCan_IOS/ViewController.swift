//
//  ViewController.swift
//  AlertaCan_IOS
//
//  Created by Carlos Ruiz on 26/08/22.
//

import CoreLocation
import MapKit
import UIKit

// Include CLLocationManagerDelegate to allow manager.delegatte in viewDidAppear
class ViewController: UIViewController, CLLocationManagerDelegate {

    // ------------ GO TO HOME ------------
    // Using the navIcon
    @IBAction func HomeIconPressed(_ sender: UIButton) {
        performSegue(withIdentifier: "mapToHome", sender: nil)
    }
    // Using the back button
    @IBAction func BackButtonPressed(_ sender: UIButton) {
        performSegue(withIdentifier: "mapToHome", sender: nil)
    }
    
    
    
    // Map Kit Map View to create it
    // By itself only shows current location
    @IBOutlet var mapView: MKMapView!
    
    let manager = CLLocationManager()
    
    // Add a info.plist for the permission of current location
    // Add a privacy
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        // Implications on battery
        manager.desiredAccuracy = kCLLocationAccuracyBest
        
        manager.delegate = self
        // Request permissions from Info.plist
        manager.requestWhenInUseAuthorization()
        
        manager.startUpdatingLocation()
    }

    // It is called when manager.startUpdatingLocation()
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        // The manager starts storing them in the array locations and once the first one is found, it stops
        if let location = locations.first {
            manager.stopUpdatingLocation()
            render(location)
        }
    }
    
    // Function to zoom in to the location and create a marker
    func render(_ location: CLLocation) {
        
        // ------ CREATE MAP ------
        // Get the coordinates according to the location
        let coordinate = CLLocationCoordinate2D(latitude: location.coordinate.latitude, longitude: location.coordinate.longitude)
        // By how much zoom the span will render it
        let span = MKCoordinateSpan(latitudeDelta: 0.01, longitudeDelta: 0.01)
        // Creating the region that will be displayed
        let region = MKCoordinateRegion(center: coordinate, span: span)
        
        mapView.setRegion(region, animated: true)
        
        
        // ------ CREATE PIN ------
        // Create the object
        let pin = MKPointAnnotation()
        // Assign its coordinates
        pin.coordinate = coordinate
        // Add it to the mapView
        mapView.addAnnotation(pin)
    }

}


