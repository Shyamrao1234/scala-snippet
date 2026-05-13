package catstopic

object TCVariance {


  trait Animal

  class Cat extends Animal


  //contravariance
  trait SoundMaker[-T]
  implicit object AnimalSoundMaker extends SoundMaker[Animal]
  def makeSound[T](implicit soundMaker: SoundMaker[T]) = println("soundMaker")
  makeSound[Animal]
  makeSound[Cat]


  //covariance works for specific type
  trait AnimalShow[+T] {
    def show: String
  }
  implicit object GeneralAnimalShow extends AnimalShow[Animal] {
    override def show = "everywhere animal"
  }
  implicit object CatShow extends AnimalShow[Cat] {
    override def show: String = "cats everywhere"
  }

  def organizeShow[T](implicit event:AnimalShow[T]) = event.show

  println(organizeShow[Cat])// this will come fine
//  println(organizeShow[Animal]) //compilation error



}
