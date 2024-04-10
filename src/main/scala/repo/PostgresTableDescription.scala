package repo

import model.Customer
import zio.schema.{DeriveSchema, Schema}
import zio.sql.macros.TableSchema
import zio.sql.postgresql.PostgresJdbcModule

trait PostgresTableDescription extends PostgresJdbcModule {

  implicit val customerSchema = DeriveSchema.gen[Customer]

  val customers = defineTable[Customer]

  val (customerId, fName, lName) = customers.columns
}
