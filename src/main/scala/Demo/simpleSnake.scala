package Demo

import chisel3._
import chisel3.util._
import _root_.circt
import chisel3.stage.{ChiselGeneratorAnnotation}



// WIP Top level wrapper board interface for snake game

class simpleSnake extends Module {
    val io = IO(new Bundle {
        val segments = Output(UInt(7.W))
        val anode    = Output(UInt(4.W)) //characters
    })

    val segments = "b1100010".U 

    io.segments := ~segments
    io.anode := ~io.sw

}


object simpleSnakeMain extends App{
    class simpleSnakeTop extends Module {
      	val io = IO(new Bundle {
        	val board_segments = Output(UInt(7.W))
        	val board_anode   = Output(UInt(4.W))
    	})

      	val game = Module(new simpleSnake)
        io.board_segments := game.io.segments
        io.board_anode := game.io.anode
    }  

    new circt.stage.ChiselStage().execute(args,Seq(circt.stage.CIRCTTargetAnnotation(circt.stage.CIRCTTarget.SystemVerilog), ChiselGeneratorAnnotation(() => new simpleSnakeTop)))
}

