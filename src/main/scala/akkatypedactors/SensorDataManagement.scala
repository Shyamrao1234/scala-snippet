package akkatypedactors

import akka.actor.typed.Behavior


sealed trait SensorCommand
final case class ReadSensor() extends SensorCommand
final case class SensorReading(sensorId:String,value:String) extends SensorCommand

sealed trait AggregatorCommand
final case class AggregatorReadings(readings:SensorReading)

sealed trait AlertCommand
final case class TriggerAlert(sensorId:String,value:Double) extends AlertCommand

object SensorActor {
   def apply():Behavior[SensorReading] = ???




}


object SensorDataManagement  extends App{





}
