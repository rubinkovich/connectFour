package connectfour

const val FREE_SPACE = ' '
var boardMap = List(1) { List(1) { FREE_SPACE} } as MutableList<MutableList<Char>>
val symbols = listOf('o', '*')
var gamesAmount = 0

fun main() {
    println("Connect Four")
    val names: List<String> = inputNames()
    val players = listOf(Player(name = names[0], symbol = symbols[0]), Player(name = names[1], symbol = symbols[1]))
    val (rows, columns) = inputDimensions()
    val gamesAmount = numberOfGames()

    println(
        "${players[0].name} VS ${players[1].name}\n" +
                "$rows X $columns board"
    )
    when (gamesAmount) {
        1 -> println("Single game")
        else -> println("Total $gamesAmount games")
    }

    TotalGame@for (game in 0 until gamesAmount) {
        if (gamesAmount > 1) println("Game #${game + 1}")
        boardMap = List(rows) { List(columns) { FREE_SPACE } } as MutableList<MutableList<Char>>
        printBoard()

        var count = 0
        var column: Int
        Game@while (true) {
            val player = (count + game) % 2
            do {
                println("${players[player].name}'s turn:")
                val inputString = readln()

                if (inputString == "end") break@TotalGame

                try {
                    column = inputString.toInt()
                } catch (e: Exception) {
                    println("Incorrect column number")
                    continue
                }
                if (column !in 1..boardMap[0].size) {
                    println("The column number is out of range (1 - ${boardMap[0].size})")
                    continue
                }
                if (boardMap[0][column - 1] != FREE_SPACE) {
                    println("Column $column is full")
                    continue
                }
                move(players[player].symbol, column - 1)
                printBoard()
                break
            } while (true)

            if (boardFull()) {
                println("It is a draw")
                players[0].score++
                players[1].score++
                break@Game
            }

            if (winCheck(symbols[player])) {
                println("Player ${players[player].name} won")
                players[player].score += 2
                break@Game
            }
            count++
        }
        if (gamesAmount > 1) {
            println("Score\n" +
                    "${players[0].name}: ${players[0].score} ${players[1].name}: ${players[1].score}")
        }
    }
    println("Game over!")
}

private fun numberOfGames(): Int {
    while (true) {
        println(
            "Do you want to play single or multiple games?\n" +
                    "For a single game, input 1 or press Enter\n" +
                    "Input a number of games:"
        )
        val input = readln()
        if (input == "") {
            gamesAmount = 1
            break
        }
        try {
            gamesAmount = input.toInt()
        } catch (e: Exception) {
            println("Invalid input")
            continue
        }
        if (gamesAmount < 1) {
            println("Invalid input")
            continue
        }
        break
    }
    return gamesAmount
}

class Player(val name: String, val symbol: Char, var score: Int = 0)

fun winCheck(c: Char): Boolean {
    var win = false
    val rows = boardMap.size
    val columns = boardMap[0].size
    val strings = mutableListOf<String>()
    var str = ""
    boardMap.forEach { strings.add(it.joinToString("")) }       //add rows
    for (i in 0 until columns) {                      //add columns
        str = ""
        boardMap.forEach { str += it[i] }
        strings.add(str)
    }           //add columns
    for (i in 0..rows + columns - 2) {                //add diagonals /
        str = ""
        for (row in 0 until rows) {
            for (column in 0 until columns) {
                if (row + column == i) {
                    str += boardMap[row][column]
                }
            }
        }
        strings.add(str)
    }           //add diagonals /
    for (i in (1 - rows) until columns) {           //add diagonals \
        str = ""
        for (row in 0 until rows) {
            for (column in 0 until columns) {
                if (column - row == i) {
                    str += boardMap[row][column]
                }
            }
        }
        strings.add(str)
    }           //add diagonals \
    for (str in strings) {
        if (str.contains("$c$c$c$c")) {
            win = true
            break
        }
    }
    return win
}

fun boardFull(): Boolean {
    var result = true
    for (i in boardMap) {
        if (i.contains(FREE_SPACE)) {
            result = false
            break
        }
    }
    return result
}

fun move(c: Char, column: Int) {
    for (i in boardMap.indices.reversed()) {
        if(boardMap[i][column] == FREE_SPACE) {
            boardMap[i][column] = c
            break
        }
    }
}

fun printBoard() {
    repeat(boardMap[0].size) { print(" ${it + 1}") }
    println()
    boardMap.forEach { println("║${it.joinToString("║")}║") }
    print("╚")
    repeat(boardMap[0].size - 1) { print("═╩") }
    println("═╝")
}

fun inputDimensions(): List<Int> {
    var inputString: String
    val dimensions = mutableListOf<Int>()
    while (true) {
        println("Set the board dimensions (Rows x Columns)\nPress Enter for default (6 x 7)")
        inputString = readln().lowercase()
        if (inputString == "") {
            dimensions.add(6)
            dimensions.add(7)
            break
        } else try {
            val (rows, columns) = inputString.split("x").map { it.trim().toInt() }
            if (rows in 5..9) {
                dimensions.add(rows)
            } else println("Board rows should be from 5 to 9")
            if (columns in 5..9) {
                dimensions.add(columns)
            } else println("Board columns should be from 5 to 9")
        } catch (e: Exception) {
            println("Invalid input")
        }
        if (dimensions.size != 2) {
            dimensions.clear()
        } else break
    }
    return dimensions
}

fun inputNames(): List<String> {
    val names = mutableListOf<String>()
    println("First player's name:")
    names.add(readln())
    println("Second player's name:")
    names.add(readln())
    return names
}
