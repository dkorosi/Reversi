# Specifikáció

## Reversi
A reversi egy kétszemélyes táblajáték amelyet 8x8-as táblán játszanak, 64 db
két különböző oldalú koronggal.
Több variánsa is létezik a játéknak, a feladatban a leggyakrabban
alkalmazott szabályrendszert valósítjuk meg (Othello).

### Szabályok
A táblára az egyik játékos *világos*, míg a másik *sötét* korongokat helyez el
felváltva. 

Egy lépés akkor érvényes, ha a soron következő játékos korongja
  - egy üres mezőre kerül
  - egy másik saját koronggal közrefogja az ellenfél korongját/korongjait
    (ezt megteheti vízsintesen, függőlegesen és átlósan is)

Egy érvényes lépés után az összes közrefogott korong megfordul, így színt vált.
Amennyiben a játékosnak nincs lehetősége érvényes lépésre, passzolnia kell,
így ismét az ellenfél lép. Viszont ha van lehetőség érvényes lépésre, akkor
tilos passzolni.

### A játék célja
A cél, hogy a korongok többségének színe a játékosé legyen.

### A játék kezdete
A táblán a játék kezdetén a középső négy rácsra átlósan el kell helyezni 2-2
sötét és világos korongot. A sötét korongok ÉK-DNY, míg a világos korongok 
ÉNY-DK irányúak.

### A játék vége
Ha megtelik a tábla, vagy ha egyik játékos sem tud érvényes lépést tenni,
azaz ha egymás után mindkét játékos passzol. Ilyenkor meg kell számolni a
táblán lévő korongokat szín szerint. A több koronggal rendelkező játékos nyer.
