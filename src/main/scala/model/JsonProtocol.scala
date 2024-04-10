package model

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import zio.schema.{DeriveSchema, Schema}

object JsonProtocol {
  implicit val customerDecoder: Decoder[Customer] = deriveDecoder
  implicit val customerEncoder: Encoder[Customer] = deriveEncoder

  implicit val customerSchema: Schema[Customer] = DeriveSchema.gen[Customer]
}
