package com.example.rockpaperscissorsgame

import androidx.lifecycle.ViewModel
import com.example.rockpaperscissorsgame.RockPaperScissorsViewModel.GamePieces.Paper
import com.example.rockpaperscissorsgame.RockPaperScissorsViewModel.GamePieces.Rock
import com.example.rockpaperscissorsgame.RockPaperScissorsViewModel.GamePieces.Scissors
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking

class RockPaperScissorsViewModel : ViewModel() {
// Step 1 -> Basic game rules implementation
    /***
     * Rules of the game
     * 1- scissors beats paper
     * 2- paper beats rock
     * 3- rock beats scissors
     * 4- scissors vs scissors -> draw
     * 5- paper vs paper -> draw
     * 6- rock vs rock -> draw
     */

    var player1Count = 0
    var player2Count = 0

    enum class GamePieces {
        Rock, Paper, Scissors
    }

    fun gameOutput(gamePiece: GamePieces, gamePiece2: GamePieces): String {
        return when {
            ((gamePiece == Scissors && gamePiece2 == Paper) || (gamePiece == Paper && gamePiece2 == Scissors)) -> "${Scissors.name} Wins"
            ((gamePiece == Paper && gamePiece2 == Rock) || (gamePiece == Rock && gamePiece2 == Paper)) -> "${Paper.name} Wins"
            ((gamePiece == Rock && gamePiece2 == Scissors) || (gamePiece == Scissors && gamePiece2 == Rock)) -> "${Rock.name} Wins"
            else -> "Draw"
        }
    }

    // Step 2 -> If two player are playing
    // this requires a data class that contains name of the player and piece they played

    data class Player(val name: String, val selectedGamePiece: GamePieces)

    data class GameState(
        val player1: Player? = null,
        val player2: Player? = null,
        val gameResult: String? = null,
        val bestOf3Winner: String? = null,
        val player1Score: Int? = 0,
        val player2Score: Int? = 0
    )

    private val _state = MutableStateFlow(GameState())

    val state: StateFlow<GameState> = _state

    fun onPiecesSelected(gamePiece: GamePieces) {
        if (state.value.player1 == null) {
            _state.update { it.copy(player1 = Player("You", gamePiece)) }
        }
        if (state.value.player2 == null) {
            val selection = computerPick()
            _state.update { it.copy(player2 = Player("Computer", selection)) }
        }

        if (state.value.bestOf3Winner == null) {
            state.value.player1?.let { player1 ->
                state.value.player2?.let { player2 ->
                    val gamesResult = gameOutputWithPlayers(player1, player2)
                    _state.update { it.copy(gameResult = gamesResult) }
                }
            }
        }

        bestOf3Winner(state.value.player1, state.value.player2)
    }

    // when game ends -> reset game after 3 seconds
    fun restGameAfterASecond() {
        runBlocking {
            delay(1000L)
            if (state.value.bestOf3Winner == null) {
                _state.update { it.copy(player1 = null, player2 = null, gameResult = null) }
            }
        }
    }

    // create a gameOutput function that take in two player, checking what piece they picked and returning a winner
    fun gameOutputWithPlayers(
        player1: Player,
        player2: Player
    ): String { // make use of this when two player are playing instead of player vs computer
        return if (player1.selectedGamePiece == Scissors && player2.selectedGamePiece == Paper) {
            player1Count++
            _state.update { it.copy(player1Score = player1Count) }
            "${player1.name} Win"
        } else if (player1.selectedGamePiece == Paper && player2.selectedGamePiece == Scissors) {
            player2Count++
            _state.update { it.copy(player2Score = player2Count) }
            "${player2.name} Win"
        } else if (player1.selectedGamePiece == Paper && player2.selectedGamePiece == Rock) {
            player1Count++
            _state.update { it.copy(player1Score = player1Count) }
            "${player1.name} Win"
        } else if (player1.selectedGamePiece == Rock && player2.selectedGamePiece == Paper) {
            player2Count++
            _state.update { it.copy(player2Score = player2Count) }
            "${player2.name} Win"
        } else if (player1.selectedGamePiece == Rock && player2.selectedGamePiece == Scissors) {
            player1Count++
            _state.update { it.copy(player1Score = player1Count) }
            "${player1.name} Win"
        } else if (player1.selectedGamePiece == Scissors && player2.selectedGamePiece == Rock) {
            player2Count++
            _state.update { it.copy(player2Score = player2Count) }
            "${player2.name} Win"
        } else {
            "Draw"
        }
    }

// Step 3 -> Best of 3 is winner
    // Everytime a player wins -> add 1 (register this in gameOutputWithPlayers)
    // Player that get to 3 first -> wins

    fun bestOf3Winner(player1: Player?, player2: Player?) {
        when {
            state.value.player1Score == 3 -> {
                _state.update {
                    it.copy(
                        bestOf3Winner = formatWinnerText(player1),
                        gameResult = null
                    )
                }
            }

            state.value.player2Score == 3 -> {
                _state.update {
                    it.copy(
                        bestOf3Winner = formatWinnerText(player2),
                        gameResult = null,
                    )
                }
            }
        }
    }


    private fun formatWinnerText(player: Player?): String {
        return if (player?.name == "You") {
            "You are the Master Winner"
        } else {
            "Computer is the Master Winner"
        }
    }

    // after best of 3 winner is announced -> enable button and reset game
    fun onGameRest() {
        _state.update {
            it.copy(
                player1 = null,
                player2 = null,
                gameResult = null,
                bestOf3Winner = null,
                player1Score = 0,
                player2Score = 0
            )
        }
        player1Count = 0
        player2Count = 0
    }

    // player vs computer
    // player selects their option, machine picks by random

    fun computerPick() = GamePieces.entries.random()
}

/***
 * To improve this -> immutable variables....
 *
 */
