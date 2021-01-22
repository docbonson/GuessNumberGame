package com.skizzmaximus.android.guessnumbergame

import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModel

class NumGuessGameViewModel: ViewModel() {

    var guessIndex = 10
    var winIndex = 0
    var lossIndex = 0
    var totalIndex = 0
    // var randomNumber = (1..50).random()
    var randomNumber: Int = newNumber()

    //random number function
    fun newNumber():Int {
        return (1..50).random()
    }


}