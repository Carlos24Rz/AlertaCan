//
//  HomeViewController.swift
//  IOS_AlertaCan
//
//  Created by Carlos Vega on 10/10/22.
//

import UIKit

class HomeViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource {

    override func viewDidLoad() {
        super.viewDidLoad()

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
    
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
