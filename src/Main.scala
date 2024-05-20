import scala.collection.mutable.ArrayBuffer
import hevs.graphics.FunGraphics
import java.awt.Color


object SystemData {
  val dt: Double = 1.0 // [s] : time step; must be greater than latency times!
}

class SimOrchestrator() {
  val roadLength: Int = 100000 // [m]
  var counter: Int = 1
  var numberOfVehicles: Int = 1
  var vehicleSet: ArrayBuffer[Vehicle] = ArrayBuffer(new Vehicle(index = counter))

  def createVehicle(): Unit = {
    counter += counter
    numberOfVehicles += numberOfVehicles
    val nextVehicle = new Vehicle(index = counter)
    // initialise next vehicle with its own values (e.g. random with normal distribution)
    nextVehicle.length = 4 //[m] : vehicle length
    nextVehicle.speed = 10 // [m/s] : current speed of vehicle
    nextVehicle.desiredCruisingSpeed = 35
    nextVehicle.brakingLatency = 0.5 // [s] : reaction time for braking
    nextVehicle.brakingIntensity = -2.5 // [m/s^2] : negative acceleration
    nextVehicle.accelerationLatency = 0.75 // [s] : reaction time for accelerating
    nextVehicle.accelerationIntensity = 1 // [m/s^2] : positive acceleration
    nextVehicle.minDistance = 100 // [m] minimum distance accepted behind vehicle in front before braking
    nextVehicle.latency = 0.5 //
    vehicleSet.append(nextVehicle)
  }
  //
  //  def removeVehicle(): Unit = {
  //  // find vehicle with greatest position and destroy it
  //    vehicleSet.remove(0)
  //    numberOfVehicles -= numberOfVehicles
  //  }

  val s = new FunGraphics(1500, 700, "Traffic dynamics")

  def display(pos: Double, t: Int, colour: Color): Unit = {
    s.setColor(colour)
    s.drawCircle(pos.toInt, t, 1)
  }

  def run(): Unit = {
    // update the position of the first vehicle:
    vehicleSet(0).decisionMaking(1000)
    vehicleSet(0).changeState()
    for (i <- 1 until numberOfVehicles) {
      println(s"Distance between vehicles : ${vehicleSet(i - 1).position - vehicleSet(i).position}")
      vehicleSet(i).decisionMaking(vehicleSet(i - 1).position - vehicleSet(i).position)
      vehicleSet(i).changeState()
    }
  }
}

class Vehicle(val index: Int) {
  // state attributes:
  val indexNumber: Int = index
  var length: Int = 4 //[m] : vehicle length
  var position: Double = 0 // [m] : current position of center of mass of vehicle
  var speed: Double = 10 // [m/s] : current speed of vehicle
  var acceleration: Double = 0 // [m/s^2] : current acceleration of vehicle
  //  var occupiedSpace = (position - length/2, position + length/2)

  // behaviour attributes:
  var desiredCruisingSpeed: Double = 30
  var brakingLatency: Double = 0.5 // [s] : reaction time for braking
  var brakingIntensity: Double = -2.5 // [m/s^2] : negative acceleration
  var accelerationLatency: Double = 0.75 // [s] : reaction time for accelerating
  var accelerationIntensity: Double = 1 // [m/s^2] : positive acceleration
  var minDistance: Int = 100 // [m] minimum distance accepted behind vehicle in front before braking
  var decision: Int = 0 // decision made by agent : brake (-1), accelerate (+1) or cruise (0)
  var previousDecision: Int = decision
  var latency: Double = 0.5 // [s] time to react for braking or accelerating

  // methods:
  def decisionMaking(distanceInFront: Double) = { // decision made by agent : brake, accelerate or cruise
    // println(s"distanceInFront : ${distanceInFront} and minDistance : ${minDistance}")
    decision = 0
    if (distanceInFront < minDistance) (decision = -1)
    if (distanceInFront > minDistance * 2) {
      if (speed < desiredCruisingSpeed) (decision = +1)
    }
  }

  def changeState() = { // change of agents' state
    if (decision == 0) (acceleration = 0)
    if (decision == +1) {
      acceleration = accelerationIntensity
      if (previousDecision < decision) (latency = accelerationLatency)
    }
    if (decision == -1) {
      acceleration = brakingIntensity
      if (previousDecision > decision) (latency = brakingLatency)
    }
    speed = speed + acceleration * (SystemData.dt)
    position = position + speed * SystemData.dt
  }

}


object TrafficSimulator extends App {
  val simOrch: SimOrchestrator = new SimOrchestrator

  for (i <- 1 to 10) {
    simOrch.run
    println(s"Vehicle speed = ${simOrch.vehicleSet(0).speed}")
    println(s"Vehicle position = ${simOrch.vehicleSet(0).position}")
    simOrch.display(simOrch.vehicleSet(0).position/10, i, Color.blue)
  }

  simOrch.createVehicle()

  for (i <- 11 to 1000) {
    simOrch.run
    println(s"Vehicle1 speed = ${simOrch.vehicleSet(0).speed}")
    println(s"Vehicle1 position = ${simOrch.vehicleSet(0).position}")
    simOrch.display(simOrch.vehicleSet(0).position/10, i, Color.blue)
    println(s"Vehicle2 speed = ${simOrch.vehicleSet(1).speed}")
    println(s"Vehicle2 position = ${simOrch.vehicleSet(1).position}")
    simOrch.display(simOrch.vehicleSet(1).position/10, i, Color.red)
  }
}
