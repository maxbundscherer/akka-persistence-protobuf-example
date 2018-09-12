package de.mb.akka.protobufexample.actors

import akka.actor.ActorLogging
import akka.persistence.{PersistentActor, SnapshotOffer}

object GarageActor {

  import de.mb.akka.protobufexample.persistence.GarageAggregate._
  import de.mb.akka.protobufexample.persistence.GarageAggregateProto

  trait GarageEvt

  val persistenceId: String = "garage-actor"
  val snapshotInterval: Int = 5

  case class GarageState(carItems: Vector[Car] = Vector.empty) {

    def addCar   (evt: GarageAggregateProto.AddCarEvt): GarageState       = copy(carItems = carItems :+ Car(evt.id, evt.name, evt.horsepower))
    def updateCar(evt: GarageAggregateProto.UpdateCarEvt): GarageState    = copy(carItems = carItems.filter(_.id != evt.id) :+ Car(evt.id, evt.name, evt.horsepower))

  }

}

class GarageActor extends PersistentActor with ActorLogging {

  import GarageActor._
  import de.mb.akka.protobufexample.persistence.GarageAggregate._
  import de.mb.akka.protobufexample.persistence.GarageAggregateProto

  override def persistenceId: String = GarageActor.persistenceId

  /**
    * Mutable actor garageState
    */
  private var garageState: GarageState = GarageState()

  /**
    * Recovery behavior
    * @return Receive
    */
  override def receiveRecover: Receive = {

    case SnapshotOffer(_, snapshot: GarageState) =>

      log.debug("Restore snapshot '" + snapshot + "' now")
      garageState = snapshot

    case evt: GarageEvt => updateState(evt)
    case any: Any => log.warning("Get unhandled message in recovery behaviour '" + any + "'")

  }

  /**
    * Default behavior
    * @return Receive
    */
  override def receiveCommand: Receive = {

    case cmd: GarageCmd => processCmd(cmd)
    case any: Any => log.warning("Get unhandled message in default behaviour '" + any + "'")

  }

  /**
    * Process cmd
    * @param cmd Cmd
    */
  private def processCmd(cmd: GarageCmd): Unit = cmd match {

    case cmd: AddCarCmd =>

      if( garageState.carItems.exists(_.id == cmd.value.id) ) tellSender( CarAlreadyExist() )
      else persistAndUpdateState( GarageAggregateProto.AddCarEvt(cmd.value.id, cmd.value.name, cmd.value.horsepower) )

    case cmd: UpdateCarCmd =>

      if( ! garageState.carItems.exists(_.id == cmd.value.id) ) tellSender( CarNotFound() )
      else persistAndUpdateState( GarageAggregateProto.UpdateCarEvt(cmd.value.id, cmd.value.name, cmd.value.horsepower) )

    case cmd: GetAllCarCmd =>

      tellSender( GetAllCar(garageState.carItems) )

  }

  /**
    * Write event to journal and update state
    * @param evt Evt
    */
  private def persistAndUpdateState(evt: GarageEvt): Unit = persist(evt) { evt => updateState(evt) }

  /**
    * Update State
    * @param evt Evt
    */
  private def updateState(evt: GarageEvt): Unit = evt match {

    case evt: GarageAggregateProto.AddCarEvt =>

      garageState = garageState addCar evt
      snapshot()
      tellSender( GarageSuccess() )

    case evt: GarageAggregateProto.UpdateCarEvt =>

      garageState = garageState updateCar evt
      snapshot()
      tellSender( GarageSuccess() )

  }

  /**
    * Tell sender if recovery is not running
    * @param msg Result
    */
  private def tellSender(msg: GarageResponse): Unit = { if(!recoveryRunning) sender ! msg }

  /**
    * Do snapshot if recovery mode is not running and snapshot interval is reached and is not the first message
    */
  private def snapshot(): Unit = {

    if (
      !recoveryRunning &&
        lastSequenceNr % snapshotInterval == 0 &&
        lastSequenceNr != 0
    ) {

      log.debug("Do snapshot '" + garageState + "' now")
      saveSnapshot(garageState)
    }

  }

}