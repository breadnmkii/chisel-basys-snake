package Helper

import chisel3._
import chisel3.util._


/* Implements the double dabble algorithm */

class binToBCD extends Module {
  val io = IO(new Bundle {
    val bin = Input(UInt(14.W)) //0127 = 000111111
    val bcd = Output(UInt(16.W)) //0000 0001 0010 0111
  })

  val bin = io.bin

  //make array to store BCD values for each iteration
  val bcdVals = Wire(Vec(15, UInt(16.W)))

  // Initialize the first iteration
  bcdVals(0) := 0.U(16.W)

  // Define a for-loop to iterate through 14 iterations
  for (i <- 0 until 14) {
    val bcdIn = bcdVals(i)
    val bcd3to0 = bcdIn(3, 0)
    val bcd7to4 = bcdIn(7, 4)
    val bcd11to8 = bcdIn(11, 8)
    val bcd15to12 = bcdIn(15, 12)

    //if nibble is greater than 5, add 3
    val bcd3to0Result = Mux(bcd3to0 >= 5.U, bcd3to0 + 3.U, bcd3to0)
    val bcd7to4Result = Mux(bcd7to4 >= 5.U, bcd7to4 + 3.U, bcd7to4)
    val bcd11to8Result = Mux(bcd11to8 >= 5.U, bcd11to8 + 3.U, bcd11to8)
    val bcd15to12Result = Mux(bcd15to12 >= 5.U, bcd15to12 + 3.U, bcd15to12)

    //set value for next iteration
    val temp = Cat(bcd15to12Result, bcd11to8Result, bcd7to4Result, bcd3to0Result)
    bcdVals(i+1) := Cat(temp(14,0), bin(13 - i))
  }

  io.bcd := bcdVals(14) // Use the final BCD value from the last iteration
}


