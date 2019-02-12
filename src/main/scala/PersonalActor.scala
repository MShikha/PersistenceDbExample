import akka.actor.{ActorLogging, Props}
import akka.persistence.journal.{Tagged, WriteEventAdapter}
import akka.persistence.{PersistentActor, RecoveryCompleted, SnapshotOffer}

object PersonalActor {
  def props(friendId: String) = Props(new PersonalActor(friendId))
}

class PersonalActor(friendId: String) extends PersistentActor with ActorLogging {
  override val persistenceId = friendId
//  override val recovery = r

  var state = PersonalState()
  def updateState(event: PersonalEvent) = state = state.update(event)

  val receiveRecover: Receive = {
    case evt: PersonalEvent =>
      log.info(s"Replaying event: $evt")
      updateState(evt)
    case SnapshotOffer(_, recoveredState : PersonalState) =>
      log.info(s"Snapshot offered: $recoveredState")
      state = recoveredState
    case RecoveryCompleted => log.info(s"Recovery completed. Current state: $state")
  }

  val receiveCommand: Receive = {
    case AddFriend(friend) => persist(PersonalAdded(friend))(updateState)
    case RemoveFriend(friend) => persist(PersonalRemoved(friend))(updateState)
    case "snap" => saveSnapshot(state)
    case "print" => log.info(s"Current state: $state")
  }
}

case class AddFriend(friend: Friend)
case class RemoveFriend(friend: Friend)
case class Friend(id: String)
sealed trait PersonalEvent
case class PersonalAdded(friend: Friend) extends PersonalEvent
case class PersonalRemoved(friend: Friend) extends PersonalEvent

case class PersonalState(friends: Vector[Friend] = Vector.empty[Friend]) {
  def update(evt: PersonalEvent) = evt match {
    case PersonalAdded(friend) => copy(friends :+ friend)
    case PersonalRemoved(friend) => copy(friends.filterNot(_ == friend))
  }
  override def toString = friends.mkString(",")
}

class TaggingEventAdapter extends WriteEventAdapter {
  override def manifest(event: Any): String = ""

  def withTag(event: Any, tag: String) = Tagged(event, Set(tag))

  override def toJournal(event: Any): Any = event match {
    case _: PersonalAdded =>
      withTag(event, "friend-added")
    case _: PersonalRemoved =>
      withTag(event, "friend_removed")
    case _ => event
  }
}
