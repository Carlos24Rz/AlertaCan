//
//  Dog.swift
//  IOS_AlertaCan
//
//  Created by Carlos Vega on 10/12/22.
//

import Foundation

struct Dog {
    var breed : String?
    var color : String?
    var date_missing : String?
    var date_registration : String?
    var description : String?
    var imageUrl : String?
    var last_time_location : String?
    var name : String?
    var owner_phone : String?
    var sex : String?
    var size : String?
    var state : String?
    var user : String?

    init(dog : Dictionary<String, Any>) {
        breed = dog["breed"] as? String ?? ""
        color = dog["color"] as? String ?? ""
        date_missing = dog["date_missing"] as? String ?? ""
        date_registration = dog["date_registration"] as? String ?? ""
        description = dog["description"] as? String ?? ""
        imageUrl = dog["imageUrl"] as? String ?? ""
        last_time_location = dog["last_time_location"] as? String ?? ""
        name = dog["name"] as? String ?? ""
        owner_phone = dog["owner_phone"] as? String ?? ""
        sex = dog["sex"] as? String ?? ""
        size = dog["size"] as? String ?? ""
        state = dog["state"] as? String ?? ""
        user = dog["user"] as? String ?? ""
    }
    
//    init(_ date_missing : String, _ description : String, _ state : String, _ last_time_location : String, _ imageUrl : String, _ color : String, _ size : String, _ name : String, _ user : String, _ breed : String, _ date_registration : String, _ sex : String) {
//        self.date_missing = date_missing
//        self.description = description
//        self.state = state
//        self.last_time_location = last_time_location
//        self.imageUrl = imageUrl
//        self.color = color
//        self.size = size
//        self.name = name
//        self.user = user
//        self.breed = breed
//        self.date_registration = date_missing
//        self.sex = sex
//    }
    
    func getInfoCard() -> [String] {
        let infoCard = [imageUrl!, name!, breed!]
        return infoCard
    }
}
