class Environment {
  val dt: Double = 1 // [s] : time step
}
class Vehicle(dt: Double) {
// state attributes:
  val length: Int = 4 //[m] : vehicle length
  var position: Double = 0 // [m] : current position of center of mass of vehicle
  var speed: Double = 30 // [m/s] : current speed of vehicle
  var acceleration: Double = 0 // [m/s^2] : current acceleration of vehicle
  var occupiedSpace = (position - length/2, position + length/2)
// behaviour attributes:
  var desiredCruisingSpeed: Double = 30
  var brakingLatency: Double = 0.5 // [s] : reaction time for braking
  var brakingIntensity: Double = 5 // [m/s^2] : negative acceleration
  var accelerationLatency: Double = 1.0 // [s] : reaction time for accelerating
  var accelerationIntensity: Double = 4 // [m/s^2] : positive acceleration

  // methods:
  def changeState()= {
    // acceleration = 0 // [m/s^2] : current acceleration of vehicle
    speed = speed + acceleration * dt
    position = position + speed * dt
  }
}


object Traffic extends App {
    println("Hello world!")
}