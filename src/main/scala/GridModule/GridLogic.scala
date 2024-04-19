package Game

import chisel3._
import chisel3.util._

/* 
 * module for grid state, including collision detection and bounds detection
 * probably implement as some 2d array of the 7-seg display encoded as 1 active, 0 per
 * location.
 * 
 * Also should provide an IO interface for the DisplayDriver
*/
class GridLogic(rows: Int, cols: Int) extends Module {
    val io = IO(new Bundle {
        val logicGrid = Input(Vec(rows, UInt(cols.W)))     // logical grid
        val segs = Output(Vec(cols-1, UInt(7.W)))          // physical display
    })

    // // Helper enum
    // object Cathode extends Enumeration {
    //     val A = 0
    //     val B = 1
    //     val C = 2
    //     val D = 3
    //     val E = 4
    //     val F = 5
    //     val G = 6
    // }


    /* GRID TO DISP TRANSLATION */

    // Create segments values for cols-1 segment displays
    val segVals = Wire(Vec(cols-1, Vec(7, UInt(1.W))))

    // Initialize seg vals
    for (i <- 0 until cols-1) {
        for (j <- 0 until 7) {
            segVals(i)(j) := 0.U
        }
    }

    // Connect seg vals
    for (i <- 0 until cols-1) {
        io.segs(i) := Cat(segVals(i)(0), segVals(i)(1), segVals(i)(2), segVals(i)(3), segVals(i)(4), segVals(i)(5), segVals(i)(6))
    }
    
    // Iterate through grid
    for (i <- 0 until rows) {

         // scan horizontal grid
        for (j <- 0 until cols-1) {
            // hardcoded for segment display
            i match {
                // top seg row (A cathode)
                case 0 => {
                    when (io.logicGrid(i)(j) && io.logicGrid(i)(j+1)) {
                        segVals(j)(0) := 1.U    // 0 = A
                    }
                }
                // mid seg row (G cathode)
                case 1 => {
                    when (io.logicGrid(i)(j) && io.logicGrid(i)(j+1)) {
                        segVals(j)(6) := 1.U    // 6 = G
                    }
                }
                // bot seg row (D cathode)
                case 2 => {
                    when (io.logicGrid(i)(j) && io.logicGrid(i)(j+1)) {
                        segVals(j)(3) := 1.U    // 3 = D
                    }
                }
            }
        }

        // scan vertical grid
    }



  // output a collision signal if any overlap
  //    - player collision signal
  //    - apple collision signal

  // and have bounds checking
  //    - player out of bounds signal

  // additional IO signals for segment display driver
}