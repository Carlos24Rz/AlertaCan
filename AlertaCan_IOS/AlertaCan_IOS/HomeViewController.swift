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

    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Connect to the database
        let db = Firestore.firestore()
        // Retrieve all dogs
        db.collection("dogs").getDocuments() { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            } else {
                for document in querySnapshot!.documents {
                    print("\(document.documentID)")
                   
//                    print("\(document.documentID) => \(document.data())")
//                    print(document.data())
                    let dict = document.data()
                    let newDog = Dog(dog: dict)
                    print(newDog.getInfoCard())
                    print("-----------------")
                }
            }
        }

        
        
        //authenticateUserAndConfigureView()

        // Do any additional setup after loading the view.
    }
    
    // -----------COLLECTION VIEW -------
    
    // set of images
    var photos =  [
        "lost1",
        "lost2",
        "lost3",
        "lost4",
        "lost5",
        "lost6",
        "lost7",
        "lost8",
        "lost1",
        "lost2",
        "lost3",
        "lost4",
        "lost5",
        "lost6",
        "lost7",
        "lost8",
    ]
    
    
    
    // Set number of items in collectionView
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return photos.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "dogCard", for: indexPath) as! DogCollectionViewCell
        
        cell.dogImage.image = UIImage(named:  photos[indexPath.row])
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
