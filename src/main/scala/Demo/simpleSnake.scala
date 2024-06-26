package Demo

import chisel3._
import chisel3.util._
import _root_.circt
import chisel3.stage.{ChiselGeneratorAnnotation}

import Helper.AnyCounter

import DisplayDriver.AnodeNames
import DisplayDriver.CharacterSelectFSM

import Game.PlayerInput
import Game.GridLogic
import Game.GameLogic
import DisplayDriver.CharacterSelectFSM



// WIP Top level wrapper board interface for snake game

class simpleSnake(tickPeriod: Int) extends Module {
    val io = IO(new Bundle {
        val segments = Output(UInt(7.W))
        val anodes   = Output(UInt(4.W)) // enable segments
        val dp       = Output(UInt(1.W)) // apples
        val buttons  = Input(UInt(4.W))  // controls
    })

    /* REGISTER/WIRE INITIALIZATIONS *//////////////////////////////////////////////////////
    val rows = 3
    val cols = 8

    // Flag indicating whether game started or not
    val gameStart = RegInit(0.U)

    // Initial board registers
    val gameBoard = Reg(Vec(rows, Vec(cols, UInt(1.W))))
    for (i <- 0 until rows) {   // Initialize gameBoard registers
        for (j <- 0 until cols) {
            gameBoard(i)(j) := 0.U
        }
    }
    
    // Registers for two snake segment coordinates
    val maxSnakeLen = 3
    val snakeHead = maxSnakeLen - 1
    val snakePos = Reg(Vec(maxSnakeLen, Vec(2, UInt(3.W))))
    // Initial positions
    /* Note: The top left corner of board looks like:
     * 0,0  0,1 0,2 ...
     * 1,0  1,1 ...
     * 2,0  ...
     */
    when (gameStart === 0.U) {
        snakePos(snakeHead) := VecInit(0.U, 0.U)    // head of snake
        snakePos(1) := VecInit(1.U, 0.U)
        snakePos(0) := VecInit(2.U, 0.U)            // tail of snake
    }


    /* MODULE INITIALIZATIONS *//////////////////////////////////////////////////////
    // GridLogic mod
    val grid = Module(new GridLogic(rows,cols))   // 3x8 logical grid
    grid.io.logicGrid(0) := gameBoard(0)
    grid.io.logicGrid(1) := gameBoard(1)
    grid.io.logicGrid(2) := gameBoard(2)
    
    // Display mods
    val characterSelect = Module(new CharacterSelectFSM)
    characterSelect.io.char0 := grid.io.segs(0)
    characterSelect.io.char1 := grid.io.segs(1)
    characterSelect.io.char2 := grid.io.segs(2)
    characterSelect.io.char3 := grid.io.segs(3)
    
    // Game mods
    val playerInput_mod = Module(new PlayerInput)
    val gameClk = Module(new AnyCounter(tickPeriod, 32))

    
    /* COMBINATIONAL LOGIC *//////////////////////////////////////////////////////

    // Refresh gameBoard with snakePos positions
    for (pos <- 0 until maxSnakeLen) {
        gameBoard(snakePos(pos)(0))(snakePos(pos)(1)) := 1.U
    }
    


    /* SEQUENTIAL LOGIC *//////////////////////////////////////////////////////

    // Executes every game clock tick
    when (gameClk.io.flag) {
        // Game started
        gameStart := 1.U
        
        // Back-propagate snakePos body nodes
        for (i <- 0 until snakeHead) {
            snakePos(i) := snakePos(i+1)
        }

        // Update snakePos head node
        switch (playerInput_mod.io.snake_direction) {
            is (playerInput_mod.right) {
                // row = row, col += 1
                snakePos(snakeHead) := VecInit(snakePos(snakeHead)(0), snakePos(snakeHead)(1)+1.U)
            }
            is (playerInput_mod.left) {
                // row = row, col -= 1
                snakePos(snakeHead) := VecInit(snakePos(snakeHead)(0), snakePos(snakeHead)(1)-1.U)
            }
            is (playerInput_mod.up) {
                // row -= 1, col = col
                snakePos(snakeHead) := VecInit(snakePos(snakeHead)(0)-1.U, snakePos(snakeHead)(1))
            }
            is (playerInput_mod.down) {
                // row += 1, col = col
                snakePos(snakeHead) := VecInit(snakePos(snakeHead)(0)+1.U, snakePos(snakeHead)(1))
            }
        }
    }


    /* PHYSICAL IO CONNECTIONS */
    // Output
    io.segments  :=  Reverse(~characterSelect.io.segments)
    io.anodes    :=  Reverse(~characterSelect.io.anodes)
    io.dp := ~"b0".U        // init off

    // Input
    playerInput_mod.io.buttons := io.buttons

}

object simpleSnakeMain extends App{
    class simpleSnakeTop extends Module {
      	val io = IO(new Bundle {
        	val board_segments = Output(UInt(7.W))
        	val board_anodes   = Output(UInt(4.W))
            val board_dp       = Output(UInt(1.W))
            val board_buttons  = Input(UInt(4.W))
    	})

      	val game = Module(new simpleSnake(100000000/2))

        // TO board
        io.board_segments := game.io.segments
        io.board_anodes := game.io.anodes
        io.board_dp := game.io.dp

        // TO game
        game.io.buttons := io.board_buttons
    }  

    new circt.stage.ChiselStage().execute(args,Seq(circt.stage.CIRCTTargetAnnotation(circt.stage.CIRCTTarget.SystemVerilog), ChiselGeneratorAnnotation(() => new simpleSnakeTop)))
}

