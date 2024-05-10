class Environment {
  val dt: Double = 1 // [s] : time step; must be greater than latency times!
}
class Vehicle(dt: Double) {
// state attributes:
  val length: Int = 4 //[m] : vehicle length
  var position: Double = 0 // [m] : current position of center of mass of vehicle
  var speed: Double = 30 // [m/s] : current speed of vehicle
  var acceleration: Double = 0 // [m/s^2] : current acceleration of vehicle
//  var occupiedSpace = (position - length/2, position + length/2)

  // behaviour attributes:
  var desiredCruisingSpeed: Double = 30
  var brakingLatency: Double = 0.5 // [s] : reaction time for braking
  var brakingIntensity: Double = -5 // [m/s^2] : negative acceleration
  var accelerationLatency: Double = 0.75 // [s] : reaction time for accelerating
  var accelerationIntensity: Double = 4 // [m/s^2] : positive acceleration
  var minDistance: Int = 200 // [m] minimum distance accepted behind vehicle in front before braking
  var decision: Int = 0  // decision made by agent : brake (-1), accelerate (+1) or cruise (0)
  var latency: Double = 0  // [s] time to react for braking or accelerating

  // methods:
  def decisionMaking(positionFront: Double) = { // decision made by agent : brake, accelerate or cruise
    var previousDecision: Int = decision
    decision = 0
    if ((positionFront - position) < minDistance)(decision = -1)
    if ((positionFront - position) > minDistance * 3){
      if(speed < desiredCruisingSpeed) (decision = +1)
    }
  }

  def changeState(previousDecision: Int)= { // change of agents' state
    if(decision == 0) (acceleration = 0)
    if(decision == +1) {
      acceleration = accelerationIntensity
      if (previousDecision < decision) (latency = accelerationLatency)
    }
    if(decision == -1) {
      acceleration = brakingIntensity
      if (previousDecision > decision) (latency = brakingLatency)
    }
    speed = speed * latency + acceleration * (dt - latency)
    position = position + speed * dt
  }
}


object Traffic extends App {
    println("Hello world!")
}