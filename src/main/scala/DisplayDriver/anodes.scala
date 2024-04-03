package displayDriver

import chisel3._
import chisel3.util._

/*
*
* Modulue that creates and names anodes 
* output: (1,0,0,0),(0,1,0,0),(0,0,1,0),(0,0,0,1)
* 
*/

class anodes extends Module{
    val io = IO(new Bundle{
        val an0 = Output(UInt(4.W))
        val an1 = Output(UInt(4.W))
        val an2 = Output(UInt(4.W))
        val an3 = Output(UInt(4.W))
    })
    //Anode display value of low(0) is on
    io.an0 := Cat(0.U, 0.U, 0.U, 1.U) //"b0001".U

    io.an1 := Cat(0.U, 0.U, 1.U, 0.U)

    io.an2 := Cat(0.U, 1.U, 0.U, 0.U)

    io.an3 := Cat(1.U, 0.U, 0.U, 0.U)

}