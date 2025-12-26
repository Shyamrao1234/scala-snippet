package akkatypedactors

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.mutable.{Map => MutableMap}

object BreakingActorEncapsulation {

  trait AccountCommand

  case class Deposit(cardId: String, amount: Double) extends AccountCommand
  case class Withdraw(cardId: String, amount: Double) extends AccountCommand
  case class CreateCreditCard(cardId: String) extends AccountCommand


  case object CheckCardStatuses extends AccountCommand
  trait CreditCardCommand
  case class AttachToAccount(balances: MutableMap[String, Double], cards: MutableMap[String, ActorRef[CreditCardCommand]]) extends CreditCardCommand

  case object CheckStatus extends CreditCardCommand

  object NaiveBankAccount {
    def apply(): Behavior[AccountCommand] = Behaviors.setup { context =>
      val accountBalances: MutableMap[String, Double] = MutableMap()
      val cardMap: MutableMap[String, ActorRef[CreditCardCommand]] = MutableMap()

      Behaviors.receiveMessage {
        case CreateCreditCard(cardId) =>
          val creditCardRef = context.spawn(CreditCard(cardId), cardId)
          creditCardRef ! AttachToAccount(accountBalances, cardMap)
          Behaviors.same
        case Deposit(cardId, amount)=>
          context.log.info(s"Depositing ${amount} via card ${cardId}")
          val getOldBalance=accountBalances.getOrElse(cardId,0)
          accountBalances += cardId -> (getOldBalance + amount)
          Behaviors.same

        case Withdraw(cardId,amount) =>
          val getOldBalance = accountBalances.getOrElse(cardId,0)
          if(getOldBalance < amount){
            context.log.warn(s"Attempted withdraw of ${amount} via card ${cardId} : Insufficient funds")
            Behaviors.same
          }else {
            context.log.info(s"Withdrawing $amount via $cardId")
            accountBalances += cardId -> (getOldBalance-amount)
            Behaviors.same
          }

      }
    }
  }


  object CreditCard {

    def apply(cardId: String): Behavior[CreditCardCommand] = ???

  }


  def main(args: Array[String]): Unit = {

  }

}
