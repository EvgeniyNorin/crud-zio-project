package repo

import model.Customer
import zio.sql.ConnectionPool
import zio.stream.ZStream
import zio.{ZIO, ZLayer}

final class CustomerRepositoryImpl(
    pool: ConnectionPool
) extends CustomerRepository
    with PostgresTableDescription {

  val driverLayer: ZLayer[Any, Nothing, SqlDriver] =
    ZLayer
      .make[SqlDriver](
        ZLayer.succeed(pool),
        SqlDriver.live
      )

  override def findAll(): ZStream[Any, Throwable, Customer] = ???

  override def add(customer: Customer): ZIO[Any, Throwable, Unit] = {
    val query =
      insertInto(customers)(customerId, fName, lName)
        .values(
          (
            customer.id,
            customer.firstName,
            customer.lastName
          )
        )

    ZIO.logInfo(s"Query to insert customer is ${renderInsert(query)}") *>
      execute(query)
        .provideSomeLayer(driverLayer)
        .unit

  }

  override def updateCustomer(customer: Customer): ZIO[Any, Throwable, Unit] =
    ???

  override def delete(id: Int): ZIO[Any, Throwable, Unit] = ???
}

object CustomerRepositoryImpl {
  val live: ZLayer[ConnectionPool, Throwable, CustomerRepository] =
    ZLayer.fromFunction(new CustomerRepositoryImpl(_))
}
