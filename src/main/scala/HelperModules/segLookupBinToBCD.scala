package Helper

import chisel3._
import chisel3.util._

/*
* segment lookup values for Binary to bcd conversion
* 
*/
// val segLookup = Module(new segLookup)
// segLookup.io.bcd := bin2bcd.io.bcd
// val a vector  := segLookup.io.out 

class segLookupBinToBCD extends Module {
    val io = IO(new Bundle{
        val in = Input(UInt(16.W))
        val SegVec = Output(Vec(4, UInt(7.W)))
    })
    
    //vectors to hold the values in each character and the segments of that character
    val charVector = VecInit(Seq(io.in(3, 0), io.in(7, 4), io.in(11, 8), io.in(15, 12)))
    val segVector = VecInit(Seq.fill(4)(0.U(7.W)))
    
    //generate seq(charVal,i),(cV,i+1)....
    for ((charVal, i) <- charVector.zipWithIndex) {
        val temp = Wire(UInt(7.W))

        //match the value (1-9 and a-f) with the segments it takes to light it up (0= off, 1 is on)
        temp := MuxCase(Cat(1.U, 1.U, 1.U, 1.U, 1.U, 1.U, 1.U), Seq( 

            //                   Cat( N ,  NE,  SE,   S,  SW,  NW, mW) //Order now matches
            //                   Cat( CA,  CB,  CC,  CD,  CE,  CF, CG)
            (charVal === 0.U) -> Cat(1.U, 1.U, 1.U, 1.U, 1.U, 1.U, 0.U),
            (charVal === 1.U) -> Cat(0.U, 1.U, 1.U, 0.U, 0.U, 0.U, 0.U), //b0110000.U
            (charVal === 2.U) -> Cat(1.U, 1.U, 0.U, 1.U, 1.U, 0.U, 1.U),
            (charVal === 3.U) -> Cat(1.U, 1.U, 1.U, 1.U, 0.U, 0.U, 1.U),
            (charVal === 4.U) -> Cat(0.U, 1.U, 1.U, 0.U, 0.U, 1.U, 1.U),
            (charVal === 5.U) -> Cat(1.U, 0.U, 1.U, 1.U, 0.U, 1.U, 1.U),
            (charVal === 6.U) -> Cat(1.U, 0.U, 1.U, 1.U, 1.U, 1.U, 1.U),
            (charVal === 7.U) -> Cat(1.U, 1.U, 1.U, 0.U, 0.U, 0.U, 0.U),
            (charVal === 8.U) -> Cat(1.U, 1.U, 1.U, 1.U, 1.U, 1.U, 1.U),
            (charVal === 9.U) -> Cat(1.U, 1.U, 1.U, 1.U, 0.U, 1.U, 1.U)


            /* 
            //                      Cat(mW , NW,  SW,   S,  SE,  NE,   N )
            // (charVal === 0.U)    -> Cat(0.U, 1.U, 1.U, 1.U, 1.U, 1.U, 1.U), 
            // (charVal === 1.U)    -> Cat(0.U, 0.U, 0.U, 0.U, 1.U, 1.U, 0.U),
            // (charVal === 2.U)    -> Cat(1.U, 0.U, 1.U, 1.U, 0.U, 1.U, 1.U),
            // (charVal === 3.U)    -> Cat(1.U, 0.U, 0.U, 1.U, 1.U, 1.U, 1.U), 
            // (charVal === 4.U)    -> Cat(1.U, 1.U, 0.U, 0.U, 1.U, 1.U, 0.U), 
            // (charVal === 5.U)    -> Cat(1.U, 1.U, 0.U, 1.U, 1.U, 0.U, 1.U),
            // (charVal === 6.U)    -> Cat(1.U, 1.U, 1.U, 1.U, 1.U, 0.U, 1.U),
            // (charVal === 7.U)    -> Cat(0.U, 0.U, 0.U, 0.U, 1.U, 1.U, 1.U),
            // (charVal === 8.U)    -> Cat(1.U, 1.U, 1.U, 1.U, 1.U, 1.U, 1.U),
            // (charVal === 9.U)    -> Cat(1.U, 1.U, 0.U, 1.U, 1.U, 1.U, 1.U),
             */






            

            // (charVal === "ha".U) -> Cat(1.U, 1.U, 1.U, 0.U, 1.U, 1.U, 1.U),
            // (charVal === "hb".U) -> Cat(1.U, 1.U, 1.U, 1.U, 1.U, 0.U, 0.U),
            // (charVal === "hc".U) -> Cat(0.U, 1.U, 1.U, 1.U, 0.U, 0.U, 1.U),
            // (charVal === "hd".U) -> Cat(1.U, 0.U, 1.U, 1.U, 1.U, 1.U, 0.U),
            // (charVal === "he".U) -> Cat(1.U, 1.U, 1.U, 1.U, 0.U, 0.U, 1.U),
            // (charVal === "hf".U) -> Cat(1.U, 1.U, 1.U, 0.U, 0.U, 0.U, 1.U),
        ))

        segVector(i) := temp
    }

    io.SegVec := segVector
}