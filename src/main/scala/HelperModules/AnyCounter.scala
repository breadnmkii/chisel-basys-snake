package Helper

import chisel3._
import chisel3.util._

//A counter that counts for n ticks and then sets io.flag high for one clock tick
//width is the bit-width of ticks so you can properly set your register

class AnyCounter(ticks : Int, width : Int) extends Module{
    val io = IO(new Bundle {
        val flag = Output(Bool())
    })
    val flagVal = RegInit(false.B)

    // val CNT_MAX = (100000000 - 1).U //for basys3

    val CNT_MAX = ((ticks - 1).U)
    val cntReg = RegInit(0.U(width.W))

    cntReg := Mux(cntReg === CNT_MAX, 0.U, cntReg + 1.U)

    flagVal := Mux(cntReg === CNT_MAX, true.B, false.B)  //true 1 clock tick

    io.flag := flagVal
}