package com.cuupa.mailprocessor.services.files.util

import java.io.Serializable

class DPI : Serializable {

    val x: Float
    val y: Float

    constructor(x: Float, y: Float) {
        this.x = if(x >1200F){
            1200F
        } else {
            x
        }

        this.y = if(y >1200F){
            1200F
        } else {
            y
        }
    }
}
