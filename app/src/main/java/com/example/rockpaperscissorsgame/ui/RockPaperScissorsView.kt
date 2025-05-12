package com.example.rockpaperscissorsgame.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rockpaperscissorsgame.R
import com.example.rockpaperscissorsgame.RockPaperScissorsViewModel
import com.example.rockpaperscissorsgame.RockPaperScissorsViewModel.GamePieces
import com.example.rockpaperscissorsgame.RockPaperScissorsViewModel.GamePieces.Paper
import com.example.rockpaperscissorsgame.RockPaperScissorsViewModel.GamePieces.Rock
import com.example.rockpaperscissorsgame.RockPaperScissorsViewModel.GamePieces.Scissors
import com.example.rockpaperscissorsgame.ui.theme.RockPaperScissorsGameTheme

@Composable
fun RockPaperScissorsView() {

    val viewModel: RockPaperScissorsViewModel = viewModel()
    val state = viewModel.state.collectAsState()

    Column(
        modifier = Modifier.padding(bottom = 10.dp, top = 60.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        GamePieceSelection(viewModel::onPiecesSelected, state.value.bestOf3Winner)
        GameMainSection(
            state.value.player1?.selectedGamePiece,
            state.value.player2?.selectedGamePiece,
            state.value.player1Score,
            state.value.player2Score
        )

        OutcomeAndResetView(
            state.value.gameResult,
            state.value.bestOf3Winner,
            viewModel::onGameRest,
            viewModel::restGameAfterASecond
        )
    }
}

@Composable
fun OutcomeAndResetView(
    gameOutCome: String?,
    bestOf3Winner: String?,
    onResetTapped: () -> Unit,
    resetGame: () -> Unit
) {
    Column(verticalArrangement = Arrangement.Center) {

        if (gameOutCome != null) {
            Text("$gameOutCome", modifier = Modifier.padding(start = 180.dp))
            LaunchedEffect(null) {
                resetGame()
            }
        }

        if (bestOf3Winner != null) {
            Text("$bestOf3Winner", modifier = Modifier.padding(start = 120.dp))
        }

        Button(
            onClick = onResetTapped,
            modifier = Modifier.padding(top = 30.dp, start = 150.dp),
            enabled = bestOf3Winner != null
        ) {
            Text("Reset game")
        }
    }
}

@Composable
fun GameMainSection(
    player1Piece: GamePieces?,
    player2Piece: GamePieces?,
    player1Score: Int?,
    player2Score: Int?
) {
    val player1Image = when (player1Piece) {
        Rock -> R.drawable.rock
        Paper -> R.drawable.paper
        Scissors -> R.drawable.scissors
        else -> null
    }

    val player2Image = when (player2Piece) {
        Rock -> R.drawable.rock
        Paper -> R.drawable.paper
        Scissors -> R.drawable.scissors
        else -> null
    }

    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        Column {
            Box(
                modifier = Modifier
                    .padding(start = 30.dp)
                    .size(width = 50.dp, height = 25.dp)
            ) {
                Text("You: $player1Score")
            }

            Box(
                modifier = Modifier
                    .padding(start = 30.dp)
                    .size(width = 150.dp, height = 150.dp)
                    .background(Color.LightGray)
            ) {
                player1Image?.let {
                    Image(painterResource(it), null, modifier = Modifier.size(150.dp))
                }
            }
        }
        VerticalDivider(modifier = Modifier.height(175.dp))
        Column {
            Box(
                modifier = Modifier
                    .size(width = 120.dp, height = 25.dp)
            ) {
                Text(" Computer: $player2Score")
            }
            Box(
                modifier = Modifier
                    .size(width = 150.dp, height = 150.dp)
                    .background(Color.LightGray)
            ) {
                player2Image?.let {
                    Image(
                        painter = painterResource(it),
                        contentDescription = null,
                        modifier = Modifier.size(150.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun GamePieceSelection(onPieceSelected: (GamePieces) -> Unit, bestOf3Winner: String?) {
    Text("Pick a game piece", modifier = Modifier.padding(start = 20.dp))

    Row(
        modifier = Modifier.padding(start = 45.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Box(modifier = Modifier.clickable(enabled = bestOf3Winner == null) {
            onPieceSelected(Rock)
        }) { // when this is clicked -> player picked a rock
            Image(
                painter = painterResource(R.drawable.rock),
                contentDescription = "Rock",
                modifier = Modifier.size(100.dp)
            )
        }
        Box(modifier = Modifier.clickable(enabled = bestOf3Winner == null) {
            onPieceSelected(Paper)
        }) {
            Image(
                painter = painterResource(R.drawable.paper),
                contentDescription = "Paper",
                modifier = Modifier.size(100.dp)
            )
        }
        Box(modifier = Modifier.clickable(enabled = bestOf3Winner == null) {
            onPieceSelected(
                Scissors
            )
        }) {
            Image(
                painter = painterResource(R.drawable.scissors),
                contentDescription = "Scissors",
                modifier = Modifier.size(100.dp)
            )
        }

    }
}


// game piece selection options
// when selected -> show game piece pic bigger under with a divider that shows the opponent choice
// have a text to show the winner
// add a reset button below that

// each section should have a counter of each game they won
// Have a bigger text that cover the whole view to show who the master winner is


@Preview
@Composable
fun RockPaperScissorsViewPreview() {
    RockPaperScissorsGameTheme {
        RockPaperScissorsView()
    }
}


@Preview
@Composable
fun GamePieceSelectionPreview() {
    RockPaperScissorsGameTheme {
        GamePieceSelection({}, null)
    }
}

@Preview
@Composable
fun GameMainSectionPreview() {
    RockPaperScissorsGameTheme {
        GameMainSection(Paper, Scissors, 1, 2)
    }
}

@Preview
@Composable
fun OutcomeAndResetViewPreview() {
    RockPaperScissorsGameTheme {
        OutcomeAndResetView(null, "", {}) { }
    }
}
