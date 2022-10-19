//
//  DogManager.swift
//  IOS_AlertaCan
//
//  Created by Carlos Vega on 10/16/22.
//

import Foundation
import FirebaseCore
import Firebase
import FirebaseFirestore

class DogManager {
    
    init() {}
    
    var dogsCollection : [Dog] = []
    var filteredCollection : [Dog] = []
    var filteredValues : [String : String] = [
        "status" : "Perdido",
        "sex" : "Todos",
        "size" : "Todos",
        "race" : "Todos",
        "color" : "Todos"
    ]
    
    
    // ---------------------------------------------------
    // ------------------- FETCH DATA --------------------
    // ---------------------------------------------------
    func fetchFromDatabase(completion: @escaping () -> Void) {
        dogsCollection = []
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
                self.filteredCollection.append(dog)
            }
            completion()
        }
    }

    
    
    // ---------------------------------------------------
    // --------------------- FILTERS ---------------------
    // ---------------------------------------------------

    func applyFilters() {
        filteredCollection = []
        for dog in dogsCollection {
            if (filteredValues["status"] == dog.state || filteredValues["status"] == "All") {
                if (filteredValues["sex"] != "Todos") {
                    if (dog.sex != filteredValues["sex"]) {continue}
                }
                if (filteredValues["size"] != "Todos") {
                    if (dog.size != filteredValues["size"]) {continue}
                }
                if (filteredValues["color"] != "Todos") {
                    if (dog.color != filteredValues["color"]) {continue}
                }
                if (filteredValues["race"] != "Todos") {
                    if (dog.breed != filteredValues["race"]) {continue}
                }
                filteredCollection.append(dog)
            }
        }
    }
    
    func changeFilter(key : String, value : String) {
        filteredValues[key] = value
    }
    
    func restartFilters() {
        filteredValues = [
            "status" : "Perdido",
            "sex" : "Todos",
            "size" : "Todos",
            "race" : "Todos",
            "color" : "Todos"
        ]
    }
    
    // ---------------------------------------------------
    // --------------------- GETTERS ---------------------
    // ---------------------------------------------------
    
    func getAllCollection() -> [Dog] {
        return dogsCollection
    }
    
    func getCollection() -> [Dog] {
        return filteredCollection
    }
    
    func getFilterValues() -> [String : String] {
        return filteredValues
    }
    
    func getMyPets(user: String) -> [Dog] {
        var result : [Dog] = []
        for dog in dogsCollection {
            if dog.user == user {
                result.append(dog)
            }
        }
        return result
    }
    
    func getIndex(name: String) -> Int {
        return filteredCollection.firstIndex(where: {$0.name == name}) ?? 0
    }
    
}
