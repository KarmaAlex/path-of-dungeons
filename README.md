# Path of dungeons
Codice per il progetto per il corso di laboratorio di programmazione ad oggetti, svolto interamente da me ed attualmente incompleto ma quasi concluso.
Si tratta di un gioco di ruolo in cui si può esplorare un dungeon generato casualmente.
Il dungeon è formato da tipi di stanze diversi (per ora solo le stanze con combattimenti sono implementate) come negozi, stanze che contengono nemici, stanze con segreti ed infine la stanza che contiene il boss che conlude il gioco.
Il party di giocatori può avere dimensione da 1 a 4 e ciascuno può scegliere tra una delle 4 classi disponibili, ogniuna con oggetti iniziali diversi ed abilità speciali.
I giocatori inoltre hanno un inventario in cui possono raccogliere ed equipaggiare gli oggetti generati casualmente dopo ogni battaglia oppure acquistati dai negozi con l'oro ottenuto dai nemici.
I combattimenti sono strutturati a turni, con l'ordine determinato dalla statistica "velocità" di ogni partecipante. In ogni turno il giocatore può scegliere la propria azione tramite l'interfaccia, e se si tratta di un'abilità che richiede un bersaglio di sceglierlo tra i nemici o compagni.
# Test
Sono presenti alcuni unit test per i metodi che si occupano di generazione casuale di oggetti e dungeon eseguibili con
```mvn test```

# Eseguire
Si può provare il programma con
```mvn javafx:run```

