//
//  Response.swift
//  IOS_AlertaCan
//
//  Created by Carlos Vega on 10/16/22.
//

import DeepCodable
import Foundation

struct Response : DeepCodable {
    static let codingTree = CodingTree {
        Key("result") {
            Key("geometry") {
                Key("location", containing: \._location)
            }
        }
    }
    /*
    Also valid is the flattened form:
    static let codingTree = CodingTree {
        Key("topLevel", "secondLevel", "thirdLevel", containing: \._property)
    }
    */

    @Value var location: [String : Double]
}

