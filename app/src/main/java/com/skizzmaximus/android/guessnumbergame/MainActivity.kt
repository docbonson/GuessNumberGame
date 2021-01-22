package com.skizzmaximus.android.guessnumbergame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private val guessGameViewModel: NumGuessGameViewModel by lazy {
        ViewModelProviders.of(this).get(NumGuessGameViewModel::class.java)
    }

    //Text Views
    private lateinit var winLossNotifier: TextView
    private lateinit var realNumber: TextView
    private lateinit var totalGames: TextView
    private lateinit var winTotal: TextView
    private lateinit var lossTotal: TextView
    private lateinit var numberOfGuesses: TextView
    private lateinit var warning:TextView

    //edit text
    private lateinit var userNumber: EditText

    //Booleans
    var gameIsRunning: Boolean = true
    var gaveUp: Boolean = false

    //Buttons
    private lateinit var guess: Button
    private lateinit var giveUp: Button
    private lateinit var tryAgain: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val provider: ViewModelProvider = ViewModelProviders.of(this)
        val guessGameViewModel = provider.get(NumGuessGameViewModel::class.java)

        //Widgets Buttons
        guess = findViewById(R.id.btnGuess)
        giveUp = findViewById(R.id.btnGiveUp)
        tryAgain = findViewById(R.id.tryAgain)

        //Widgets textViews
        winLossNotifier = findViewById(R.id.win_lose)
        realNumber = findViewById(R.id.realNumber)
        totalGames = findViewById(R.id.num_totalGames)
        winTotal = findViewById(R.id.num_gamesWon)
        lossTotal = findViewById(R.id.num_gamesLost)
        numberOfGuesses = findViewById(R.id.tv_numRemainingGuesses)
        userNumber = findViewById(R.id.edtNumber)
        warning = findViewById(R.id.warning)

        if(savedInstanceState != null) {
            warning.text = savedInstanceState.getCharSequence("key_warning", "")
            winLossNotifier.text = savedInstanceState.getCharSequence("key_win_lose", "")
            realNumber.text = savedInstanceState.getCharSequence("key_real_num", "")
            gameIsRunning = savedInstanceState.getBoolean("key_start", true)
            gaveUp = savedInstanceState.getBoolean("key_gaveUp", false)
        }

        guess.setOnClickListener {
            if (gameIsRunning) {
                guess()
            }
        }

        giveUp.setOnClickListener {
            gaveUp = true
            guess()
        }

        tryAgain.setOnClickListener {
            restart()
        }

        draw()
    }

    private fun guess() {
        //check parameters
        when {
            //check if userNumber is empty
            TextUtils.isEmpty(userNumber.text.toString()) -> warning.setText(R.string.not_right)
            //If user number is over or below the parameters
            userNumber.text.toString().toInt() < 1 -> warning.setText(R.string.not_right)
            userNumber.text.toString().toInt() > 50 -> warning.setText(R.string.not_right)
            //If userNumber is higher or lower than random number
            userNumber.text.toString().toInt() > guessGameViewModel.randomNumber -> warning.setText(R.string.high)
            userNumber.text.toString().toInt() < guessGameViewModel.randomNumber -> warning.setText(R.string.low)
            //If user wins game
            userNumber.text.toString().toInt() == guessGameViewModel.randomNumber -> {
                winLossNotifier.setText(R.string.winner)
                realNumber.setText(guessGameViewModel.randomNumber.toString())
                guessGameViewModel.winIndex++
                winTotal.setText(guessGameViewModel.winIndex.toString())
                gameOver()
            }
        }

        //user guess counter
        //checking for int, still could be empty. Create a boolean.
        if(TextUtils.isEmpty(userNumber.text.toString())) {
            warning.setText(R.string.not_right)
        }else if (userNumber.text.toString().toInt() != guessGameViewModel.randomNumber) {
            guessGameViewModel.guessIndex--
            numberOfGuesses.setText(guessGameViewModel.guessIndex.toString())
        }

        //lose game
        if (guessGameViewModel.guessIndex <= 0 || gaveUp) {
            winLossNotifier.setText(R.string.loser)
            realNumber.setText(guessGameViewModel.randomNumber.toString())
            guessGameViewModel.lossIndex++
            lossTotal.setText(guessGameViewModel.lossIndex.toString())
            gameOver()
        }
        draw()
    }

    //Game over screen
    private fun gameOver() {
        guessGameViewModel.totalIndex++
        totalGames.setText(guessGameViewModel.totalIndex.toString())
        warning.setText("")
        gameIsRunning = false
        draw()
    }

    //restart the game
    private fun restart() {
        gameIsRunning = true
        guessGameViewModel.guessIndex = 10
        numberOfGuesses.setText(guessGameViewModel.guessIndex.toString())
        winLossNotifier.setText("")
        warning.setText("")
        userNumber.setText("")
        realNumber.setText("")
        guessGameViewModel.randomNumber = guessGameViewModel.newNumber()
        draw()
    }

    private fun draw() {
        totalGames.setText(guessGameViewModel.totalIndex.toString())
        winTotal.setText(guessGameViewModel.winIndex.toString())
        lossTotal.setText(guessGameViewModel.lossIndex.toString())
        numberOfGuesses.setText(guessGameViewModel.guessIndex.toString())
        //set number of guesses
        numberOfGuesses.setText(guessGameViewModel.guessIndex.toString())

        gaveUp = false

        //set button visibility
        if(gameIsRunning) {
            tryAgain.visibility = View.GONE
            giveUp.visibility = View.VISIBLE
            guess.visibility = View.VISIBLE
        } else {
            tryAgain.visibility = View.VISIBLE
            giveUp.visibility = View.GONE
            guess.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState?.putCharSequence("key_warning", warning.text)
        outState?.putCharSequence("key_win_lose", winLossNotifier.text)
        outState?.putCharSequence("key_real_num", realNumber.text)
        outState?.putBoolean("key_start", gameIsRunning)
        outState?.putBoolean("key_gaveUp", gaveUp)
    }


}