# DistributedTron
Progetto di Sistemi Distribuiti @ UniBo - Alma Mater Studiorum
Implementazione del famoso gioco Tron, su sistema distribuito di host tramite l'utilizzo della tecnologia RMI(Remote Method Invocation). 
Linguaggio di sviluppo Java(JDK 7).
Configurazione logica ad anello unidirezionale, tollerante ai guasti di tipo CRASH (Fail Stop).
Ambiente di sviluppo: Eclipse.
Numero di giocatori: da 1 a 6 [giocare con 1 solo Player non ha molto senso, nato per fare testing sull'App].
Testato su LAN Ethernet, possibilità anche di giocare al di fuori di semplici LAN --> con IP fissi e porta 1234 del Router accessibile.

# Installazione
1) aprire il progetto con Eclipse:
1.1) fare l'Export del file Jar, il quale dopo può essere lanciato tramite il comando "Java -jar"
1.2) fare play dall'interfaccia grafica dell'IDE
2) tramite terminale, posizionarsi all'interno della cartella bin del progetto e dare il comando:
  java start.StartPlay
