import akka.actor.ActorSystem
import akka.persistence.jdbc.query.scaladsl.JdbcReadJournal
import akka.persistence.query.PersistenceQuery
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink

object PersonalJournalReader extends App {
  implicit val system = ActorSystem()

  implicit val mat = ActorMaterializer()(system)

  val queries: JdbcReadJournal = PersistenceQuery(system).readJournalFor[JdbcReadJournal](JdbcReadJournal.Identifier)

  val laura = system.actorOf(PersonalActor.props("Laura1"))
//  val maria = system.actorOf(FriendActor.props("Maria1"))
//  laura ! AddFriend(Friend("Hector1"))
//  laura ! AddFriend(Friend("Nancy1"))
//  maria ! AddFriend(Friend("Oliver1"))
//  maria ! AddFriend(Friend("Steve1"))
  laura ! RemoveFriend(Friend("Laura"))
//  system.scheduler.scheduleOnce(5 second, maria, AddFriend(Friend("Steve1")))
//  system.scheduler.scheduleOnce(10 second, maria, RemoveFriend(Friend("Laura1")))
//  Thread.sleep(2000)

  queries.eventsByTag("friend-added", 0L).map(e => log(e.persistenceId,e.event)).to(Sink.ignore).run()
  queries.eventsByPersistenceId("Maria",0L,Long.MaxValue).map(e => log(e.persistenceId, e.event)).to(Sink.ignore).run()


  def log(id: String, evt: Any) = system.log.info(s"Id [$id] Event [$evt]")
}

