package http

import akka.actor.ActorSystem
import akka.http.javadsl.ConnectionContext
import akka.stream.ActorMaterializer

import java.io.InputStream
import java.security.{KeyStore, SecureRandom}
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}

object LowLevelHttps extends App {

  implicit val system = ActorSystem("Https")
  implicit val materializer = ActorMaterializer()


  //step - 1
  val ks: KeyStore = KeyStore.getInstance("PKCS12")
  val keyStoreFile: InputStream = getClass.getClassLoader.getResourceAsStream("keystore.pkcs12")

  val password = "akka-https".toCharArray
  ks.load(keyStoreFile, password)

  //step -2
  val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
  keyManagerFactory.init(ks, password)

  //step -3
  val trustManagerFactory = TrustManagerFactory.getInstance("SunX509")
  trustManagerFactory.init(ks)

  //step - 4
  val sslContext = SSLContext.getInstance("TLS")
  sslContext.init(keyManagerFactory.getKeyManagers, trustManagerFactory.getTrustManagers, new SecureRandom)

  val httpsConnectionContext = ConnectionContext.https(sslContext)


}
