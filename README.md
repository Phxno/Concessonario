# Concessonario

--- Linee Guida ---

1. Prova d’esame del modulo/insegnamento di Ingegneria del Software
    La prova d’esame comprende la discussione di un progetto di sviluppo di software, da svolgere con i linguaggi e gli strumenti illustrati a lezione e nel corso delle attività di laboratorio.
    I linguaggi da usare sono Java o Phyton, con le relative tecniche di sviluppo delle interfacce grafiche. Per l’uso di altri linguaggi, gli studenti devono preventivamente concordare le modalità di sviluppo del software con il docente (vanno comunque considerati linguaggi orientati agli oggetti).
    L’esame orale potrà poi continuare o iniziare con domande relative alle varie parti del programma dell’insegnamento.
2. Produzione del sistema software e della documentazione di progetto relativa
    Il sistema software (completamente funzionante) sarà relativo ad uno dei tre esempi dettagliati nei requisiti attraverso i documenti presenti sulla piattaforma di e-learning di Ingegneria del Software. È richiesto che il software sia dotato di una opportuna interfaccia grafica. È anche possibile che gli studenti propongano al docente progetti di sistemi software in altri ambiti. Si consiglia agli studenti di adottare la metodologia di progettazione ed implementazione ad oggetti incrementale illustrata a lezione e di realizzare il progetto a gruppi di due o tre persone (con verifica delle tecniche di pair-programming e pair-designing). Gruppi più numerosi sono ammessi, ma vanno concordati prima con il docente.
    La progettazione e l’implementazione dovranno considerare l’applicabilità dei principali pattern di progettazione e di architetture discussi a lezione.
-- La documentazione di progetto deve prevedere almeno (con approccio top-down):
    a. Use Case principali e relative schede di specifica
    b. Sequence diagram di dettaglio per i principali Use Case
    c. Activity Diagram relativo alle modalità di interazione/operatività del software
    d. Class Diagram e Sequence diagram del software progettato
    e. Descrizione dettagliata delle attività di test del software, con illustrazione di unit test, test dell’interazione e così via
    f. discussione dei principali pattern eventualmente adottati e della loro applicazione

--- CONSEGNA ---
Ingegneria del Software 2023-2024
Esercizio 3
Si vuole progettare un sistema informatico per la vendita online di automobili di un gruppo multi-concessionaria, con
la possibilità di configurare la versione di automobile desiderata.

Il gruppo vende auto di diversi modelli, raggruppati per marca. Per ogni modello di auto il sistema memorizza: un
nome univoco, una descrizione, le dimensioni (altezza, lunghezza, larghezza, peso, volume del bagagliaio), i possibili
motori con i relativi tipi di alimentazione (gasolio, benzina, ibrida, elettrica, ibrida plug-in, e così via), e diverse
immagini, che permettano di vedere l’auto da vari punti di vista e (potenzialmente) con differenti colori. Il sistema
registra tutte le auto a catalogo e definisce possibili optional: il colore, le possibili aggiunte (ad es., ruotino di scorta,
ruota di scorta, vetri oscurati, interni in pelle, ruote con diametri maggiori di quello standard). Per ogni optional si
definisce il prezzo e se è applicabile al modello scelto.
Per alcuni modelli uno sconto, che può variare da un mese all’altro, viene applicato direttamente in fase di costruzione
del preventivo ed opportunamente indicato nel preventivo. Il cliente deve anche indicare dove intende, nel caso di
acquisto, ritirare l’auto.

Un generico utente può configurare, senza autenticarsi, l’auto per la quale pensa di richiedere un preventivo. Al
momento in cui decide di richiedere un preventivo al gruppo, l’utente deve registrarsi (se non già registrato) e poi
accedere al sistema. Solo in seguito, può richiedere la valutazione del suo usato, allegando delle fotografie dell’auto
usata.
Nel caso di valutazione dell’usato, il preventivo verrà finalizzato da una persona del negozio, che provvederà a
valutare l’usato. Il preventivo potrà essere prodotto anche come file pdf.

Il sistema memorizza i preventivi effettuati. Entro 20 giorni il potenziale acquirente può confermare il preventivo e
pagare un acconto, per ordinare l’auto nella configurazione indicata. Il sistema provvederà a proporre una data di
consegna (a un mese, più 10 giorni per ogni optional richiesto), entro la quale avere l’auto ordinata.

Il gruppo ha diverse sedi in Italia, dove è possibile ritirare l’auto ordinata. Per ogni sede il sistema memorizza nome,
indirizzo completo e tutti gli ordini relativi a quella sede.

Il sistema deve permettere agli impiegati del gruppo di accedere con autenticazione al sistema e gestire i preventivi
che richiedono la valutazione dell’usato. Gli impiegati potranno poi gestire gli ordini e avvisare i clienti quando l’auto
ordinata è pronta per la consegna (dopo il pagamento dell’importo dovuto).

La segreteria amministrativa del gruppo è responsabile dell’inserimento delle info su modelli e optional di ogni marca.
Essa può accedere al sistema e visualizzare i preventivi per cliente, per marca e per negozio di consegna richiesto.
Il sistema deve permettere all’utente di configurare l’auto avendo un controllo del prezzo finale dell’auto ad ogni
momento.
