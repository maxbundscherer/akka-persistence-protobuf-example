package de.mb.akka.protobufexample.persistence

object GarageAggregate {

  sealed trait GarageCmd
  sealed trait GarageResponse

  // ~ Cmd and Response ~
  case class AddCarCmd(value: Car)          extends GarageCmd
  case class CarAlreadyExist()              extends GarageResponse

  case class UpdateCarCmd(value: Car)       extends GarageCmd
  case class CarNotFound()                  extends GarageResponse

  case class GetAllCarCmd()                 extends GarageCmd
  case class GetAllCar(value: Vector[Car])  extends GarageResponse

  case class GarageSuccess()                extends GarageResponse

  // ~ Models ~
  case class Car(id: Long, name: String, horsepower: Int)

}