package DisplayDriver

import chisel3._
import chisel3.util._
import _root_.circt
import chisel3.stage.{ChiselGeneratorAnnotation}

// NOTE: import any helper modules here

/*
* Display driver for adaptng snake grid onto the 7-segment displays
*/

class topLevelDisplay() extends Module{
    val io = IO(new Bundle{
        val input  = Input(Vec(4, UInt(7.W))) 

        //values to pass to board_ pins output
        val segments  = Output(UInt(7.W)) 
        val anode     = Output(UInt(4.W))
    })

    val chosenSegments = Wire(Vec(4, UInt(7.W)))

    /* TODO:
    * Implement a logic module to translate the snake grid into segment display,
    * including the 4 decimal points (apples)
    * 
    * - Will likely import the GridLogic module, then translate those signals to
    *   display outputs
    */

    
    chosenSegments := io.input


    val characterSelect = Module(new CharacterSelectFSM)
    characterSelect.io.char0 := chosenSegments(0)
    characterSelect.io.char1 := chosenSegments(1)
    characterSelect.io.char2 := chosenSegments(2)
    characterSelect.io.char3 := chosenSegments(3)


    //1 is on for a segmenet 0 is off until this point
    //See board spec for active low 
    io.segments  :=  ~characterSelect.io.segments
    io.anode     :=  ~characterSelect.io.anodes

}