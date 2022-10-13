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
    
}
