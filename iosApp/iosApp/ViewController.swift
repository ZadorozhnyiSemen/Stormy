//
//  ViewController.swift
//  iosApp
//
//  Created by user on 12/23/19.
//  Copyright © 2019 Semyon. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        Forecast.forecast.watch { forecast in
            guard let temp = forecast?.currently?.temperature else {
                return
            }
            print(temp)
        }
    }
}

