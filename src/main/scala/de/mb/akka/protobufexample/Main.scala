package de.mb.akka.protobufexample

import de.mb.akka.protobufexample.actors.GarageActor

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object Main extends App {

  import de.mb.akka.protobufexample.persistence.GarageAggregate._

  private implicit  val timeout: Timeout  = Timeout(5 seconds)
  private           val system            = ActorSystem("system")
  private           val garageActor        = system.actorOf( Props[GarageActor] )

  private def askGarageActor(cmd: GarageCmd): GarageResponse = Await.result( (garageActor ? cmd).asInstanceOf[Future[GarageResponse]], timeout.duration )

  println("GetAllCar\t'" + askGarageActor( GetAllCarCmd() ) + "'")

  println("AddCar\t\t'" + askGarageActor( AddCarCmd(Car(0, "BMW F30 320d", 200)) ) + "'")
  println("GetAllCar\t'" + askGarageActor( GetAllCarCmd() ) + "'")

  println("AddCar\t\t'" + askGarageActor( AddCarCmd(Car(0, "BMW F30 320d", 200)) ) + "'")
  println("GetAllCar\t'" + askGarageActor( GetAllCarCmd() ) + "'")

  println("UpdateCar\t'" + askGarageActor( UpdateCarCmd(Car(0, "BMW F30 320d", 400)) ) + "'")
  println("GetAllCar\t'" + askGarageActor( GetAllCarCmd() ) + "'")

  println("UpdateCar\t'" + askGarageActor( UpdateCarCmd(Car(-1, "BMW F30 320d", 400)) ) + "'")
  println("GetAllCar\t'" + askGarageActor( GetAllCarCmd() ) + "'")

}