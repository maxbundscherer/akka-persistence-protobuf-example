package de.mb.akka.protobufexample.utils

import akka.serialization.SerializerWithStringManifest

object ProtobufSerializer {

  import de.mb.akka.protobufexample.persistence.GarageAggregateProto

  final val manifest_GarageActor$AddCarEvt     = classOf[GarageAggregateProto.AddCarEvt]   .getName
  final val manifest_GarageActor$UpdateCarEvt  = classOf[GarageAggregateProto.UpdateCarEvt].getName

}

class ProtobufSerializer extends SerializerWithStringManifest {

  import ProtobufSerializer._
  import de.mb.akka.protobufexample.persistence.GarageAggregateProto

  /**
    * Serializer identifier
    * @return Int
    */
  override def identifier: Int = 9002

  /**
    * Get manifest (getClass.getName)
    * @param o Object
    * @return String
    */
  override def manifest(o: AnyRef): String = o.getClass.getName

  /**
    * Convert from object to binary
    * @param o Object
    * @return Array
    */
  override def toBinary(o: AnyRef): Array[Byte] = o match {

    case z: GarageAggregateProto.AddCarEvt      => z.toByteArray
    case z: GarageAggregateProto.UpdateCarEvt   => z.toByteArray

  }

  /**
    * Convert from binary to object
    * @param bytes Array
    * @param manifest String
    * @return Object
    */
  override def fromBinary(bytes: Array[Byte], manifest: String): AnyRef = {

    manifest match {

      case `manifest_GarageActor$AddCarEvt`     => GarageAggregateProto.AddCarEvt.parseFrom(bytes)
      case `manifest_GarageActor$UpdateCarEvt`  => GarageAggregateProto.AddCarEvt.parseFrom(bytes)

    }

  }

}