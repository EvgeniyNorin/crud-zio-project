package repo

import model.Customer
import zio.ZIO
import zio.stream.ZStream

trait CustomerRepository {

  def findAll(): ZStream[Any, Throwable, Customer]

  def add(customer: Customer): ZIO[Any, Throwable, Unit]

  def updateCustomer(customer: Customer): ZIO[Any, Throwable, Unit]

  def delete(id: Int): ZIO[Any, Throwable, Unit]
}

object CustomerRepository {
  def findAll(): ZStream[CustomerRepository, Throwable, Customer] =
    ZStream.serviceWithStream[CustomerRepository](_.findAll())

  def add(customer: Customer): ZIO[CustomerRepository, Throwable, Unit] =
    ZIO.serviceWithZIO[CustomerRepository](_.add(customer))

  def update(customer: Customer): ZIO[CustomerRepository, Throwable, Unit] =
    ZIO.serviceWithZIO[CustomerRepository](_.updateCustomer(customer))

  def delete(id: Int): ZIO[CustomerRepository, Throwable, Unit] =
    ZIO.serviceWithZIO[CustomerRepository](_.delete(id))
}
