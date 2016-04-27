# DistributedTron
<ul>
  <li>Progetto di Sistemi Distribuiti @ UniBo - Alma Mater Studiorum
  <li>Implementazione del famoso gioco Tron, su sistema distribuito di host tramite l'utilizzo della tecnologia RMI(Remote Method Invocation)
  <li>Linguaggio di sviluppo Java(JDK 7)
  <li>Configurazione logica ad anello unidirezionale, tollerante ai guasti di tipo CRASH (Fail Stop)
  <li>Ambiente di sviluppo: Eclipse
  <li>Numero di giocatori: da 1 a 6 [giocare con 1 solo Player non ha molto senso, nato per fare testing sull'App]
  <li>Testato su LAN Ethernet, possibilità anche di giocare al di fuori di semplici LAN --> con IP fissi e porta 1234 del Router accessibile
</ul>

# Installazione
<ol>
 <li>aprire il progetto con Eclipse:
    <ul>
      <li>fare l'Export del file Jar, il quale dopo può essere lanciato tramite il comando <i>Java -jar</i></li>
      <li>fare play dall'interfaccia grafica dell'IDE</li>
    </ul>
  </li>
  <li>tramite terminale, posizionarsi all'interno della cartella bin del progetto e dare il comando:
    <ul>
      <li><i>java start.StartPlay</i></li>
    </ul>
  </li>
</ol>

# Registrazione al gioco e avvio
Come prima cosa <b>uno ed uno solo</b> dei giocatori deve fungere da <i>Servizio di Registrazione</i>, dando così la possibilità agli altri giocatori di registrarsi al servizio di gioco. Ciò è possibile farlo cliccando sulla spunta <i>"Sono il server"</i>, indicando il numero di giocatori che si intende far giocare e premendo il pulsante <i>"Connetti"</i>:
<p align="center">
  <img src="/img/Server.png" width="350"/>
</p>

I restanti giocatori(oltre al giocatore <i>Andrea</i> di colore rosso che funge anche da <i>Servizio di Registrazione</i>, come mostrato nello screenshot precedente) non devono far altro che inserire un proprio username, L'IP del <i>Servizio di Registrazione</i> e premere il pulsante <i>"Connetti"</i>:
<p align="center">
  <img src="/img/Slave.png" width="350"/>
</p>

# Credits
<ul>
  <li>Andrea Sghedoni [ <a href="andre.sghedoni@gmail.com">andre.sghedoni@gmail.com</a> ]
  <li>Orgest Shehaj
  <li>Antonio Carbonara 
</ul>
