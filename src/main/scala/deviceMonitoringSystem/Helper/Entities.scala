package deviceMonitoringSystem.Helper

object Entities {

  sealed trait GateWayCommand
  case class GateWayConnected(gatewayId:String)
  case class GateWayFailedToConnect(gatewayId:String)
  


}
