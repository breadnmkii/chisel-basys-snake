package displayDriver

import chisel3._
import chisel3.util._

/*
*
* Module that selects the character value shown  
* outputs the "hot" character to be shown
* 
*/

class characterSelectFSM extends Module{
    val io = IO(new Bundle{
        val char0          = Input(UInt(7.W))
        val char1          = Input(UInt(7.W))
        val char2          = Input(UInt(7.W))
        val char3          = Input(UInt(7.W))

        val segmenents = Output(UInt(7.W)) 
        val anodes = Output(UInt(4.W)) 
    })

    //module to name and create anode values for characters
    val anodeSet = Module(new anodes)

    //enum of positions for the hot character being shown
    val pos0 :: pos1 :: pos2 :: pos3 :: Nil = Enum(4) //Note: Chisel only allows for constant state values, so we cant use io.char0 - io.char3 as the states 
    //Set initial state and register to hold state (also one to hold the hot char, which is output)
    val posReg = RegInit(pos0)

    //create bundle
    val hot_char = new Bundle{
        val segmenents = Reg(UInt(7.W))
        val anodes = Reg(UInt(4.W))
    }

    //refresh counter to switch states(position)  
    val refresh_counter = RegInit(0.U(32.W))   //was 19 bits for 400000, now 32 for all varying speeds 
    refresh_counter := refresh_counter + 1.U 

    //refresh every 4ms (per diligent reference)
    when(refresh_counter === 400000.U){  
        refresh_counter := 0.U

        //switch hot char
        //set bundle values based on input
        switch(posReg){
            is(pos0){
                hot_char.segmenents := io.char0 //0101010
                hot_char.anodes := anodeSet.io.an0 //0001
                posReg := pos1
            }
            is(pos1){
                hot_char.segmenents := io.char1 //Cat(io.char1(6), io.char1(5), io.char1(4), io.char1(3), io.char1(2), io.char1(1), io.char1(0))
                hot_char.anodes := anodeSet.io.an1 //0010
                posReg := pos2
            }
            is(pos2){
                hot_char.segmenents := io.char2//Cat(io.char2(6), io.char2(5), io.char2(4), io.char2(3), io.char2(2), io.char2(1), io.char2(0))
                hot_char.anodes := anodeSet.io.an2 //0100
                posReg := pos3               
            }
            is(pos3){
                hot_char.segmenents := io.char3 // Cat(io.char3(6), io.char3(5), io.char3(4), io.char3(3), io.char3(2), io.char3(1), io.char3(0))
                hot_char.anodes := anodeSet.io.an3 //1000
                posReg := pos0
            }
        }
    }

    //set output to the hot vector bundle value
    io.segmenents := hot_char.segmenents
    io.anodes := hot_char.anodes
}