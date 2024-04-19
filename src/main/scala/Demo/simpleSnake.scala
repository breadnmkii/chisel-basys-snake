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

    /* REGISTER/WIRE INITIALIZATIONS */
    val grid = Module(new GridLogic(3,8))   // 3x8 logical row
    grid.io.logicGrid(0) := "b00000000".U
    grid.io.logicGrid(1) := "b00000000".U
    grid.io.logicGrid(2) := "b00000000".U
    
    /* MODULE INITIALIZATIONS */
    // Display mods
    val characterSelect = Module(new CharacterSelectFSM)
    characterSelect.io.char0 := grid.io.segs(0)
    characterSelect.io.char1 := grid.io.segs(1)
    characterSelect.io.char2 := grid.io.segs(2)
    characterSelect.io.char3 := grid.io.segs(3)
    
    // Game mods
    val playerInput_mod = Module(new PlayerInput)
    val gameClk = Module(new AnyCounter(tickPeriod, 32))


    /* COMBINATIONAL LOGIC */
    switch (playerInput_mod.io.snake_direction) {
        is (playerInput_mod.up) {
            grid.io.logicGrid(0) := "b11111111".U
            grid.io.logicGrid(1) := "b10000001".U
            grid.io.logicGrid(2) := "b11111111".U
        }
        is (playerInput_mod.right) {
            grid.io.logicGrid(0) := "b10010011".U
            grid.io.logicGrid(1) := "b10010011".U
            grid.io.logicGrid(2) := "b10010011".U
        }
        is (playerInput_mod.down) {
            grid.io.logicGrid(0) := "b00000000".U
            grid.io.logicGrid(1) := "b10001100".U
            grid.io.logicGrid(2) := "b00001100".U
        }
        is (playerInput_mod.left) {
            grid.io.logicGrid(0) := "b00000011".U
            grid.io.logicGrid(1) := "b00110000".U
            grid.io.logicGrid(2) := "b11000000".U
        }
    }

    

    /* SEQUENTIAL LOGIC */

    // Executes every game clock tick
    when (gameClk.io.flag) {
        
        switch (playerInput_mod.io.snake_direction) {
            is (playerInput_mod.right) {
                
            }
            is (playerInput_mod.left) {
                
            }
            is (playerInput_mod.up) {

            }
            is (playerInput_mod.down) {
                
            }
        }   
    }


    /* PHYSICAL IO CONNECTIONS */
    // Output
    io.segments  :=  Reverse(~characterSelect.io.segments)
    io.anodes    :=  ~characterSelect.io.anodes

    io.dp := ~"b0".U        // enable all apples

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

      	val game = Module(new simpleSnake(100000000/6))

        // TO board
        io.board_segments := game.io.segments
        io.board_anodes := game.io.anodes
        io.board_dp := game.io.dp

        // TO game
        game.io.buttons := io.board_buttons
    }  

    new circt.stage.ChiselStage().execute(args,Seq(circt.stage.CIRCTTargetAnnotation(circt.stage.CIRCTTarget.SystemVerilog), ChiselGeneratorAnnotation(() => new simpleSnakeTop)))
}

