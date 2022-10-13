//
//  HomeViewController.swift
//  IOS_AlertaCan
//
//  Created by Carlos Vega on 10/10/22.
//

import UIKit
import FirebaseAuth
import FirebaseCore
import Firebase
import FirebaseFirestore

class HomeViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource {

    @IBOutlet weak var dogsCollectionView: UICollectionView!
    
    // General database
    var dogsCollection : [Dog] = []
    // TODO: Create a copy to filter according to the properties
    // Collection to display on screen after filters:
    var filteredCollection : [Dog] = []
    
    // ---------------------------------------------------
    // ------------------ VIEW DID LOAD ------------------
    // ---------------------------------------------------
    override func viewDidLoad() {
        // Connect to the database
        let db = Firestore.firestore()
        // Retrieve all dogs
        db.collection("dogs").getDocuments() { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            } else {
                for document in querySnapshot!.documents {
                    // for each dog create a dog object and add it to dogs collection
                    let newDog = Dog(dog: document.data())
                    self.dogsCollection.append(newDog)
                }
            }
            // Filter dogs with Perdido status and show those
            // Perdido status is the default
            for dog in self.dogsCollection {
                if (dog.state == "Perdido") {
                    self.filteredCollection.append(dog)
                }
            }
            // Update data after fetching and filter is done
            self.dogsCollectionView.reloadData()
            super.viewDidLoad()
        }

        //authenticateUserAndConfigureView()
    }
    
    // ---------------------------------------------------
    // ----------------- COLLECTION VIEW -----------------
    // ---------------------------------------------------
    
    // Set number of items in collectionView
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return filteredCollection.count
    }
    
    // Fill collection view with dogs data
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        // Modify template "dogCard"
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "dogCard", for: indexPath) as! DogCollectionViewCell
//        cell.dogImage.image = UIImage(named:  photos[indexPath.row])
        cell.dogImage.loadFrom(URLAddres: filteredCollection[indexPath.row].imageUrl ?? "")
        cell.nameLabel.text = filteredCollection[indexPath.row].name
        cell.nameLabel.adjustsFontSizeToFitWidth = true
        cell.nameLabel.minimumScaleFactor = 0.2
        cell.nameLabel.numberOfLines = 0
        cell.raceLabel.text = filteredCollection[indexPath.row].breed
        return cell
    }
    
    //Keep Login
//    func authenticateUserAndConfigureView() {
//
//        if Auth.auth().currentUser == nil{
//            DispatchQueue.main.sync {
//                let navController = UINavigationController(rootViewController: LoginViewController()); navController.navigationBar.barStyle = .black
//                self.present(navController, animated: true, completion: nil)
//            }
//        }
//    }
    
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}


// -------------------------------------------------------------
// ------------- LOAD IMAGES FROM THE INTERNET -----------------
// -------------------------------------------------------------

extension UIImageView {
    func loadFrom(URLAddres: String) {
        guard let url = URL(string: URLAddres) else {return}
        DispatchQueue.main.async { [weak self] in
            if let imageData = try? Data(contentsOf: url) {
                if let loadedImage = UIImage(data: imageData) {
                    self?.image = loadedImage
                } else {
                    self?.image = UIImage(named: "noPhoto")
                }
            } else {
                self?.image = UIImage(named: "noPhoto")
            }
        }

    }
    
}
