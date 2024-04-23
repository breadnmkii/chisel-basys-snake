package Game

import chisel3._
import chisel3.util._

/* 
 File hierarchy

 board button (input) --> file             --> direction signal (output) 
                          - debounce input |

 */

class PlayerInput extends Module {
  // four buttons on basys as UP, RIGHT, LEFT, and DOWN
  // middle button as PAUSE

    val io = IO(new Bundle {
        val buttons = Input(UInt(4.W))
        val snake_direction = Output(UInt(2.W))
        val isPressed = Output(UInt(1.W))
    })

    // Direction enum
    // 00 - UP
    // 01 - RIGHT
    // 10 - DOWN
    // 11 - LEFT
    val up :: right :: down :: left :: Nil = util.Enum(4)

    val lastDirection = RegInit(0.U(2.W))
    lastDirection := io.snake_direction



    when(io.buttons(0) && lastDirection =/= down) {
        io.snake_direction := up
        io.isPressed := 1.U
    }.elsewhen(io.buttons(1) && lastDirection =/= left) {
        io.snake_direction := right
        io.isPressed := 1.U
    }.elsewhen(io.buttons(2) && lastDirection =/= up) {
        io.snake_direction := down
        io.isPressed := 1.U
    }.elsewhen(io.buttons(3) && lastDirection =/= right) {
        io.snake_direction := left
        io.isPressed := 1.U
    }.otherwise {
        io.snake_direction := lastDirection
        io.isPressed := 0.U
    }
    
    


}
