//
//  DogMatchTableViewCell.swift
//  IOS_AlertaCan
//
//  Created by Carlos Vega on 10/19/22.
//

import UIKit

class DogMatchTableViewCell: UITableViewCell {

    @IBOutlet weak var dogButton: UIButton!
    @IBOutlet weak var dogImage: UIImageView!
    @IBOutlet weak var raceLabel: UILabel!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var matchTag: UIButton!
    
    override func layoutSubviews() {
        super.layoutSubviews()

        contentView.frame = contentView.frame.inset(by: UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 10))
    }
}
